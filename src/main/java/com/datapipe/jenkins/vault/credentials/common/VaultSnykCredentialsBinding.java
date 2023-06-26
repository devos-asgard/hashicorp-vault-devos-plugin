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

public class VaultSnykCredentialsBinding extends MultiBinding<VaultSnykCredentials> {

  public static final String DEFAULT_TOKEN_VARIABLE = "TOKEN";

  private String tokenVariable;

  @DataBoundConstructor
  public VaultSnykCredentialsBinding(@Nullable String tokenVariable, String credentialsId) {
    super(credentialsId);
    this.tokenVariable = defaultIfBlank(tokenVariable, DEFAULT_TOKEN_VARIABLE);
  }


  @Override
  protected Class<VaultSnykCredentials> type() {
    return VaultSnykCredentials.class;
  }

  @Override
  public MultiEnvironment bind(@Nonnull Run<?, ?> build, @Nullable FilePath workspace, @Nullable Launcher launcher,
      @Nonnull TaskListener listener) throws IOException, InterruptedException {
      VaultSnykCredentials credentials = this.getCredentials(build);
    Map<String, String> map = new HashMap<>();
    map.put(this.tokenVariable, credentials.getToken().getPlainText());
    return new MultiEnvironment(map);
  }

  public String getTokenVariable() {
    return tokenVariable;
  }

    @Override
    public Set<String> variables() {
        Set<String> variables = new HashSet<>();
        variables.add(this.tokenVariable);
        return variables;
    }

  @Extension
  public static class DescriptorImpl extends BindingDescriptor<VaultSnykCredentials> {

    @Override
    protected Class<VaultSnykCredentials> type() {
      return VaultSnykCredentials.class;
    }

    @NonNull
    @Override
    public String getDisplayName() {
      return "Vault Snyk Credentials";
    }

    @SuppressWarnings("unused") // used by stapler
    public ListBoxModel doFillEngineVersionItems(@AncestorInPath Item context) {
      return engineVersions(context);
    }

  }

}
