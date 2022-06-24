package com.datapipe.jenkins.vault.credentials.snapshots;

import com.cloudbees.plugins.credentials.CredentialsSnapshotTaker;
import com.datapipe.jenkins.vault.credentials.Snapshot;
import com.datapipe.jenkins.vault.credentials.common.VaultFileCredentialImpl;
import hudson.Extension;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;

@Extension
public class VaultFileCredentialsSnapshotTaker extends
    CredentialsSnapshotTaker<VaultFileCredentialImpl> {

    @Override
    public Class<VaultFileCredentialImpl> type() {
        return VaultFileCredentialImpl.class;
    }

    @Override
    public VaultFileCredentialImpl snapshot(
        VaultFileCredentialImpl credential) {
        Snapshot<String> contents = null;
        try {
            contents = new Snapshot<>(
                IOUtils.toString(credential.getContent(), String.valueOf(StandardCharsets.UTF_8)));

        } catch (IOException e) {
            throw new IllegalStateException("Can not convert stream to string", e);
        }

        return new VaultFileCredentialImpl(credential.getScope(), credential.getId(),
            credential.getDescription(), contents);
    }
}
