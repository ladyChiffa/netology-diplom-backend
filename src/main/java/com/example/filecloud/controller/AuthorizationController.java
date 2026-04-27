package com.example.filecloud.controller;

import com.example.filecloud.model.Credentials;
import com.example.filecloud.model.Token;
import com.example.filecloud.service.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AuthorizationController {
    private final AuthorizationService authService;

    @PostMapping("/login")
    public ResponseEntity<Token> getAuthorities(@RequestBody Credentials guest) {
        return ResponseEntity.ok(authService.getAuthorities(guest.getLogin(), guest.getPassword()));
    }

    @PostMapping("/logout")
    public void dropAuthorities(@RequestHeader Map<String, String> headers) {
        String authToken = headers.get("auth-token").replace("Bearer ", "");
    }
}

