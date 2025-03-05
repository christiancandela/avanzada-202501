package com.example.demo.services;

import com.example.demo.domain.User;
import com.example.demo.domain.UserStatus;
import com.example.demo.dtos.UserRegistrationRequest;
import com.example.demo.dtos.UserResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserServiceImpl implements UserService{
    private final Map<String, User> userStore = new ConcurrentHashMap<>();

    @Override
    public UserResponse createUser(UserRegistrationRequest user) {
        if (userStore.values().stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(user.email()))) {
            throw new RuntimeException("Email ya registrado");
        }
        var newUser = User.builder()
                .id(UUID.randomUUID().toString())
                .rol(user.rol())
                .dateBirth(user.dateBirth())
                .email(user.email())
                .fullName(user.fullName())
                .password(encode(user.password()))
                .status(UserStatus.REGISTERED).build();
        userStore.put(newUser.getId(), newUser);
        return new UserResponse(newUser.getId(), newUser.getEmail(), newUser.getFullName(), newUser.getDateBirth(),newUser.getRol());
    }

    @Override
    public Optional<UserResponse> getUser(String id) {
        var user = userStore.get(id);
        UserResponse userResponse = null;
        if( user != null ){
            userResponse = new UserResponse(user.getId(), user.getEmail(), user.getFullName(), user.getDateBirth(),user.getRol());
        }
        return Optional.ofNullable(userResponse);
    }

//    @Override
//    public Optional<UserResponse> getUser(String id) {
//        return Optional.ofNullable(userStore.get(id))
//                .map(
//                        user->new UserResponse(user.getId(), user.getEmail(), user.getFullName(), user.getDateBirth(),user.getRol())
//                );
//    }

    private String encode(String password){
        var passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }
}
