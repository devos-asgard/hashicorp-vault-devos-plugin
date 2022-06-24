package com.datapipe.jenkins.vault.credentials.snapshots;

import com.cloudbees.plugins.credentials.CredentialsSnapshotTaker;
import com.datapipe.jenkins.vault.credentials.SecretSnapshot;
import com.datapipe.jenkins.vault.credentials.Snapshot;
import com.datapipe.jenkins.vault.credentials.common.VaultAzureServicePrincipalCredentialsImpl;
import hudson.Extension;

@Extension
public class VaultAzureServicePrincipalCredentialSnapshotTaker extends
    CredentialsSnapshotTaker<VaultAzureServicePrincipalCredentialsImpl> {

    @Override
    public Class<VaultAzureServicePrincipalCredentialsImpl> type() {
        return VaultAzureServicePrincipalCredentialsImpl.class;
    }

    @Override
    public VaultAzureServicePrincipalCredentialsImpl snapshot(
        VaultAzureServicePrincipalCredentialsImpl credential) {
        Snapshot<String> clientId = new Snapshot<>(credential.getUsername());
        SecretSnapshot clientSecret = new SecretSnapshot(credential.getPassword());
        Snapshot<String> subscriptionId = new Snapshot<>(credential.getSubscription());
        Snapshot<String> tenantId = new Snapshot<>(credential.getTenant());

        return new VaultAzureServicePrincipalCredentialsImpl(credential.getScope(), credential.getId(),
            credential.getDescription(), clientId, clientSecret, subscriptionId, tenantId);
    }
}
