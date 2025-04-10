package com.example.demo.services;

import com.example.demo.dtos.LoginRequest;
import com.example.demo.dtos.TokenResponse;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service("securityService")
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;

    @Value("${jwt.expiry}")
    private long expiry;

    @Override
    public TokenResponse login(LoginRequest request) {
        final var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        final var roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        final var now = Instant.now();
        final var expire = now.plus(expiry, ChronoUnit.MINUTES);
        return new TokenResponse(jwtTokenProvider.generateTokenAsString(authentication.getName(),roles,now,expire),
                "Bearer",expire,roles);
    }

    public boolean isCurrentUser(String id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findById(id)
                .map(user -> user.getEmail().equals(username))
                .orElse(false);
    }
}
