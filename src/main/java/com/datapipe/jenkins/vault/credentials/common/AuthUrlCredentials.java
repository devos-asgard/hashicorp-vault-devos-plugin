package com.datapipe.jenkins.vault.credentials.common;

import com.cloudbees.plugins.credentials.Credentials;
import com.cloudbees.plugins.credentials.CredentialsNameProvider;
import com.cloudbees.plugins.credentials.NameWith;
import edu.umd.cs.findbugs.annotations.NonNull;

@NameWith(AuthUrlCredentials.NameProvider.class)
public interface AuthUrlCredentials extends Credentials {
  @NonNull
  String getAuthUrl();

  public static class NameProvider extends CredentialsNameProvider<AuthUrlCredentials> {
    public NameProvider() {
    }

    @NonNull
    public String getName(@NonNull AuthUrlCredentials credentials) {
      return credentials.getAuthUrl();
    }
  }
}
