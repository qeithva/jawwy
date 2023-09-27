package com.github.jawwy.keycloak.custommessageuserlock;

import lombok.extern.jbosslog.JBossLog;

@JBossLog
public class CustomUsernameFormAuthenticator extends UsernamePasswordForm {

    @Override
    protected String tempDisabledError() {
        return Messages.ACCOUNT_TEMPORARILY_DISABLED;
    }
}