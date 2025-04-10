package com.example.demo.services;

import com.example.demo.dtos.UserRegistrationRequest;
import com.example.demo.dtos.UserResponse;
import com.example.demo.dtos.UserSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

public interface UserService {

    UserResponse createUser(UserRegistrationRequest user);

    @PreAuthorize("hasAuthority('ADMIN') or @securityService.isCurrentUser(#id)")
    Optional<UserResponse> getUser(String id);
    Page<UserResponse> searchUsers(UserSearchRequest request);
    // ...
}