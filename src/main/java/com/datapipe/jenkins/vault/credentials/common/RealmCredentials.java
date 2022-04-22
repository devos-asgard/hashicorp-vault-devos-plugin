package com.datapipe.jenkins.vault.credentials.common;

import com.cloudbees.plugins.credentials.Credentials;
import com.cloudbees.plugins.credentials.CredentialsNameProvider;
import com.cloudbees.plugins.credentials.NameWith;
import edu.umd.cs.findbugs.annotations.NonNull;

@NameWith(RealmCredentials.NameProvider.class)
public interface RealmCredentials extends Credentials {
  @NonNull
  String getRealm();

  public static class NameProvider extends CredentialsNameProvider<RealmCredentials> {
    public NameProvider() {
    }

    @NonNull
    public String getName(@NonNull RealmCredentials credentials) {
      return credentials.getRealm();
    }
  }
}
