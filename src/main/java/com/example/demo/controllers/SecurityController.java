package com.example.demo.controllers;

import com.example.demo.dtos.LoginRequest;
import com.example.demo.dtos.TokenResponse;
import com.example.demo.services.SecurityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequiredArgsConstructor
public class SecurityController {
    private final SecurityService securityService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(securityService.login(request));
    }
}
