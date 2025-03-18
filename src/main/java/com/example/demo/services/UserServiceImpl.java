package com.example.demo.services;

import com.example.demo.dtos.UserRegistrationRequest;
import com.example.demo.dtos.UserResponse;
import com.example.demo.exceptions.ValueConflictException;
import com.example.demo.mappers.UserMapper;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public UserResponse createUser(UserRegistrationRequest user) {
        if (userRepository.findUserByEmail(user.email()).isPresent()) {
            throw new ValueConflictException("Email ya registrado");
        }
        var newUser = userMapper.parseOf(user);
        return userMapper.toUserResponse(userRepository.save(newUser));
    }

    @Override
    public Optional<UserResponse> getUser(String id) {
        return userRepository.findById(id)
                .map(userMapper::toUserResponse);
    }
}
