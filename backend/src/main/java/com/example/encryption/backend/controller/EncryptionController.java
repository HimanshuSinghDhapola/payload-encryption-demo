package com.example.encryption.backend.controller;

import com.example.encryption.backend.dto.ApiResponse;
import com.example.encryption.backend.dto.EncryptedRequest;
import com.example.encryption.backend.service.EncryptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/encryption")
public class EncryptionController {

    private final EncryptionService encryptionService;

    public EncryptionController(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @PostMapping("/process")
    public ResponseEntity<ApiResponse<String>> processEncryptedData(@RequestBody EncryptedRequest request) throws Exception {

        String encryptedResponse = encryptionService.processEncryptedRequest(request.getKey(), request.getData(), request.getKeyId());

        ApiResponse<String> response = new ApiResponse<>(
                "success",
                "Payload processed successfully",
                encryptedResponse
        );
        return ResponseEntity.ok(response);
    }
}
