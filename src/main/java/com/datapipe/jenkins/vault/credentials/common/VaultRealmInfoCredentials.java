package com.datapipe.jenkins.vault.credentials.common;

import com.cloudbees.plugins.credentials.CredentialsNameProvider;
import com.cloudbees.plugins.credentials.NameWith;
import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Util;

@NameWith(value = VaultRealmInfoCredentials.NameProvider.class, priority = 32)
public interface VaultRealmInfoCredentials extends StandardUsernamePasswordCredentials,
    AuthUrlRealmCredentials {

  String getDisplayName();

  class NameProvider extends CredentialsNameProvider<VaultRealmInfoCredentials> {

    @NonNull
    @Override
    public String getName(VaultRealmInfoCredentials hashicorpVaultCredentials) {
      String description = Util.fixEmpty(hashicorpVaultCredentials.getDescription());
      return hashicorpVaultCredentials.getDisplayName() + (description == null ? ""
          : " (" + description + ")");
    }
  }
}
