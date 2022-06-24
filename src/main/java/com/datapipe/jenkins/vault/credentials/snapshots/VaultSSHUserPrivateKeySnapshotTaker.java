package com.datapipe.jenkins.vault.credentials.snapshots;

import com.cloudbees.plugins.credentials.CredentialsSnapshotTaker;
import com.datapipe.jenkins.vault.credentials.SecretSnapshot;
import com.datapipe.jenkins.vault.credentials.Snapshot;
import com.datapipe.jenkins.vault.credentials.common.VaultSSHUserPrivateKeyImpl;
import hudson.Extension;

@Extension
public class VaultSSHUserPrivateKeySnapshotTaker extends
    CredentialsSnapshotTaker<VaultSSHUserPrivateKeyImpl> {

    @Override
    public Class<VaultSSHUserPrivateKeyImpl> type() {
        return VaultSSHUserPrivateKeyImpl.class;
    }

    @Override
    public VaultSSHUserPrivateKeyImpl snapshot(
        VaultSSHUserPrivateKeyImpl credentials) {
        Snapshot<String> userNameSnapShot = new Snapshot<>(credentials.getUsername());
        Snapshot<String> privateKeySnapShot = new Snapshot<>(credentials.getPrivateKey());
        SecretSnapshot passPhraseSnapshot = new SecretSnapshot(credentials.getPassphrase());
        return new VaultSSHUserPrivateKeyImpl(credentials.getScope(), credentials.getId(),
            credentials.getDescription(), userNameSnapShot, privateKeySnapShot, passPhraseSnapshot);
    }
}
