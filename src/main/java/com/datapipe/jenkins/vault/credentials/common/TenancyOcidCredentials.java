package com.datapipe.jenkins.vault.credentials.common;

import com.cloudbees.plugins.credentials.Credentials;
import com.cloudbees.plugins.credentials.CredentialsNameProvider;
import com.cloudbees.plugins.credentials.NameWith;
import edu.umd.cs.findbugs.annotations.NonNull;

@NameWith(TenancyOcidCredentials.NameProvider.class)
public interface TenancyOcidCredentials extends Credentials {

  @NonNull
  String getTenancyOcid();

  public static class NameProvider extends CredentialsNameProvider<TenancyOcidCredentials> {

    public NameProvider() {
    }

    @NonNull
    public String getName(@NonNull TenancyOcidCredentials credentials) {
      return credentials.getTenancyOcid();
    }
  }
}
