package com.datapipe.jenkins.vault.credentials.common;

import com.cloudbees.plugins.credentials.CredentialsScope;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Item;
import hudson.model.ItemGroup;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import hudson.util.Secret;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import static com.datapipe.jenkins.vault.configuration.VaultConfiguration.engineVersions;
import static com.datapipe.jenkins.vault.credentials.common.VaultHelper.getVaultSecretKey;
import static org.apache.commons.lang.StringUtils.defaultIfBlank;

@SuppressWarnings("ALL")
public class VaultSecureTokenServiceCredentialsImpl extends AbstractVaultBaseStandardCredentials
    implements VaultSecureTokenServiceCredentials {

    private static final Logger LOGGER = Logger
        .getLogger(VaultSecureTokenServiceCredentialsImpl.class.getName());

  public static final String DEFAULT_USERNAME_KEY = "username";
  public static final String DEFAULT_PASSWORD_KEY = "password";
  public static final String DEFAULT_SECURITY_TOKEN = "security_token";

  private String usernameKey;
  private String passwordKey;
  private String securityTokenKey;
  private Supplier<String> useranme;
  private Supplier<Secret> password;
  private Supplier<String> securityToken;

  @DataBoundConstructor
  public VaultSecureTokenServiceCredentialsImpl(CredentialsScope scope, String id, String description,
      Supplier<String> useranme, Supplier<Secret> password, Supplier<String> securityToken) {
    super(scope, id, description);
    this.useranme = useranme;
    this.password = password;
    this.securityToken = securityToken;
  }

  @NonNull
  public String getUsernameKey() {
    return usernameKey;
  }

  @DataBoundSetter
  public void setUsernameKey(String usernameKey) {
    this.usernameKey = defaultIfBlank(usernameKey, DEFAULT_USERNAME_KEY);
  }

  @NonNull
  public String getPasswordKey() {
    return passwordKey;
  }

  @DataBoundSetter
  public void setPasswordKey(String passwordKey) {
    this.passwordKey = defaultIfBlank(passwordKey, DEFAULT_PASSWORD_KEY);
  }

  @NonNull
  public String getSecurityTokenKey() {
    return securityTokenKey;
  }

  @DataBoundSetter
  public void setSecurityTokenKey(String securityTokenKey) {
    this.securityTokenKey = defaultIfBlank(securityTokenKey, DEFAULT_SECURITY_TOKEN);
  }

  @NonNull
  @Override
  public String getUsername() {
      if (useranme == null) {
          LOGGER.log(Level.WARNING, "Username was null");
          return Secret.toString(Secret.fromString(getVaultSecretKeyValue(getUsernameKey())));
      }
      return useranme.get();
  }

  @NonNull
  @Override
  public Secret getPassword() {
      if (password == null) {
          LOGGER.log(Level.WARNING, "Password was null");
          return Secret.fromString(getVaultSecretKeyValue(passwordKey));
      }
      return password.get();
  }

  @NonNull
  @Override
  public String getSecurityToken() {
      if (securityToken == null) {
          LOGGER.log(Level.WARNING, "SecurityToken was null");
          return Secret.toString(Secret.fromString(getVaultSecretKeyValue(securityTokenKey)));
      }
      return securityToken.get();
  }

  @Extension
  public static class DescriptorImpl extends BaseStandardCredentialsDescriptor {

    @NonNull
    @Override
    public String getDisplayName() {
      return "Vault Secure Token Service Credentials";
    }

    public FormValidation doTestConnection(
        @AncestorInPath ItemGroup<Item> context,
        @QueryParameter("path") String path,
        @QueryParameter("usernameKey") String usernameKey,
        @QueryParameter("passwordKey") String passwordKey,
        @QueryParameter("securityTokenKey") String securityTokenKey,
        @QueryParameter("prefixPath") String prefixPath,
        @QueryParameter("namespace") String namespace,
        @QueryParameter("engineVersion") Integer engineVersion) {

      String username = null;
      try {
        username = getVaultSecretKey(path, defaultIfBlank(usernameKey, DEFAULT_USERNAME_KEY), prefixPath, namespace, engineVersion);
      } catch (Exception e) {
        return FormValidation.error("FAILED to retrieve username key: \n" + e);
      }

      try {
        getVaultSecretKey(path, defaultIfBlank(passwordKey, DEFAULT_PASSWORD_KEY), prefixPath, namespace, engineVersion);
      } catch (Exception e) {
        return FormValidation.error("FAILED to retrieve password key: \n" + e);
      }

      try {
        getVaultSecretKey(path, defaultIfBlank(securityTokenKey, DEFAULT_SECURITY_TOKEN), prefixPath, namespace, engineVersion);
      } catch (Exception e) {
        return FormValidation.error("FAILED to retrieve securityToken key: \n" + e);
      }

      return FormValidation
          .ok("Successfully retrieved accessKey " + username + " and other variables.");
    }


    @SuppressWarnings("unused") // used by stapler
    public ListBoxModel doFillEngineVersionItems(@AncestorInPath Item context) {
      return engineVersions(context);
    }

  }
}
