package com.datapipe.jenkins.vault.credentials.common;

import com.cloudbees.plugins.credentials.CredentialsScope;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Item;
import hudson.model.ItemGroup;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import hudson.util.Secret;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import static com.datapipe.jenkins.vault.configuration.VaultConfiguration.engineVersions;
import static com.datapipe.jenkins.vault.credentials.common.VaultHelper.getVaultSecretKey;
import static org.apache.commons.lang.StringUtils.defaultIfBlank;

@SuppressWarnings("ALL")
public class VaultAzureServicePrincipalCredentialsImpl extends AbstractVaultBaseStandardCredentials
    implements VaultAzureServicePrincipalCredentials {


  public static final String DEFAULT_ARM_CLIENT_ID_KEY = "ARM_CLIENT_ID";
  public static final String DEFAULT_ARM_CLIENT_SECRET_KEY = "ARM_CLIENT_SECRET";
  public static final String DEFAULT_ARM_SUBSCRIPTION_ID_KEY = "ARM_SUBSCRIPTION_ID";
  public static final String DEFAULT_ARM_TENANT_ID_KEY = "ARM_TENANT_ID";

  private String clientIdKey;
  private String clientSecretKey;
  private String subscriptionIdKey;
  private String tenantIdKey;

  @DataBoundConstructor
  public VaultAzureServicePrincipalCredentialsImpl(CredentialsScope scope, String id, String description) {
    super(scope, id, description);
  }

  @NonNull
  public String getClientIdKey() {
    return clientIdKey;
  }

  @DataBoundSetter
  public void setClientIdKey(String clientIdKey) {
    this.clientIdKey = defaultIfBlank(clientIdKey, DEFAULT_ARM_CLIENT_ID_KEY);
  }

  @NonNull
  public String getClientSecretKey() {
    return clientSecretKey;
  }

  @DataBoundSetter
  public void setClientSecretKey(String clientSecretKey) {
    this.clientSecretKey = defaultIfBlank(clientSecretKey, DEFAULT_ARM_CLIENT_SECRET_KEY);
  }

  @NonNull
  public String getSubscriptionIdKey() {
    return subscriptionIdKey;
  }

  @DataBoundSetter
  public void setSubscriptionIdKey(String subscriptionIdKey) {
    this.subscriptionIdKey = defaultIfBlank(subscriptionIdKey, DEFAULT_ARM_SUBSCRIPTION_ID_KEY);
  }

  @NonNull
  public String getTenantIdKey() {
    return tenantIdKey;
  }

  @DataBoundSetter
  public void setTenantIdKey(String tenantIdKey) {
    this.tenantIdKey = defaultIfBlank(tenantIdKey, DEFAULT_ARM_TENANT_ID_KEY);
  }

  @NonNull
  @Override
  public String getSubscription() {
    String secretKey = defaultIfBlank(subscriptionIdKey, DEFAULT_ARM_SUBSCRIPTION_ID_KEY);
    return getVaultSecretKeyValue(secretKey);
  }

  @NonNull
  @Override
  public String getTenant() {
    String secretKey = defaultIfBlank(tenantIdKey, DEFAULT_ARM_TENANT_ID_KEY);
    return getVaultSecretKeyValue(secretKey);
  }

  @NonNull
  @Override
  public Secret getPassword() {
    String secretKey = defaultIfBlank(clientSecretKey, DEFAULT_ARM_CLIENT_SECRET_KEY);
    String secret = getVaultSecretKeyValue(secretKey);
    return Secret.fromString(secret);
  }

  @NonNull
  @Override
  public String getUsername() {
    String secretKey = defaultIfBlank(clientIdKey, DEFAULT_ARM_CLIENT_ID_KEY);
    return getVaultSecretKeyValue(secretKey);
  }

  @Extension
  public static class DescriptorImpl extends BaseStandardCredentialsDescriptor {

    @NonNull
    @Override
    public String getDisplayName() {
      return "Vault Azure Service Principal";
    }

    public FormValidation doTestConnection(
        @AncestorInPath ItemGroup<Item> context,
        @QueryParameter("path") String path,
        @QueryParameter("clientIdKey") String clientIdKey,
        @QueryParameter("clientSecretKey") String clientSecretKey,
        @QueryParameter("subscriptionIdKey") String subscriptionIdKey,
        @QueryParameter("tenantIdKey") String tenantIdKey,
        @QueryParameter("prefixPath") String prefixPath,
        @QueryParameter("namespace") String namespace,
        @QueryParameter("engineVersion") Integer engineVersion) {

      String clientId = null;
      try {
        clientId = getVaultSecretKey(path, defaultIfBlank(clientIdKey, DEFAULT_ARM_CLIENT_ID_KEY), prefixPath, namespace, engineVersion, context);
      } catch (Exception e) {
        return FormValidation.error("FAILED to retrieve clientId key: \n" + e);
      }

      try {
        getVaultSecretKey(path, defaultIfBlank(clientSecretKey, DEFAULT_ARM_CLIENT_SECRET_KEY), prefixPath, namespace, engineVersion, context);
      } catch (Exception e) {
        return FormValidation.error("FAILED to retrieve client secret key: \n" + e);
      }

      try {
        getVaultSecretKey(path, defaultIfBlank(subscriptionIdKey, DEFAULT_ARM_SUBSCRIPTION_ID_KEY), prefixPath, namespace, engineVersion, context);
      } catch (Exception e) {
        return FormValidation.error("FAILED to retrieve subscriptionId key: \n" + e);
      }

      try {
        getVaultSecretKey(path, defaultIfBlank(tenantIdKey, DEFAULT_ARM_TENANT_ID_KEY), prefixPath, namespace, engineVersion, context);
      } catch (Exception e) {
        return FormValidation.error("FAILED to retrieve tenantId key: \n" + e);
      }

      return FormValidation
          .ok("Successfully retrieved username " + clientId + " and other variables.");
    }


    @SuppressWarnings("unused") // used by stapler
    public ListBoxModel doFillEngineVersionItems(@AncestorInPath Item context) {
      return engineVersions(context);
    }

  }
}
