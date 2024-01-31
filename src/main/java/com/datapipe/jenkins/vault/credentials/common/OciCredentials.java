package com.datapipe.jenkins.vault.credentials.common;

import com.cloudbees.plugins.credentials.CredentialsNameProvider;
import com.cloudbees.plugins.credentials.NameWith;
import edu.umd.cs.findbugs.annotations.NonNull;

@NameWith(OciCredentials.NameProvider.class)
public interface OciCredentials extends TenancyOcidCredentials, UserOcidCredentials, FingerPrintCredentials {

  public static class NameProvider extends CredentialsNameProvider<OciCredentials> {

    public NameProvider() {
    }

    @NonNull
    public String getName(@NonNull OciCredentials credentials) {
      return credentials.getTenancyOcid() + credentials.getUserOcid() + credentials.getFingerPrint();
    }
  }
}
