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

public class VaultAzureServicePrincipalCredentialsBinding extends MultiBinding<VaultAzureServicePrincipalCredentials> {

  public static final String DEFAULT_ARM_CLIENT_ID = "ARM_CLIENT_ID";
  public static final String DEFAULT_ARM_CLIENT_SECRET = "ARM_CLIENT_SECRET";
  public static final String DEFAULT_ARM_SUBSCRIPTION_ID = "ARM_SUBSCRIPTION_ID";
  public static final String DEFAULT_ARM_TENANT_ID = "ARM_TENANT_ID";
  private String clientIdVariable;
  private String clientSecretVariable;
  private String subscriptionIdVariable;
  private String tenantIdVariable;

  @DataBoundConstructor
  public VaultAzureServicePrincipalCredentialsBinding(@Nullable String clientIdVariable,
      @Nullable String clientSecretVariable,
      @Nullable String subscriptionIdVariable,
      @Nullable String tenantIdVariable,
      String credentialsId) {
    super(credentialsId);
    this.clientIdVariable = defaultIfBlank(clientIdVariable, DEFAULT_ARM_CLIENT_ID);
    this.clientSecretVariable = defaultIfBlank(clientSecretVariable, DEFAULT_ARM_CLIENT_SECRET);
    this.subscriptionIdVariable = defaultIfBlank(subscriptionIdVariable, DEFAULT_ARM_SUBSCRIPTION_ID);
    this.tenantIdVariable = defaultIfBlank(tenantIdVariable, DEFAULT_ARM_TENANT_ID);
  }


  @Override
  protected Class<VaultAzureServicePrincipalCredentials> type() {
    return VaultAzureServicePrincipalCredentials.class;
  }

  @Override
  public MultiEnvironment bind(@Nonnull Run<?, ?> build, @Nullable FilePath workspace, @Nullable Launcher launcher,
      @Nonnull TaskListener listener) throws IOException, InterruptedException {
    VaultAzureServicePrincipalCredentials credentials = this.getCredentials(build);
    Map<String, String> map = new HashMap<>();
    map.put(this.clientIdVariable, credentials.getUsername());
    map.put(this.clientSecretVariable, credentials.getPassword().getPlainText());
    map.put(this.subscriptionIdVariable, credentials.getSubscription());
    map.put(this.tenantIdVariable, credentials.getTenant());
    return new MultiEnvironment(map);
  }

  public String getClientIdVariable() {
    return clientIdVariable;
  }

  public String getClientSecretVariable() {
    return clientSecretVariable;
  }

  public String getSubscriptionIdVariable() {
    return subscriptionIdVariable;
  }

  public String getTenantIdVariable() {
    return tenantIdVariable;
  }

    @Override
    public Set<String> variables() {
        Set<String> variables = new HashSet<>();
        variables.add(this.clientIdVariable);
        variables.add(this.clientSecretVariable);
        variables.add(this.subscriptionIdVariable);
        variables.add(this.tenantIdVariable);
        return variables;
    }

  @Extension
  public static class DescriptorImpl extends BindingDescriptor<VaultAzureServicePrincipalCredentials> {

    @Override
    protected Class<VaultAzureServicePrincipalCredentials> type() {
      return VaultAzureServicePrincipalCredentials.class;
    }

    @NonNull
    @Override
    public String getDisplayName() {
      return "Vault Azure Service Principal Credentials";
    }

    @SuppressWarnings("unused") // used by stapler
    public ListBoxModel doFillEngineVersionItems(@AncestorInPath Item context) {
      return engineVersions(context);
    }

  }

}
