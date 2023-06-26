package com.datapipe.jenkins.vault.credentials.common;

import com.cloudbees.plugins.credentials.CredentialsScope;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Item;
import hudson.model.ItemGroup;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import hudson.util.Secret;
import java.util.function.Supplier;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import static com.datapipe.jenkins.vault.configuration.VaultConfiguration.engineVersions;
import static com.datapipe.jenkins.vault.credentials.common.VaultHelper.getVaultSecretKey;
import static org.apache.commons.lang.StringUtils.defaultIfBlank;

public class VaultSnykCredentialsImpl extends AbstractVaultBaseStandardCredentials implements
    VaultSnykCredentials {

    public static final String DEFAULT_TOKEN_KEY = "token";


    private Supplier<Secret> token;

    private String tokenKey;

    @DataBoundConstructor
    public VaultSnykCredentialsImpl(CredentialsScope scope, String id, String description) {
        super(scope, id, description);
        this.token = null;
    }

    public VaultSnykCredentialsImpl(CredentialsScope scope, String id, String description, Supplier<Secret> token) {
        super(scope, id, description);
        this.token = token;
    }

    @NonNull
    public String getTokenKey() {
        return tokenKey;
    }

    @DataBoundSetter
    public void setTokenKey(String tokenKey) {
        this.tokenKey = defaultIfBlank(tokenKey, DEFAULT_TOKEN_KEY);
    }

    @NonNull
    @Override
    public Secret getToken() {
        if (token != null) {
            return token.get();
        }
        return Secret.fromString(getVaultSecretKeyValue(getTokenKey()));
    }

    @Extension
    public static class DescriptorImpl extends BaseStandardCredentialsDescriptor {

        @Override
        public String getDisplayName() {
            return "Vault Snyk Credential";
        }

        public FormValidation doTestConnection(
            @AncestorInPath ItemGroup<Item> context,
            @QueryParameter("path") String path,
            @QueryParameter("tokenKey") String tokenKey,
            @QueryParameter("prefixPath") String prefixPath,
            @QueryParameter("namespace") String namespace,
            @QueryParameter("engineVersion") Integer engineVersion) {

            String token;
            try {
                token = getVaultSecretKey(path, defaultIfBlank(tokenKey, DEFAULT_TOKEN_KEY), prefixPath, namespace, engineVersion);
            } catch (Exception e) {
                return FormValidation.error("FAILED to retrieve Vault Snyk token: \n" + e);
            }

            return FormValidation
                .ok("Successfully retrieved Snyk token : " + token);
        }

        @SuppressWarnings("unused") // used by stapler
        public ListBoxModel doFillEngineVersionItems(@AncestorInPath Item context) {
            return engineVersions(context);
        }

    }
}
