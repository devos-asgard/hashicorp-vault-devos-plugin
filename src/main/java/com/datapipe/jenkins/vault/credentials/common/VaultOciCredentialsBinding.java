package com.datapipe.jenkins.vault.credentials.common;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Item;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.util.ListBoxModel;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jenkinsci.plugins.credentialsbinding.BindingDescriptor;
import org.jenkinsci.plugins.credentialsbinding.MultiBinding;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;

import static com.datapipe.jenkins.vault.configuration.VaultConfiguration.engineVersions;
import static org.apache.commons.lang.StringUtils.defaultIfBlank;

public class VaultOciCredentialsBinding extends MultiBinding<VaultOciCredentials> {

  public static final String DEFAULT_TENANCY_OCID_VARIABLE = "TENANCY_OCID";
  public static final String DEFAULT_USER_OCID_VARIABLE = "USER_OCID";
  public static final String DEFAULT_FINGERPRINT_VARIABLE = "FINGERPRINT";
  public static final String DEFAULT_SSH_PRIVATE_KEY_VARIABLE = "SSH_PRIVATE_KEY";
  public static final String DEFAULT_SSH_PRIVATE_KEY_PASSWORD_VARIABLE = "SSH_PRIVATE_KEY_PASSWORD";
  private String tenancyOcidVariable;
  private String userOcidVariable;
  private String fingerPrintVariable;
  private String sshPrivateKeyVariable;
  private String sshPrivateKeyPasswordVariable;

  @DataBoundConstructor
  public VaultOciCredentialsBinding(@Nullable String tenancyOcidVariable,
      @Nullable String userOcidVariable,
      @Nullable String fingerPrintVariable,
      @Nullable String sshPrivateKeyVariable,
      @Nullable String sshPrivateKeyPasswordVariable,
      String credentialsId) {
    super(credentialsId);
    this.tenancyOcidVariable = defaultIfBlank(tenancyOcidVariable, DEFAULT_TENANCY_OCID_VARIABLE);
    this.userOcidVariable = defaultIfBlank(userOcidVariable, DEFAULT_USER_OCID_VARIABLE);
    this.fingerPrintVariable = defaultIfBlank(fingerPrintVariable, DEFAULT_FINGERPRINT_VARIABLE);
    this.sshPrivateKeyVariable = defaultIfBlank(sshPrivateKeyVariable, DEFAULT_SSH_PRIVATE_KEY_VARIABLE);
    this.sshPrivateKeyPasswordVariable = defaultIfBlank(sshPrivateKeyPasswordVariable,
        DEFAULT_SSH_PRIVATE_KEY_PASSWORD_VARIABLE);
  }

  @Override
  protected Class<VaultOciCredentials> type() {
    return VaultOciCredentials.class;
  }

  @Override
  public MultiEnvironment bind(@Nonnull Run<?, ?> build, @Nullable FilePath workspace, @Nullable Launcher launcher,
      @Nonnull TaskListener listener) throws IOException, InterruptedException {
    VaultOciCredentials credentials = this.getCredentials(build);
    Map<String, String> map = new HashMap<>();
    map.put(this.tenancyOcidVariable, credentials.getTenancyOcid());
    map.put(this.userOcidVariable, credentials.getUserOcid());
    map.put(this.fingerPrintVariable, credentials.getFingerPrint());
    map.put(this.sshPrivateKeyVariable, credentials.getUsername());
    map.put(this.sshPrivateKeyPasswordVariable, credentials.getPassword().getPlainText());
    return new MultiEnvironment(map);
  }

  public String getTenancyOcidVariable() {
    return tenancyOcidVariable;
  }

  public String getUserOcidVariable() {
    return userOcidVariable;
  }

  public String getFingerPrintVariable() {
    return fingerPrintVariable;
  }

  public String getSshPrivateKeyVariable() {
    return sshPrivateKeyVariable;
  }

  public String getSshPrivateKeyPasswordVariable() {
    return sshPrivateKeyPasswordVariable;
  }

  @Override
  public Set<String> variables() {
    Set<String> variables = new HashSet<>();
    variables.add(this.tenancyOcidVariable);
    variables.add(this.userOcidVariable);
    variables.add(this.fingerPrintVariable);
    variables.add(this.sshPrivateKeyVariable);
    variables.add(this.sshPrivateKeyPasswordVariable);
    return variables;
  }

  @Extension
  public static class DescriptorImpl extends BindingDescriptor<VaultOciCredentials> {

    @Override
    protected Class<VaultOciCredentials> type() {
      return VaultOciCredentials.class;
    }

    @NonNull
    @Override
    public String getDisplayName() {
      return "Vault OCI Credentials";
    }

    @SuppressWarnings("unused") // used by stapler
    public ListBoxModel doFillEngineVersionItems(@AncestorInPath Item context) {
      return engineVersions(context);
    }

  }

}
