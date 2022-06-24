package com.datapipe.jenkins.vault.credentials.snapshots;

import com.cloudbees.plugins.credentials.CredentialsSnapshotTaker;
import com.datapipe.jenkins.vault.credentials.SecretSnapshot;
import com.datapipe.jenkins.vault.credentials.common.VaultStringCredentialImpl;
import hudson.Extension;

@Extension
public class VaultStringCredentialSnapshotTaker extends
    CredentialsSnapshotTaker<VaultStringCredentialImpl> {

    @Override
    public Class<VaultStringCredentialImpl> type() {
        return VaultStringCredentialImpl.class;
    }

    @Override
    public VaultStringCredentialImpl snapshot(
        VaultStringCredentialImpl credentials) {
        SecretSnapshot secretSnapshot = new SecretSnapshot(credentials.getSecret());
        return new VaultStringCredentialImpl(credentials.getScope(), credentials.getId(),
            credentials.getDescription(), secretSnapshot);
    }
}
