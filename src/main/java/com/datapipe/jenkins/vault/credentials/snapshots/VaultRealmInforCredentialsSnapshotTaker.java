package com.datapipe.jenkins.vault.credentials.snapshots;

import com.cloudbees.plugins.credentials.CredentialsSnapshotTaker;
import com.datapipe.jenkins.vault.credentials.SecretSnapshot;
import com.datapipe.jenkins.vault.credentials.Snapshot;
import com.datapipe.jenkins.vault.credentials.common.VaultRealmInfoCredentialsImpl;
import hudson.Extension;

@Extension
public class VaultRealmInforCredentialsSnapshotTaker extends
    CredentialsSnapshotTaker<VaultRealmInfoCredentialsImpl> {


    @Override
    public Class<VaultRealmInfoCredentialsImpl> type() {
        return VaultRealmInfoCredentialsImpl.class;
    }

    @Override
    public VaultRealmInfoCredentialsImpl snapshot(
        VaultRealmInfoCredentialsImpl credentials) {
        Snapshot<String> client = new Snapshot<>(credentials.getUsername());
        SecretSnapshot clientSecret = new SecretSnapshot(credentials.getPassword());
        Snapshot<String>authUrl = new Snapshot<>(credentials.getAuthUrl());
        Snapshot<String> realm = new Snapshot<>(credentials.getRealm());

        return new VaultRealmInfoCredentialsImpl(credentials.getScope(), credentials.getId(),
            credentials.getDescription(), client, clientSecret, authUrl, realm);
    }
}
