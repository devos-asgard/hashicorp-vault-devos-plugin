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
public class VaultRealmInfoCredentialsImpl extends AbstractVaultBaseStandardCredentials
    implements VaultRealmInfoCredentials {

    private static final Logger LOGGER = Logger
        .getLogger(VaultRealmInfoCredentialsImpl.class.getName());

  public static final String DEFAULT_RI_CLIENT_KEY = "RI_CLIENT_KEY";
  public static final String DEFAULT_RI_CLIENT_SECRET_KEY = "RI_CLIENT_SECRET_KEY";
  public static final String DEFAULT_RI_AUTH_URL_KEY = "RI_AUTH_URL_KEY";
  public static final String DEFAULT_RI_REALM_KEY = "RI_REALM_KEY";

  private String clientKey;
  private String clientSecretKey;
  private String authUrlKey;
  private String realmKey;
  private Supplier<Secret> client;
  private Supplier<Secret> clientSceret;
  private Supplier<Secret> authUrl;
  private Supplier<Secret> realm;

  @DataBoundConstructor
  public VaultRealmInfoCredentialsImpl(CredentialsScope scope, String id, String description,
      Supplier<Secret> client, Supplier<Secret> clientSceret, Supplier<Secret> authUrl, Supplier<Secret> realm) {
    super(scope, id, description);
    this.client = client;
    this.clientSceret = clientSceret;
    this.authUrl = authUrl;
    this.realm = realm;
  }

  @NonNull
  public String getClientKey() {
    return clientKey;
  }

  @DataBoundSetter
  public void setClientKey(String clientKey) {
    this.clientKey = defaultIfBlank(clientKey, DEFAULT_RI_CLIENT_KEY);
  }

  @NonNull
  public String getClientSecretKey() {
    return clientSecretKey;
  }

  @DataBoundSetter
  public void setClientSecretKey(String clientSecretKey) {
    this.clientSecretKey = defaultIfBlank(clientSecretKey, DEFAULT_RI_CLIENT_SECRET_KEY);
  }

  @NonNull
  public String getAuthUrlKey() {
    return authUrlKey;
  }

  @DataBoundSetter
  public void setAuthUrlKey(String authUrlKey) {
    this.authUrlKey = defaultIfBlank(authUrlKey, DEFAULT_RI_AUTH_URL_KEY);
  }

  @NonNull
  public String getRealmKey() {
    return realmKey;
  }

  @DataBoundSetter
  public void setRealmKey(String realmKey) {
    this.realmKey = defaultIfBlank(realmKey, DEFAULT_RI_REALM_KEY);
  }

  @NonNull
  @Override
  public String getAuthUrl() {
      if (authUrl == null) {
          LOGGER.log(Level.WARNING, "AuthUrl was null");
          return Secret.toString(Secret.fromString(getVaultSecretKeyValue(authUrlKey)));
      }
      return Secret.toString(authUrl.get());
  }

  @NonNull
  @Override
  public String getRealm() {
      if (realm == null) {
          LOGGER.log(Level.WARNING, "Realm was null");
          return Secret.toString(Secret.fromString(getVaultSecretKeyValue(realmKey)));
      }
      return Secret.toString(realm.get());
  }

  @NonNull
  @Override
  public Secret getPassword() {
      if (clientSceret == null) {
          LOGGER.log(Level.WARNING, "ClientSecret was null");
          return Secret.fromString(getVaultSecretKeyValue(clientSecretKey));
      }
      return clientSceret.get();
  }

  @NonNull
  @Override
  public String getUsername() {
      if (client == null) {
          LOGGER.log(Level.WARNING, "Client was null");
          return Secret.toString(Secret.fromString(getVaultSecretKeyValue(clientKey)));
      }
      return Secret.toString(client.get());
  }

  @Extension
  public static class DescriptorImpl extends BaseStandardCredentialsDescriptor {

    @NonNull
    @Override
    public String getDisplayName() {
      return "Vault Realm Information";
    }

    public FormValidation doTestConnection(
        @AncestorInPath ItemGroup<Item> context,
        @QueryParameter("path") String path,
        @QueryParameter("clientKey") String clientKey,
        @QueryParameter("clientSecretKey") String clientSecretKey,
        @QueryParameter("authUrlKey") String authUrlKey,
        @QueryParameter("realmKey") String realmKey,
        @QueryParameter("prefixPath") String prefixPath,
        @QueryParameter("namespace") String namespace,
        @QueryParameter("engineVersion") Integer engineVersion) {

      String client = null;
      try {
        client = getVaultSecretKey(path, defaultIfBlank(clientKey, DEFAULT_RI_CLIENT_KEY), prefixPath, namespace, engineVersion);
      } catch (Exception e) {
        return FormValidation.error("FAILED to retrieve client key: \n" + e);
      }

      try {
        getVaultSecretKey(path, defaultIfBlank(clientSecretKey, DEFAULT_RI_CLIENT_SECRET_KEY), prefixPath, namespace, engineVersion);
      } catch (Exception e) {
        return FormValidation.error("FAILED to retrieve client secret key: \n" + e);
      }

      try {
        getVaultSecretKey(path, defaultIfBlank(authUrlKey, DEFAULT_RI_AUTH_URL_KEY), prefixPath, namespace, engineVersion);
      } catch (Exception e) {
        return FormValidation.error("FAILED to retrieve authUrl key: \n" + e);
      }

      try {
        getVaultSecretKey(path, defaultIfBlank(realmKey, DEFAULT_RI_REALM_KEY), prefixPath, namespace, engineVersion);
      } catch (Exception e) {
        return FormValidation.error("FAILED to retrieve realm key: \n" + e);
      }

      return FormValidation
          .ok("Successfully retrieved client " + client + " and other variables.");
    }


    @SuppressWarnings("unused") // used by stapler
    public ListBoxModel doFillEngineVersionItems(@AncestorInPath Item context) {
      return engineVersions(context);
    }

  }
}
