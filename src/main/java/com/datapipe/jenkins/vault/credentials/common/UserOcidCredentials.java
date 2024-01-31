package com.datapipe.jenkins.vault.credentials.common;

import com.cloudbees.plugins.credentials.Credentials;
import com.cloudbees.plugins.credentials.CredentialsNameProvider;
import com.cloudbees.plugins.credentials.NameWith;
import edu.umd.cs.findbugs.annotations.NonNull;

@NameWith(UserOcidCredentials.NameProvider.class)
public interface UserOcidCredentials extends Credentials {

  @NonNull
  String getUserOcid();

  public static class NameProvider extends CredentialsNameProvider<UserOcidCredentials> {

    public NameProvider() {
    }

    @NonNull
    public String getName(@NonNull UserOcidCredentials credentials) {
      return credentials.getUserOcid();
    }
  }
}
