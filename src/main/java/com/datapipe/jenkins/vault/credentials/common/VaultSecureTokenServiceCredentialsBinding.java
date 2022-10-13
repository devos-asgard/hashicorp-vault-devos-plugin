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

public class VaultSecureTokenServiceCredentialsBinding extends MultiBinding<VaultSecureTokenServiceCredentials> {

  public static final String DEFAULT_USERNAME_VARIABLE = "USERNAME";
  public static final String DEFAULT_PASSWORD_VARIABLE = "PASSWORD";
  public static final String DEFAULT_SECURITY_TOKEN_VARIABLE = "SECURITY_TOKEN";

  private String usernameVariable;
  private String passwordVariable;
  private String securityTokenVariable;

  @DataBoundConstructor
  public VaultSecureTokenServiceCredentialsBinding(@Nullable String usernameVariable,
      @Nullable String passwordVariable,
      @Nullable String securityTokenVariable,
      String credentialsId) {
    super(credentialsId);
    this.usernameVariable = defaultIfBlank(usernameVariable, DEFAULT_USERNAME_VARIABLE);
    this.passwordVariable = defaultIfBlank(passwordVariable, DEFAULT_PASSWORD_VARIABLE);
    this.securityTokenVariable = defaultIfBlank(securityTokenVariable, DEFAULT_SECURITY_TOKEN_VARIABLE);
  }


  @Override
  protected Class<VaultSecureTokenServiceCredentials> type() {
    return VaultSecureTokenServiceCredentials.class;
  }

  @Override
  public MultiEnvironment bind(@Nonnull Run<?, ?> build, @Nullable FilePath workspace, @Nullable Launcher launcher,
      @Nonnull TaskListener listener) throws IOException, InterruptedException {
    VaultSecureTokenServiceCredentials credentials = this.getCredentials(build);
    Map<String, String> map = new HashMap<>();
    map.put(this.usernameVariable, credentials.getUsername());
    map.put(this.passwordVariable, credentials.getPassword().getPlainText());
    map.put(this.securityTokenVariable, credentials.getSecurityToken());
    return new MultiEnvironment(map);
  }

  public String getUsernameVariable() {
    return usernameVariable;
  }

  public String getPasswordVariable() {
    return passwordVariable;
  }

  public String getSecurityTokenVariable() {
    return securityTokenVariable;
  }

    @Override
    public Set<String> variables() {
        Set<String> variables = new HashSet<>();
        variables.add(this.usernameVariable);
        variables.add(this.passwordVariable);
        variables.add(this.securityTokenVariable);
        return variables;
    }

  @Extension
  public static class DescriptorImpl extends BindingDescriptor<VaultSecureTokenServiceCredentials> {

    @Override
    protected Class<VaultSecureTokenServiceCredentials> type() {
      return VaultSecureTokenServiceCredentials.class;
    }

    @NonNull
    @Override
    public String getDisplayName() {
      return "Vault Secure Token Service Credentials";
    }

    @SuppressWarnings("unused") // used by stapler
    public ListBoxModel doFillEngineVersionItems(@AncestorInPath Item context) {
      return engineVersions(context);
    }

  }

}
