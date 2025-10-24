package com.example.encryption.backend.service.impl;

import com.example.encryption.backend.service.KeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Service
public class KeyServiceImpl implements KeyService {

    @Autowired
    private Map<String, KeyPair> rsaKeyPairs;

    private final List<String> keyIds = List.of("rsa-key-v1", "rsa-key-v2");
    private int currentIdx = 0;
    private String activeKeyId = keyIds.get(0);

    private byte[] toUnsignedBytes(BigInteger value) {
        byte[] bytes = value.toByteArray();
        if (bytes[0] == 0) {
            // remove leading zero
            byte[] tmp = new byte[bytes.length - 1];
            System.arraycopy(bytes, 1, tmp, 0, tmp.length);
            return tmp;
        }
        return bytes;
    }

    @Scheduled(fixedRate = 60*1000)
    public void rotateKeyAutomatically(){
        currentIdx = (currentIdx+1)%keyIds.size();
        activeKeyId = keyIds.get(currentIdx);
    }


    @Override
    public Map<String, String> getActivePublicKey() {
        KeyPair keyPair = rsaKeyPairs.get(activeKeyId);
        if(keyPair == null){
            throw new RuntimeException("Active key not found");
        }

        RSAPublicKey pub = (RSAPublicKey) keyPair.getPublic();

        String n = Base64.getUrlEncoder().withoutPadding().encodeToString(toUnsignedBytes(pub.getModulus()));
        String e = Base64.getUrlEncoder().withoutPadding().encodeToString(toUnsignedBytes(pub.getPublicExponent()));

        return Map.of(
                "kid", activeKeyId,
                "kty", "RSA",
                "alg", "RSA-OAEP-256",
                "n", n,
                "e", e
        );
    }

    @Override
    public PrivateKey getPrivateKeyById(String keyId){
        KeyPair keyPair = rsaKeyPairs.get(keyId);
        if(keyPair == null){
            throw new RuntimeException("Private key not found for KeyId: "+keyId);
        }
        return keyPair.getPrivate();
    }

    @Override
    public String getActiveKeyId(){
        return activeKeyId;
    }
}
