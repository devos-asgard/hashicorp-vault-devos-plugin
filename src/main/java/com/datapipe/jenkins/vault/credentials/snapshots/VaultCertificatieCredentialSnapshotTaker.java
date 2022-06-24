package com.datapipe.jenkins.vault.credentials.snapshots;

import com.cloudbees.plugins.credentials.CredentialsSnapshotTaker;
import com.datapipe.jenkins.vault.credentials.SecretSnapshot;
import com.datapipe.jenkins.vault.credentials.common.VaultCertificateCredentialsImpl;
import hudson.Extension;

@Extension
public class VaultCertificatieCredentialSnapshotTaker extends
    CredentialsSnapshotTaker<VaultCertificateCredentialsImpl> {

    @Override
    public Class<VaultCertificateCredentialsImpl> type() {
        return VaultCertificateCredentialsImpl.class;
    }

    @Override
    public VaultCertificateCredentialsImpl snapshot(
        VaultCertificateCredentialsImpl credential) {
        SecretSnapshot keystore = new SecretSnapshot(credential.getKeyStoreBase64());
        SecretSnapshot base64 = new SecretSnapshot(credential.getPassword());

        return new VaultCertificateCredentialsImpl(credential.getScope(), credential.getId(),
            credential.getDescription(), keystore, base64);
    }
}
