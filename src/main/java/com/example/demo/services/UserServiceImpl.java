package com.example.demo.services;

import com.example.demo.domain.User;
import com.example.demo.dtos.UserRegistrationRequest;
import com.example.demo.dtos.UserResponse;
import com.example.demo.exceptions.ValueConflictException;
import com.example.demo.mappers.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{
    private final Map<String, User> userStore = new ConcurrentHashMap<>();
    private final UserMapper userMapper;

    @Override
    public UserResponse createUser(UserRegistrationRequest user) {
        if (userStore.values().stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(user.email()))) {
            throw new ValueConflictException("Email ya registrado");
        }
        var newUser = userMapper.parseOf(user);
//        newUser.setId(UUID.randomUUID().toString());
//        newUser.setStatus(UserStatus.REGISTERED);
        userStore.put(newUser.getId(), newUser);
        return userMapper.toUserResponse(newUser);
    }

    @Override
    public Optional<UserResponse> getUser(String id) {
        return Optional.ofNullable(userStore.get(id))
                .map(userMapper::toUserResponse);
    }
}
