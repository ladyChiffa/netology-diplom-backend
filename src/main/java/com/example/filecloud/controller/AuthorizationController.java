package com.example.filecloud.controller;

import com.example.filecloud.model.Credentials;
import com.example.filecloud.model.Token;
import com.example.filecloud.service.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger logger = LogManager.getLogger(AuthorizationController.class);

    @PostMapping("/login")
    public ResponseEntity<Token> getAuthorities(@RequestBody Credentials guest) {
        logger.info("=== IN CONTROLLER === POST /login");
        logger.info("=== IN CONTROLLER === user requested: " + guest.getLogin());
        return ResponseEntity.ok(authService.getAuthorities(guest.getLogin(), guest.getPassword()));
    }

    @PostMapping("/logout")
    public void dropAuthorities(@RequestHeader Map<String, String> headers) {
        logger.info("=== IN CONTROLLER === POST /logout");
        String authToken = headers.get("auth-token").replace("Bearer ", "");
    }
}

