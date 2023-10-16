package com.github.jawwy.keycloak.updatepassword;

import lombok.extern.jbosslog.JBossLog;
import org.keycloak.email.EmailException;
import org.keycloak.email.EmailTemplateProvider;
import org.keycloak.events.Event;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventType;
import org.keycloak.events.admin.AdminEvent;
import org.keycloak.events.admin.OperationType;
import org.keycloak.events.admin.ResourceType;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.UserModel;

//import javax.ws.rs.core.UriBuilder;
import jakarta.ws.rs.core.UriBuilder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@JBossLog
public class UpdatePasswordEventListener implements EventListenerProvider {

    static final String ID = "updatepassword-event-listener";

    private static final Set<EventType> SUPPORTED_USER_EVENT_TYPES = Set.of(EventType.UPDATE_PASSWORD);
    private final KeycloakSession session;

    public UpdatePasswordEventListener(KeycloakSession session) {
        this.session = session;
    }

    @Override
    public void onEvent(Event event) {

        // This event is not triggered by changes in the GUI
        if (!SUPPORTED_USER_EVENT_TYPES.contains(event.getType())) {
            return;
        }

        log.infof("onEvent event=%s type=%s realm=%suserId=%s", event, event.getType(), event.getRealmId(), event.getUserId());

        if (event.getType() == EventType.UPDATE_PASSWORD) {
            // log.warnf("Try to send first welcome email due to missing email. user=%s",  event.getUserId());
            // user was created via self-registration or identity-brokering
            trySendUpdatePasswordConfirmationEmail(event.getRealmId(), event.getUserId());
        }
    }

    @Override
    public void onEvent(AdminEvent event, boolean includeRepresentation) {
        log.infof("onEvent adminEvent=%s type=%s resourceType=%s resourcePath=%s includeRepresentation=%s", event, event.getOperationType(), event.getResourceType(), event.getResourcePath(), includeRepresentation);
        // NOOP

        if (event.getResourceType() == ResourceType.USER && event.getOperationType() == OperationType.UPDATE) {
            // user was created via admin-console or admin-rest API
            trySendUpdatePasswordConfirmationEmail(event.getRealmId(), event.getResourcePath().substring("users/".length()));
        }
    }


    private void trySendUpdatePasswordConfirmationEmail(String realmId, String userId) {

        RealmModel realm = session.realms().getRealm(realmId);
        // correction switch realm and userId vica versa
        UserModel user = session.users().getUserById(realm, userId);

        if (user.getEmail() == null) {
            log.warnf("Could not send UpdatePassword confirmation email due to missing email. realm=%s user=%s", realm.getId(), user.getUsername());
            return;
        } else {
            log.infof("Try to send UpdatePassword confirmation admin event email to Jawwy user=%s",user.getUsername());
        }

        UriBuilder authUriBuilder = UriBuilder.fromUri(session.getContext().getUri().getBaseUri());

        Map<String, Object> mailBodyAttributes = new HashMap<>();
        //mailBodyAttributes.put("baseUri", authUriBuilder.replacePath("/auth").build());
        mailBodyAttributes.put("username", user.getUsername());

        String realmName = realm.getDisplayName() != null ? realm.getDisplayName() : realm.getName();
        List<Object> subjectParams = List.of(realmName);

        try {
            EmailTemplateProvider emailProvider = session.getProvider(EmailTemplateProvider.class);
            emailProvider.setRealm(realm);
            emailProvider.setUser(user);
            // Don't forget to add the event-update_password.ftl (html and text) template to your theme.
            emailProvider.send("updatePasswordEmailSubject", subjectParams, "event-update_password.ftl", mailBodyAttributes);
        } catch (EmailException eex) {
            log.errorf(eex, "Failed to send UpdatePassword confirmation email. realm=%s user=%s", realm.getName(), user.getUsername());
        }
    }

    @Override
    public void close() {
        // NOOP
    }
}