package com.example.demo.services.integration;

import com.example.demo.data.TestDataLoader;
import com.example.demo.domain.Rol;
import com.example.demo.domain.User;
import com.example.demo.dtos.UserRegistrationRequest;
import com.example.demo.exceptions.ValueConflictException;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    private Map<String, User> users;


    @BeforeEach
    void setUp() {
        users = TestDataLoader.loadTestData(userRepository,mongoTemplate);
    }

    @Test
    void testCreateUser() {
        // Sección de Arrange: Se crean los datos del usuario a ser registrado
        var user = new UserRegistrationRequest("juan@example.com","12345Abc","Juan Perez",
                LocalDate.of(1980,6,25), Rol.USER);
        // Sección de Act: Ejecute la acción de crear usuario.
        var newUser = userService.createUser(user);
        // Sección de Assert: Se verifica que el usuario se haya registrado con los datos proporcionados.
        assertNotNull(newUser.id());
        assertEquals(user.email(),newUser.email());
        assertEquals(user.fullName(),newUser.fullName());
        assertEquals(user.dateBirth(),newUser.dateBirth());
        assertEquals(user.rol(),newUser.rol());
    }

    @Test
    void testCreateUserThrowsValueConflictExceptionWhenEmailExists() {
        // Sección de Arrange: Se crean los datos del usuario a ser registrado (Con el email de un usuario ya existente).
        var userStore = users.values().stream().findAny().orElseThrow();
        var user = new UserRegistrationRequest(userStore.getEmail(), "12345Abc","Juan Perez",
                LocalDate.of(1980,6,25), Rol.USER);
        // Sección de Act y Sección de Assert: Ejecute la acción de crear usuario se verifica que genere una excepción debido al email repetido.
        assertThrows(ValueConflictException.class,() -> userService.createUser(user) );
    }

    @Test
    void testGetUserSuccess() {
        // Sección de Arrange: Se obtiene aleatoriamente uno de los usuarios registrado para pruebas.
        var userStore = users.values().stream().findAny().orElseThrow();
        // Sección de Act: Ejecute la acción de obtener usuario basado en su Id.
        var foundUser = userService.getUser(userStore.getId()).orElseThrow();
        // Sección de Assert: Se verifica que los datos obtenidos correspondan a los del usuario almacenado.
        assertEquals(userStore.getFullName(),foundUser.fullName());
        assertEquals(userStore.getDateBirth(),foundUser.dateBirth());
        assertEquals(userStore.getRol(),foundUser.rol());
    }

    @Test
    void testGetUserNotFound() {
        // Sección de Arrange: Se crean los datos del usuario a ser registrado (Con el email de un usuario ya existente).
        var id = UUID.randomUUID().toString();
        // Sección de Act: Ejecute la acción de obtener usuario basado en su Id.
        var user = userService.getUser(id);
        // Sección de Assert: Se verifica que los datos obtenidos correspondan a lo esperado.
        assertEquals(Optional.empty(),user);
    }
}