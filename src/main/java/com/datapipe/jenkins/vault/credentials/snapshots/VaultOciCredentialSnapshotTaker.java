package com.datapipe.jenkins.vault.credentials.snapshots;

import com.cloudbees.plugins.credentials.CredentialsSnapshotTaker;
import com.datapipe.jenkins.vault.credentials.SecretSnapshot;
import com.datapipe.jenkins.vault.credentials.Snapshot;
import com.datapipe.jenkins.vault.credentials.common.VaultOciCredentialsImpl;
import hudson.Extension;

@Extension
public class VaultOciCredentialSnapshotTaker extends
    CredentialsSnapshotTaker<VaultOciCredentialsImpl> {

    @Override
    public Class<VaultOciCredentialsImpl> type() {
        return VaultOciCredentialsImpl.class;
    }

    @Override
    public VaultOciCredentialsImpl snapshot(
        VaultOciCredentialsImpl credential) {
        Snapshot<String> tenancyOcid = new Snapshot<>(credential.getTenancyOcid());
        Snapshot<String> userOcid = new Snapshot<>(credential.getUserOcid());
        Snapshot<String> fingerPrint = new Snapshot<>(credential.getFingerPrint());
        Snapshot<String> sshPrivateKey = new Snapshot<>(credential.getUsername());
        SecretSnapshot sshPrivateKeyPassword = new SecretSnapshot(credential.getPassword());

        return new VaultOciCredentialsImpl(credential.getScope(), credential.getId(),
            credential.getDescription(), tenancyOcid, userOcid, fingerPrint, sshPrivateKey, sshPrivateKeyPassword);
    }
}
