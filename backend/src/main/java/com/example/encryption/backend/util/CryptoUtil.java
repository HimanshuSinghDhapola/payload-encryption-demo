package com.example.encryption.backend.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.spec.MGF1ParameterSpec;
import java.util.Base64;

public class CryptoUtil {

    // Decrypts the aes-key
    public static SecretKey decryptAESKey(String encryptedBase64Key, PrivateKey privateKey) throws Exception{
        byte[] encryptedKeyBytes = Base64.getDecoder().decode(encryptedBase64Key);
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
        OAEPParameterSpec oaepParams = new OAEPParameterSpec(
                "SHA-256",
                "MGF1",
                MGF1ParameterSpec.SHA256,
                PSource.PSpecified.DEFAULT
        );
        rsaCipher.init(Cipher.DECRYPT_MODE, privateKey, oaepParams);
        byte[] aesKeyBytes = rsaCipher.doFinal(encryptedKeyBytes);
        return new SecretKeySpec(aesKeyBytes, 0, aesKeyBytes.length, "AES");
    }

    // Decrypts payload using AES-key
    public static String decryptPayload(String encryptedBase64Data, SecretKey aesKey) throws  Exception{
        byte[] encryptedData = Base64.getDecoder().decode(encryptedBase64Data);

        byte[] iv = new byte[12];

        System.arraycopy(encryptedData, 0, iv, 0, 12);
        byte[] ciphertext = new byte[encryptedData.length - 12];
        System.arraycopy(encryptedData, 12, ciphertext, 0, ciphertext.length);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.DECRYPT_MODE, aesKey, gcmSpec);
        byte[] decrypted = cipher.doFinal(ciphertext);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    // Encrypts response using the same AES key
    public static String encryptResponse(String response, SecretKey aesKey) throws Exception{
        byte[] iv = new byte[12];
        java.security.SecureRandom random = new java.security.SecureRandom();
        random.nextBytes(iv);

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec gcmSpec = new GCMParameterSpec(128, iv);
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, gcmSpec);

        byte[] encrypted = cipher.doFinal(response.getBytes(StandardCharsets.UTF_8));

        // add IV to the start of ciphertext
        byte[] combined = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

        return Base64.getEncoder().encodeToString(combined);
    }
}
