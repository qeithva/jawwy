package com.github.jawwy.keycloak.custommessageuserlock;

import org.keycloak.authentication.authenticators.browser.UsernamePasswordForm;

public class CustomUsernameFormAuthenticator extends UsernamePasswordForm {

    @Override
    protected String tempDisabledError() {
        return Messages.ACCOUNT_TEMPORARILY_DISABLED;
    }
}