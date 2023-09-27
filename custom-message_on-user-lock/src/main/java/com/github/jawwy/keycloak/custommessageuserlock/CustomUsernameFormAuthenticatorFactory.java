import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;

public class CustomUsernameFormAuthenticatorFactory implements AuthenticatorFactory, ConfigurableAuthenticatorFactory {

    private static final CustomPasswordAuthenticator SINGLETON = new CustomUsernameFormAuthenticator();

    
    @Override
    public Authenticator create(KeycloakSession session) {
        return SINGLETON;
    }
    
    @Override
    public boolean isUserSetupAllowed() {
        return true;
    }

    @Override
    public String getHelpText() {
        return "Description of the created authenticator.";
    }

    @Override
    public String getDisplayType() {
        return "Custom Authentication Login";
    }

    @Override
    public String getReferenceCategory() {
        return "Custom Authentication Login";
    }

    @Override
    public void init(Config.Scope config) {

    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {

    }

    @Override
    public void close() {

    }

}