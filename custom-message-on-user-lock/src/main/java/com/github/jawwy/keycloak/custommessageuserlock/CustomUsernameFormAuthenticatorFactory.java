package com.github.jawwy.keycloak.updatepassword;

import com.google.auto.service.AutoService;
import org.keycloak.Config;
import org.keycloak.events.EventListenerProvider;
import org.keycloak.events.EventListenerProviderFactory;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;

@AutoService(EventListenerProviderFactory.class)
public class UpdatePasswordEventListenerFactory implements EventListenerProviderFactory {

    @Override
    public EventListenerProvider create(KeycloakSession session) {
        return new UpdatePasswordEventListener(session);
    }

    @Override
    public void init(Config.Scope config) {
        // NOOP
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
        // NOOP
    }

    @Override
    public void close() {
        // NOOP
    }

    @Override
    public String getId() {
        return UpdatePasswordEventListener.ID;
    }
}
