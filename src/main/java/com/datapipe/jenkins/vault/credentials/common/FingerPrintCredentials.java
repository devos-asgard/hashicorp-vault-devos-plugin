package com.datapipe.jenkins.vault.credentials.common;

import com.cloudbees.plugins.credentials.Credentials;
import com.cloudbees.plugins.credentials.CredentialsNameProvider;
import com.cloudbees.plugins.credentials.NameWith;
import edu.umd.cs.findbugs.annotations.NonNull;

@NameWith(FingerPrintCredentials.NameProvider.class)
public interface FingerPrintCredentials extends Credentials {

  @NonNull
  String getFingerPrint();

  public static class NameProvider extends CredentialsNameProvider<FingerPrintCredentials> {

    public NameProvider() {
    }

    @NonNull
    public String getName(@NonNull FingerPrintCredentials credentials) {
      return credentials.getFingerPrint();
    }
  }
}
