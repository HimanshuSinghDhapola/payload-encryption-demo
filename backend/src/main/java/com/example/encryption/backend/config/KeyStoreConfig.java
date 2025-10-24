package com.example.encryption.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KeyStoreConfig {
    private static final String KEYSTORE_PASSWORD = "Abcenc123";

    // currently active key
    private static final String ACTIVE_KEY_ALIAS = "rsa-key-v1";

    @Bean
    public Map<String, KeyPair> rsaKeyPairs() throws Exception{
        InputStream keyStoreStream = getClass().getClassLoader().getResourceAsStream("keystore.p12");
        if(keyStoreStream == null){
            throw new RuntimeException("keystore.p12 not found in classpath");
        }
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        keyStore.load(keyStoreStream, KEYSTORE_PASSWORD.toCharArray());

        Map<String, KeyPair> keys = new HashMap<>();

        Enumeration<String> aliases = keyStore.aliases();
        while(aliases.hasMoreElements()){
            String alias = aliases.nextElement();
            KeyStore.PrivateKeyEntry entry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(
                    alias,
                    new KeyStore.PasswordProtection(KEYSTORE_PASSWORD.toCharArray())
            );
            PrivateKey privateKey = entry.getPrivateKey();
            PublicKey publicKey = entry.getCertificate().getPublicKey();
            keys.put(alias, new KeyPair(publicKey, privateKey));
        }

        return keys;
    }

    @Bean
    public String activeKeyAlias() {
        return ACTIVE_KEY_ALIAS;
    }
}
