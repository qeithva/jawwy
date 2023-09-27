package com.github.jawwy.keycloak.custommessageuserlock;

import lombok.extern.jbosslog.JBossLog;
import org.keycloak.forms.login.freemarker.FreeMarkerLoginFormsProvider;
import org.keycloak.models.KeycloakSession;
import org.keycloak.theme.FreeMarkerUtil;
import org.keycloak.theme.Theme;
import org.keycloak.authentication.AuthenticationFlowContext;
import org.keycloak.authentication.authenticators.browser.UsernamePasswordForm;
import org.keycloak.credential.PasswordCredentialProvider;
import org.keycloak.models.UserModel;

@JBossLog
public class CustomUsernameFormAuthenticator extends UsernamePasswordForm {

    @Override
    protected String tempDisabledError() {
        return Messages.ACCOUNT_TEMPORARILY_DISABLED;
    }
}