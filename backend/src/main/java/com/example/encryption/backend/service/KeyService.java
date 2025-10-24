package com.example.encryption.backend.service;

import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.util.Map;

public interface KeyService {
    Map<String, String> getActivePublicKey();
    PrivateKey getPrivateKeyById(String keyId);
    String getActiveKeyId();
}
