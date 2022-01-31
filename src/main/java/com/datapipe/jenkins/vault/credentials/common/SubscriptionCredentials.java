package com.datapipe.jenkins.vault.credentials.common;

import com.cloudbees.plugins.credentials.Credentials;
import com.cloudbees.plugins.credentials.CredentialsNameProvider;
import com.cloudbees.plugins.credentials.NameWith;
import edu.umd.cs.findbugs.annotations.NonNull;

@NameWith(SubscriptionCredentials.NameProvider.class)
public interface SubscriptionCredentials extends Credentials {
  @NonNull
  String getSubscription();

  public static class NameProvider extends CredentialsNameProvider<SubscriptionCredentials> {
    public NameProvider() {
    }

    @NonNull
    public String getName(@NonNull SubscriptionCredentials credentials) {
      return credentials.getSubscription();
    }
  }
}
