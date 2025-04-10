package com.example.demo.setup;

import com.example.demo.domain.User;
import com.example.demo.domain.UserStatus;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DefaultUserInitializer implements CommandLineRunner {
    private final DefaultUserProperties defaultUserProperties;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            defaultUserProperties.getUsers().stream()
                    .map(this::createUser).forEach(userRepository::save);
        }
    }

    private User createUser(DefaultUserProperties.DefaultUser defaultUser){
        return new User(UUID.randomUUID().toString(), defaultUser.username(), passwordEncoder.encode(defaultUser.password()),
                defaultUser.username(), LocalDate.of(1982,8,27), defaultUser.role(), UserStatus.ACTIVE);

    }
}