package com.example.demo.services;

import com.example.demo.dtos.UserRegistrationRequest;
import com.example.demo.dtos.UserResponse;

import java.util.Optional;

public interface UserService {

    UserResponse createUser(UserRegistrationRequest user);

    Optional<UserResponse> getUser(String id);
    
    // ...
}