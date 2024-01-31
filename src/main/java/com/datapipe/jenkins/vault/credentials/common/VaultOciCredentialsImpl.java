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
public class VaultOciCredentialsImpl extends AbstractVaultBaseStandardCredentials
    implements VaultOciCredentials {

  private static final Logger LOGGER = Logger
      .getLogger(VaultOciCredentialsImpl.class.getName());

  public static final String DEFAULT_TENANCY_OCID_KEY = "TENANCY_OCID";
  public static final String DEFAULT_USER_OCID_KEY = "USER_OCID";
  public static final String DEFAULT_FINGERPRINT_KEY = "FINGERPRINT";
  public static final String DEFAULT_SSH_PRIVATE_KEY_KEY = "SSH_PRIVATE_KEY";
  public static final String DEFAULT_SSH_PRIVATE_KEY_PASSWORD_KEY = "SSH_PRIVATE_KEY_PASSWORD";

  private String tenancyOcidKey;
  private String userOcidKey;
  private String fingerPrintKey;
  private String sshPrivateKeyKey;
  private String sshPrivateKeyPasswordKey;

  private Supplier<String> tenancyOcid;
  private Supplier<String> userOcid;
  private Supplier<String> fingerPrint;
  private Supplier<String> sshPrivateKey;
  private Supplier<Secret> sshPrivateKeyPassword;

  @DataBoundConstructor
  public VaultOciCredentialsImpl(CredentialsScope scope, String id, String description
      , Supplier<String> tenancyOcid, Supplier<String> userOcid, Supplier<String> fingerPrint, Supplier<String> sshPrivateKey,
      Supplier<Secret> sshPrivateKeyPassword) {
    super(scope, id, description);
    this.tenancyOcid = tenancyOcid;
    this.userOcid = userOcid;
    this.fingerPrint = fingerPrint;
    this.sshPrivateKey = sshPrivateKey;
    this.sshPrivateKeyPassword = sshPrivateKeyPassword;
  }

  @NonNull
  public String getTenancyOcidKey() {
    return tenancyOcidKey;
  }

  @DataBoundSetter
  public void setTenancyOcidKey(String tenancyOcidKey) {
    this.tenancyOcidKey = defaultIfBlank(tenancyOcidKey, DEFAULT_TENANCY_OCID_KEY);
  }

  @NonNull
  public String getUserOcidKey() {
    return userOcidKey;
  }

  @DataBoundSetter
  public void setUserOcidKey(String userOcidKey) {
    this.userOcidKey = defaultIfBlank(userOcidKey, DEFAULT_USER_OCID_KEY);
  }

  @NonNull
  public String getFingerPrintKey() {
    return fingerPrintKey;
  }

  @DataBoundSetter
  public void setFingerPrintKey(String fingerPrintKey) {
    this.fingerPrintKey = defaultIfBlank(fingerPrintKey, DEFAULT_FINGERPRINT_KEY);
  }

  @NonNull
  public String getSshPrivateKeyKey() {
    return sshPrivateKeyKey;
  }

  @DataBoundSetter
  public void setSshPrivateKeyKey(String sshPrivateKeyKey) {
    this.sshPrivateKeyKey = defaultIfBlank(sshPrivateKeyKey, DEFAULT_SSH_PRIVATE_KEY_KEY);
  }

  @NonNull
  public String getSshPrivateKeyPasswordKey() {
    return sshPrivateKeyPasswordKey;
  }

  @DataBoundSetter
  public void setSshPrivateKeyPasswordKey(String sshPrivateKeyPasswordKey) {
    this.sshPrivateKeyPasswordKey = defaultIfBlank(sshPrivateKeyPasswordKey, DEFAULT_SSH_PRIVATE_KEY_PASSWORD_KEY);
  }

  @NonNull
  @Override
  public String getTenancyOcid() {
    if (tenancyOcid == null) {
      LOGGER.log(Level.WARNING, "tenancyOcid was null");
      return Secret.toString(Secret.fromString(getVaultSecretKeyValue(tenancyOcidKey)));
    }
    return tenancyOcid.get();
  }

  @NonNull
  @Override
  public String getUserOcid() {
    if (userOcid == null) {
      LOGGER.log(Level.WARNING, "userOcid was null");
      return Secret.toString(Secret.fromString(getVaultSecretKeyValue(userOcidKey)));
    }
    return userOcid.get();
  }

  @NonNull
  @Override
  public String getFingerPrint() {
    if (fingerPrint == null) {
      LOGGER.log(Level.WARNING, "fingerPrint was null");
      return Secret.toString(Secret.fromString(getVaultSecretKeyValue(fingerPrintKey)));
    }
    return fingerPrint.get();
  }

  @NonNull
  @Override
  public String getUsername() {
    if (sshPrivateKey == null) {
      LOGGER.log(Level.WARNING, "sshPrivateKey was null");
      return Secret.toString(Secret.fromString(getVaultSecretKeyValue(sshPrivateKeyKey)));
    }
    return sshPrivateKey.get();
  }

  @NonNull
  @Override
  public Secret getPassword() {
    if (sshPrivateKeyPassword == null) {
      LOGGER.log(Level.WARNING, "sshPrivateKeyPassword was null");
      return Secret.fromString(getVaultSecretKeyValue(sshPrivateKeyPasswordKey));
    }
    return sshPrivateKeyPassword.get();
  }

  @Extension
  public static class DescriptorImpl extends BaseStandardCredentialsDescriptor {

    @NonNull
    @Override
    public String getDisplayName() {
      return "Vault OCI Credentials";
    }

    public FormValidation doTestConnection(@AncestorInPath ItemGroup<Item> context,
        @QueryParameter("path") String path,
        @QueryParameter("tenancyOcidKey") String tenancyOcidKey,
        @QueryParameter("userOcidKey") String userOcidKey,
        @QueryParameter("fingerPrintKey") String fingerPrintKey,
        @QueryParameter("sshPrivateKeyKey") String sshPrivateKeyKey,
        @QueryParameter("sshPrivateKeyPasswordKey") String sshPrivateKeyPasswordKey,
        @QueryParameter("prefixPath") String prefixPath,
        @QueryParameter("namespace") String namespace,
        @QueryParameter("engineVersion") Integer engineVersion) {

      String clientId = null;
      try {
        clientId = getVaultSecretKey(path, defaultIfBlank(tenancyOcidKey, DEFAULT_TENANCY_OCID_KEY), prefixPath, namespace,
            engineVersion);
      } catch (Exception e) {
        return FormValidation.error("FAILED to retrieve tenancyOcid key: \n" + e);
      }

      try {
        getVaultSecretKey(path, defaultIfBlank(userOcidKey, DEFAULT_USER_OCID_KEY), prefixPath, namespace, engineVersion);
      } catch (Exception e) {
        return FormValidation.error("FAILED to retrieve userOcid key: \n" + e);
      }

      try {
        getVaultSecretKey(path, defaultIfBlank(fingerPrintKey, DEFAULT_FINGERPRINT_KEY), prefixPath, namespace, engineVersion);
      } catch (Exception e) {
        return FormValidation.error("FAILED to retrieve fingerPrint key: \n" + e);
      }

      try {
        getVaultSecretKey(path, defaultIfBlank(sshPrivateKeyKey, DEFAULT_SSH_PRIVATE_KEY_KEY), prefixPath, namespace,
            engineVersion);
      } catch (Exception e) {
        return FormValidation.error("FAILED to retrieve sshPrivateKey key: \n" + e);
      }

      try {
        getVaultSecretKey(path, defaultIfBlank(sshPrivateKeyPasswordKey, DEFAULT_SSH_PRIVATE_KEY_PASSWORD_KEY), prefixPath,
            namespace, engineVersion);
      } catch (Exception e) {
        return FormValidation.error("FAILED to retrieve sshPrivateKeyPasswordKey key: \n" + e);
      }

      return FormValidation.ok("Successfully retrieved username " + clientId + " and other variables.");
    }


    @SuppressWarnings("unused") // used by stapler
    public ListBoxModel doFillEngineVersionItems(@AncestorInPath Item context) {
      return engineVersions(context);
    }

  }
}
