package com.datapipe.jenkins.vault.credentials.common;

import com.cloudbees.plugins.credentials.CredentialsNameProvider;
import com.cloudbees.plugins.credentials.NameWith;
import edu.umd.cs.findbugs.annotations.NonNull;

@NameWith(AuthUrlRealmCredentials.NameProvider.class)
public interface AuthUrlRealmCredentials extends AuthUrlCredentials, RealmCredentials {


  public static class NameProvider extends CredentialsNameProvider<AuthUrlRealmCredentials> {
    public NameProvider() {
    }

    @NonNull
    public String getName(@NonNull AuthUrlRealmCredentials credentials) {
      return credentials.getAuthUrl() + credentials.getRealm();
    }
  }
}
