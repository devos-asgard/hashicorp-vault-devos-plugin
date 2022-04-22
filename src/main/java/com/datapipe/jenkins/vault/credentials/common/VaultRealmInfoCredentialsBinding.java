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

public class VaultRealmInfoCredentialsBinding extends MultiBinding<VaultRealmInfoCredentials> {

  public static final String DEFAULT_RI_CLIENT = "RI_CLIENT";
  public static final String DEFAULT_RI_CLIENT_SECRET = "RI_CLIENT_SECRET";
  public static final String DEFAULT_RI_AUTH_URL = "RI_AUTH_URL";
  public static final String DEFAULT_RI_REALM = "RI_REALM";

  private String clientVariable;
  private String clientSecretVariable;
  private String authUrlVariable;
  private String realmVariable;

  @DataBoundConstructor
  public VaultRealmInfoCredentialsBinding(@Nullable String clientVariable,
      @Nullable String clientSecretVariable,
      @Nullable String authUrlVariable,
      @Nullable String realmVariable,
      String credentialsId) {
    super(credentialsId);
    this.clientVariable = defaultIfBlank(clientVariable, DEFAULT_RI_CLIENT);
    this.clientSecretVariable = defaultIfBlank(clientSecretVariable, DEFAULT_RI_CLIENT_SECRET);
    this.authUrlVariable = defaultIfBlank(authUrlVariable, DEFAULT_RI_AUTH_URL);
    this.realmVariable = defaultIfBlank(realmVariable, DEFAULT_RI_REALM);
  }


  @Override
  protected Class<VaultRealmInfoCredentials> type() {
    return VaultRealmInfoCredentials.class;
  }

  @Override
  public MultiEnvironment bind(@Nonnull Run<?, ?> build, @Nullable FilePath workspace, @Nullable Launcher launcher,
      @Nonnull TaskListener listener) throws IOException, InterruptedException {
    VaultRealmInfoCredentials credentials = this.getCredentials(build);
    Map<String, String> map = new HashMap<>();
    map.put(this.clientVariable, credentials.getUsername());
    map.put(this.clientSecretVariable, credentials.getPassword().getPlainText());
    map.put(this.authUrlVariable, credentials.getAuthUrl());
    map.put(this.realmVariable, credentials.getRealm());
    return new MultiEnvironment(map);
  }

  public String getClientIdVariable() {
    return clientVariable;
  }

  public String getClientSecretVariable() {
    return clientSecretVariable;
  }

  public String getAuthUrlVariable() {
    return authUrlVariable;
  }

  public String getRealmVariable() {
    return realmVariable;
  }

    @Override
    public Set<String> variables() {
        Set<String> variables = new HashSet<>();
        variables.add(this.clientVariable);
        variables.add(this.clientSecretVariable);
        variables.add(this.authUrlVariable);
        variables.add(this.realmVariable);
        return variables;
    }

  @Extension
  public static class DescriptorImpl extends BindingDescriptor<VaultRealmInfoCredentials> {

    @Override
    protected Class<VaultRealmInfoCredentials> type() {
      return VaultRealmInfoCredentials.class;
    }

    @NonNull
    @Override
    public String getDisplayName() {
      return "Vault Realm Information Credentials";
    }

    @SuppressWarnings("unused") // used by stapler
    public ListBoxModel doFillEngineVersionItems(@AncestorInPath Item context) {
      return engineVersions(context);
    }

  }

}
