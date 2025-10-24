package com.example.encryption.backend.service;

public interface EncryptionService {
    /*
    * Decrypts an incoming encrypted AES key and payload,
    * processes the message, and returns an encrypted response.
    *
    * @param encryptedKey Base64 encoded RSA-encrypted AES key from frontend
    * @param encrytedPayload Base64 encoded AES-encrypted payload from frontend
    * @return Base64 encoded AES-encrypted response for frontend
    * @throws Exception if decryption or encryption fails
    * */
    String processEncryptedRequest(String encryptedKey, String encryptedPayload, String keyId) throws Exception;
}
