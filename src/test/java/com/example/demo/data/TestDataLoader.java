package com.example.demo.data;

import com.example.demo.domain.Rol;
import com.example.demo.domain.User;
import com.example.demo.domain.UserStatus;
import com.example.demo.repositories.UserRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TestDataLoader {

    public static Map<String, User> loadTestData(UserRepository userRespository, MongoTemplate mongoTemplate) {
            var encoder = new BCryptPasswordEncoder();
            return loadTestData(
                    List.of(
                        new User(UUID.randomUUID().toString(),"ana@example.com", encoder.encode("12346Abc"),"Ana López", LocalDate.of(1982,8,27), Rol.USER, UserStatus.ACTIVE),
                        new User(UUID.randomUUID().toString(),"carlos@example.com",encoder.encode("12346Abc"),"Carlos Pérez", LocalDate.of(1984,10,28), Rol.USER, UserStatus.ACTIVE)
                    ),
                    userRespository,
                    mongoTemplate
            );
    }

    public static Map<String, User> loadTestData(Collection<User> newUsers,UserRepository userRespository, MongoTemplate mongoTemplate) {
        // Borrar datos existentes para asegurar la repetibilidad de las pruebas.
        mongoTemplate.getDb().listCollectionNames()
                .forEach(mongoTemplate::dropCollection);
        return userRespository.saveAll(newUsers).stream().collect(Collectors.toMap(User::getId, usuario -> usuario));
    }
}