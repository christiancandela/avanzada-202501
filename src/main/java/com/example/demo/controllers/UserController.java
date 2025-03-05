package com.example.demo.controllers;


import com.example.demo.dtos.UserRegistrationRequest;
import com.example.demo.dtos.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;


@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRegistrationRequest request){
        var response = new UserResponse(UUID.randomUUID().toString(), request.email(), request.fullName(), request.dateBirth(),request.rol());
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> get(@PathVariable("id") String id){
        return ResponseEntity.notFound().build();
    }
}
