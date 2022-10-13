package com.datapipe.jenkins.vault.credentials.common;

import com.cloudbees.plugins.credentials.CredentialsNameProvider;
import com.cloudbees.plugins.credentials.NameWith;
import com.cloudbees.plugins.credentials.common.StandardUsernamePasswordCredentials;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Util;

@NameWith(value = VaultSecureTokenServiceCredentials.NameProvider.class, priority = 32)
public interface VaultSecureTokenServiceCredentials extends StandardUsernamePasswordCredentials,
    SecurityTokenCredentials {

    String getDisplayName();

    class NameProvider extends CredentialsNameProvider<VaultSecureTokenServiceCredentials> {

        @NonNull
        @Override
        public String getName(VaultSecureTokenServiceCredentials hashicorpVaultCredentials) {
            String description = Util.fixEmpty(hashicorpVaultCredentials.getDescription());
            return hashicorpVaultCredentials.getDisplayName() + (description == null ? ""
                : " (" + description + ")");
        }
    }

}
