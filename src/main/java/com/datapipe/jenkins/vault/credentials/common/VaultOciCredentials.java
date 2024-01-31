package com.datapipe.jenkins.vault.credentials.common;

import com.cloudbees.plugins.credentials.CredentialsNameProvider;
import com.cloudbees.plugins.credentials.NameWith;
import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Util;

@NameWith(value = VaultOciCredentials.NameProvider.class, priority = 32)
public interface VaultOciCredentials extends StandardUsernamePasswordCredentials,
    OciCredentials {

  String getDisplayName();
  public static class NameProvider extends CredentialsNameProvider<VaultOciCredentials> {

    @NonNull
    @Override
    public String getName(VaultOciCredentials hashicorpVaultCredentials) {
      String description = Util.fixEmpty(hashicorpVaultCredentials.getDescription());
      return hashicorpVaultCredentials.getDisplayName() + (description == null ? ""
          : " (" + description + ")");
    }
  }
}
