package com.datapipe.jenkins.vault.credentials.common;

import com.cloudbees.plugins.credentials.Credentials;
import com.cloudbees.plugins.credentials.CredentialsNameProvider;
import com.cloudbees.plugins.credentials.NameWith;
import edu.umd.cs.findbugs.annotations.NonNull;

@NameWith(SecurityTokenCredentials.NameProvider.class)
public interface SecurityTokenCredentials extends Credentials {
  @NonNull
  String getSecurityToken();

  public static class NameProvider extends CredentialsNameProvider<SecurityTokenCredentials> {
    public NameProvider() {
    }

    @NonNull
    public String getName(@NonNull SecurityTokenCredentials credentials) {
      return credentials.getSecurityToken();
    }
  }
}
