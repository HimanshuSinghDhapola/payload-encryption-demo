package com.example.encryption.backend.service.impl;

import com.example.encryption.backend.service.EncryptionService;
import com.example.encryption.backend.service.KeyService;
import com.example.encryption.backend.util.CryptoUtil;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.PrivateKey;

@Service
public class EncryptionServiceImpl implements EncryptionService {

    private final KeyService keyService;

    public EncryptionServiceImpl(KeyService keyService){
        this.keyService = keyService;
    }

    @Override
    public String processEncryptedRequest(String encryptedKey, String encryptedPayload, String keyId) throws Exception{
        PrivateKey privateKey = keyService.getPrivateKeyById(keyId);

        // Decrypt AES key and payload
        SecretKey aesKey = CryptoUtil.decryptAESKey(encryptedKey, privateKey);
        String decryptedJson = CryptoUtil.decryptPayload(encryptedPayload, aesKey);

        // Parse JSON and extract "message"
        JSONObject requestJson = new JSONObject(decryptedJson);
        String originalMessage = requestJson.getString("message");

        String processedMessage = originalMessage.toUpperCase();

        // Wrap processed message in JSON again
        JSONObject responseJson = new JSONObject();
        responseJson.put("message", processedMessage);

        return CryptoUtil.encryptResponse(responseJson.toString(), aesKey);

    }
}
