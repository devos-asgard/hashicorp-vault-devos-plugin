package com.datapipe.jenkins.vault.credentials.common;

import com.cloudbees.plugins.credentials.Credentials;
import com.cloudbees.plugins.credentials.CredentialsNameProvider;
import com.cloudbees.plugins.credentials.NameWith;
import edu.umd.cs.findbugs.annotations.NonNull;

@NameWith(TenantCredentials.NameProvider.class)
public interface TenantCredentials extends Credentials {
  @NonNull
  String getTenant();

  public static class NameProvider extends CredentialsNameProvider<TenantCredentials> {
    public NameProvider() {
    }

    @NonNull
    public String getName(@NonNull TenantCredentials credentials) {
      return credentials.getTenant();
    }
  }
}
