package com.datapipe.jenkins.vault.credentials.common;

import com.cloudbees.plugins.credentials.CredentialsScope;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.Item;
import hudson.model.ItemGroup;
import hudson.util.FormValidation;
import hudson.util.ListBoxModel;
import hudson.util.Secret;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;
import org.kohsuke.stapler.AncestorInPath;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;

import static com.datapipe.jenkins.vault.configuration.VaultConfiguration.engineVersions;
import static com.datapipe.jenkins.vault.credentials.common.VaultHelper.getVaultSecretKey;
import static org.apache.commons.lang.StringUtils.defaultIfBlank;

@SuppressWarnings("ALL")
public class VaultSSHUserPrivateKeyImpl extends AbstractVaultBaseStandardCredentials implements
    VaultSSHUserPrivateKey {

    private static final Logger LOGGER = Logger
        .getLogger(VaultSSHUserPrivateKeyImpl.class.getName());

    public static final String DEFAULT_USERNAME_KEY = "username";
    public static final String DEFAULT_PRIVATE_KEY_KEY = "private_key";
    public static final String DEFAULT_PASSPHRASE_KEY = "passphrase";

    private static final long serialVersionUID = 1L;

    private String usernameKey;
    private String privateKeyKey;
    private String passphraseKey;
    private Supplier<Secret> username;
    private Supplier<Secret> privateKey;
    private Supplier<Secret> passphrase;

    public VaultSSHUserPrivateKeyImpl(CredentialsScope scope, String id,
        String description, Supplier<Secret> username, Supplier<Secret> privateKey, Supplier<Secret> passphrase) {
        super(scope, id, description);
        this.username = username;
        this.privateKey = privateKey;
        this.passphrase = passphrase;
    }

    @DataBoundConstructor
    public VaultSSHUserPrivateKeyImpl(CredentialsScope scope, String id,
        String description) {
        super(scope, id, description);
        username = null;
        privateKey = null;
        passphrase = null;
    }

    @NonNull
    public String getUsernameKey() {
        return usernameKey;
    }

    @DataBoundSetter
    public void setUsernameKey(String usernameKey) {
        this.usernameKey = defaultIfBlank(usernameKey, DEFAULT_USERNAME_KEY);
    }

    @NonNull
    public String getPrivateKeyKey() {
        return privateKeyKey;
    }

    @DataBoundSetter
    public void setPrivateKeyKey(String privateKeyKey) {
        this.privateKeyKey = defaultIfBlank(privateKeyKey, DEFAULT_PRIVATE_KEY_KEY);
    }

    @NonNull
    public String getPassphraseKey() {
        return passphraseKey;
    }

    @DataBoundSetter
    public void setPassphraseKey(String passphraseKey) {
        this.passphraseKey = defaultIfBlank(passphraseKey, DEFAULT_PASSPHRASE_KEY);
    }

    @NonNull
    @Override
    public String getUsername() {
        if (username != null) {
            return Secret.toString(username.get());
        }
        return Secret.toString(Secret.fromString(getVaultSecretKeyValue(defaultIfBlank(getUsernameKey(), DEFAULT_USERNAME_KEY))));
    }

    @NonNull
    @Override
    public String getPrivateKey() {
        if (privateKey != null) {
            return Secret.toString(privateKey.get());
        }
        return Secret.toString(Secret.fromString(getVaultSecretKeyValue(defaultIfBlank(getPrivateKeyKey(), DEFAULT_PRIVATE_KEY_KEY))));
    }

    @NonNull
    @Override
    public List<String> getPrivateKeys() {
        return Collections.singletonList(getPrivateKey());
    }

    @NonNull
    @Override
    public Secret getPassphrase() {
        if (passphrase != null) {
            return passphrase.get();
        }
        return Secret.fromString(getVaultSecretKeyValue(defaultIfBlank(getPassphraseKey(), DEFAULT_PASSPHRASE_KEY)));
    }

    @Extension
    public static class DescriptorImpl extends BaseStandardCredentialsDescriptor {

        @Override
        public String getDisplayName() {
            return "Vault SSH Username with private key Credential";
        }

        public FormValidation doTestConnection(
            @AncestorInPath ItemGroup<Item> context,
            @QueryParameter("path") String path,
            @QueryParameter("usernameKey") String usernameKey,
            @QueryParameter("privateKeyKey") String privateKeyKey,
            @QueryParameter("passphraseKey") String passphraseKey,
            @QueryParameter("prefixPath") String prefixPath,
            @QueryParameter("namespace") String namespace,
            @QueryParameter("engineVersion") Integer engineVersion) {

            String username;
            try {
                username = getVaultSecretKey(path, defaultIfBlank(usernameKey, DEFAULT_USERNAME_KEY), prefixPath, namespace, engineVersion);
            } catch (Exception e) {
                return FormValidation.error("FAILED to retrieve username key: \n" + e);
            }

            try {
                getVaultSecretKey(path, defaultIfBlank(privateKeyKey, DEFAULT_PRIVATE_KEY_KEY), prefixPath, namespace, engineVersion);
            } catch (Exception e) {
                return FormValidation.error("FAILED to retrieve private key key: \n" + e);
            }

            try {
                getVaultSecretKey(path, defaultIfBlank(passphraseKey, DEFAULT_PASSPHRASE_KEY), prefixPath, namespace, engineVersion);
            } catch (Exception e) {
                return FormValidation.error("FAILED to retrieve passphrase key: \n" + e);
            }

            return FormValidation.ok(String.format(
                "Successfully retrieved username %s, the private key and the passphrase",
                username));
        }

        @SuppressWarnings("unused") // used by stapler
        public ListBoxModel doFillEngineVersionItems(@AncestorInPath Item context) {
            return engineVersions(context);
        }
    }
}
