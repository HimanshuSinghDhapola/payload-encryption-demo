package com.example.encryption.backend.controller;

import com.example.encryption.backend.service.KeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/key")
public class KeyController {
    @Autowired
    private KeyService keyService;

    @GetMapping("/getPublicKey")
    public Map<String, String> getActivePublicKey(){
        return keyService.getActivePublicKey();
    }
}
