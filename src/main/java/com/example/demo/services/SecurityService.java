package com.example.demo.services;

import com.example.demo.dtos.LoginRequest;
import com.example.demo.dtos.TokenResponse;

public interface SecurityService {
    TokenResponse login(LoginRequest request);
}
