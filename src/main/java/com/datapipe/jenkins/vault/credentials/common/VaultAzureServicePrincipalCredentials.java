package com.datapipe.jenkins.vault.credentials.common;

import com.cloudbees.plugins.credentials.CredentialsNameProvider;
import com.cloudbees.plugins.credentials.NameWith;
import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Util;

@NameWith(value = VaultAzureServicePrincipalCredentials.NameProvider.class, priority = 32)
public interface VaultAzureServicePrincipalCredentials extends StandardUsernamePasswordCredentials,
    SubscriptionTenantCredentials {

  String getDisplayName();

  class NameProvider extends CredentialsNameProvider<VaultAzureServicePrincipalCredentials> {

    @NonNull
    @Override
    public String getName(VaultAzureServicePrincipalCredentials hashicorpVaultCredentials) {
      String description = Util.fixEmpty(hashicorpVaultCredentials.getDescription());
      return hashicorpVaultCredentials.getDisplayName() + (description == null ? ""
          : " (" + description + ")");
    }
  }
}
