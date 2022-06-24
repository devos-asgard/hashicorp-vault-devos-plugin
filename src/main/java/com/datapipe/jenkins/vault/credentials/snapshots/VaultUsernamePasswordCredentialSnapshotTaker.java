package com.datapipe.jenkins.vault.credentials.snapshots;

import com.cloudbees.plugins.credentials.CredentialsSnapshotTaker;
import com.datapipe.jenkins.vault.credentials.SecretSnapshot;
import com.datapipe.jenkins.vault.credentials.common.VaultUsernamePasswordCredentialImpl;
import hudson.Extension;

@Extension
public class VaultUsernamePasswordCredentialSnapshotTaker extends
    CredentialsSnapshotTaker<VaultUsernamePasswordCredentialImpl> {

    @Override
    public Class<VaultUsernamePasswordCredentialImpl> type() {
        return VaultUsernamePasswordCredentialImpl.class;
    }

    @Override
    public VaultUsernamePasswordCredentialImpl snapshot(
        VaultUsernamePasswordCredentialImpl credentials) {
        SecretSnapshot userNameSnapshot = new SecretSnapshot(credentials.getPassword());
        SecretSnapshot passwordSnapshot = new SecretSnapshot(credentials.getPassword());
        return new VaultUsernamePasswordCredentialImpl(credentials.getScope(), credentials.getId(),
            credentials.getDescription(), userNameSnapshot, passwordSnapshot);
    }
}
