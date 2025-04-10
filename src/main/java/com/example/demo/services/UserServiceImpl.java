package com.example.demo.services;

import com.example.demo.dtos.UserSearchRequest;
import com.example.demo.dtos.UserRegistrationRequest;
import com.example.demo.dtos.UserResponse;
import com.example.demo.exceptions.ValueConflictException;
import com.example.demo.mappers.UserMapper;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(UserRegistrationRequest user) {
        if (userRepository.findExistingUserByEmail(user.email()).isPresent()) {
            throw new ValueConflictException("Email ya registrado");
        }
        var newUser = userMapper.parseOf(user);
        newUser.setPassword(passwordEncoder.encode(user.password()));
        return userMapper.toUserResponse(userRepository.save(newUser));
    }

    @Override
    public Optional<UserResponse> getUser(String id) {
        return userRepository.findById(id)
                .map(userMapper::toUserResponse);
    }

    public Page<UserResponse> searchUsers(UserSearchRequest request) {
        // Configurar paginación
        Pageable pageable = PageRequest.of(request.page(), request.size());

        // Llamar al repositorio con los filtros
        return userRepository.findExistingUsersByFilters(
                request.fullName(),
                request.email(),
                request.dateBirth(),
                pageable
        ).map(userMapper::toUserResponse);
    }
}
