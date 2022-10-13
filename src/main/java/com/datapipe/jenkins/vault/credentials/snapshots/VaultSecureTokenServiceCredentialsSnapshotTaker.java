package com.datapipe.jenkins.vault.credentials.snapshots;

import com.cloudbees.plugins.credentials.CredentialsSnapshotTaker;
import com.datapipe.jenkins.vault.credentials.SecretSnapshot;
import com.datapipe.jenkins.vault.credentials.Snapshot;
import com.datapipe.jenkins.vault.credentials.common.VaultSecureTokenServiceCredentialsImpl;
import hudson.Extension;

@Extension
public class VaultSecureTokenServiceCredentialsSnapshotTaker extends
    CredentialsSnapshotTaker<VaultSecureTokenServiceCredentialsImpl> {


    @Override
    public Class<VaultSecureTokenServiceCredentialsImpl> type() {
        return VaultSecureTokenServiceCredentialsImpl.class;
    }

    @Override
    public VaultSecureTokenServiceCredentialsImpl snapshot(
        VaultSecureTokenServiceCredentialsImpl credentials) {
        Snapshot<String> username = new Snapshot<>(credentials.getUsername());
        SecretSnapshot password = new SecretSnapshot(credentials.getPassword());
        Snapshot<String> securityToken = new Snapshot<>(credentials.getSecurityToken());

        return new VaultSecureTokenServiceCredentialsImpl(credentials.getScope(), credentials.getId(),
            credentials.getDescription(), username, password, securityToken);
    }
}
