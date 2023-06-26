package com.datapipe.jenkins.vault.credentials.common;

import com.cloudbees.plugins.credentials.CredentialsNameProvider;
import com.cloudbees.plugins.credentials.NameWith;
import com.cloudbees.plugins.credentials.common.StandardCredentials;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Util;
import io.snyk.jenkins.credentials.SnykApiToken;

@NameWith(value = SnykApiToken.NameProvider.class, priority = 32)
public interface VaultSnykCredentials extends SnykApiToken, StandardCredentials {

    String getDisplayName();

    class NameProvider extends CredentialsNameProvider<VaultSnykCredentials> {

        @NonNull
        @Override
        public String getName(VaultSnykCredentials hashicorpVaultCredentials) {
            String description = Util.fixEmpty(hashicorpVaultCredentials.getDescription());
            return hashicorpVaultCredentials.getDisplayName() + (description == null ? ""
                : " (" + description + ")");
        }
    }
}
