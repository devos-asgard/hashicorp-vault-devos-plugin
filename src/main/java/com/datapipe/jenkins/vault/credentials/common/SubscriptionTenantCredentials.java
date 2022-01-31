package com.datapipe.jenkins.vault.credentials.common;

import com.cloudbees.plugins.credentials.CredentialsNameProvider;
import com.cloudbees.plugins.credentials.NameWith;
import edu.umd.cs.findbugs.annotations.NonNull;

@NameWith(SubscriptionTenantCredentials.NameProvider.class)
public interface SubscriptionTenantCredentials extends SubscriptionCredentials, TenantCredentials {


  public static class NameProvider extends CredentialsNameProvider<SubscriptionTenantCredentials> {
    public NameProvider() {
    }

    @NonNull
    public String getName(@NonNull SubscriptionTenantCredentials credentials) {
      return credentials.getSubscription() + credentials.getTenant();
    }
  }
}
