package com.example.encryption.backend.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EncryptedRequest {
    private String key;
    private String data;
    private String keyId;
}
