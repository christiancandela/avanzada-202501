package com.example.demo.services.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.demo.domain.Rol;
import com.example.demo.domain.User;
import com.example.demo.domain.UserStatus;
import com.example.demo.dtos.UserRegistrationRequest;
import com.example.demo.dtos.UserResponse;
import com.example.demo.exceptions.ValueConflictException;
import com.example.demo.mappers.UserMapper;
import com.example.demo.repositories.UserRepository;

import com.example.demo.services.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class) // Habilita Mockito en JUnit 5
class UserServiceTest {

    @Mock
    private UserRepository userRepository; // Simula la capa de persistencia

    @Mock
    private UserMapper userMapper; // Simula la conversión de entidades a DTOs

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService; // Inyecta los mocks en la implementación real

    private UserRegistrationRequest userRequest;
    private UserResponse userResponse;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User(UUID.randomUUID().toString(),"juan@example.com","12345Abc","Juan Perez", LocalDate.of(1980,6,25), Rol.USER, UserStatus.REGISTERED);
        userRequest = new UserRegistrationRequest(user.getEmail(), user.getPassword(), user.getFullName(),user.getDateBirth(),user.getRol());
        userResponse = new UserResponse(UUID.randomUUID().toString(),user.getEmail(), user.getFullName(), user.getDateBirth(),user.getRol());
    }

    @Test
    void testCreateUserSuccess() {
        // Arrange: Simular que no existe un usuario con el email dado
        when(userRepository.findExistingUserByEmail(userRequest.email())).thenReturn(Optional.empty());
        when(userMapper.parseOf(userRequest)).thenReturn(user); // Simular conversión DTO -> Entity
        when(passwordEncoder.encode(userRequest.password())).thenReturn(user.getPassword());
        when(userRepository.save(any(User.class))).thenReturn(user); // Simular persistencia
        when(userMapper.toUserResponse(any(User.class))).thenReturn(userResponse); // Simular conversión Entity -> DTO

        // Act: Llamar al método que se está probando
        UserResponse result = userService.createUser(userRequest);

        // Assert: Verificar que los datos devueltos son los esperados
        assertNotNull(result);
        assertEquals(userResponse.id(), result.id());
        assertEquals(userResponse.email(), result.email());

        // Verificar que los mocks fueron llamados correctamente
        verify(userRepository).findExistingUserByEmail(userRequest.email());
        verify(userRepository).save(any(User.class));
        verify(userMapper).toUserResponse(any(User.class));
    }

    @Test
    void testCreateUserThrowsValueConflictExceptionWhenEmailExists() {
        // Arrange: Simular que ya existe un usuario con el mismo email
        when(userRepository.findExistingUserByEmail(userRequest.email())).thenReturn(Optional.of(user));

        // Act & Assert: Verificar que se lanza la excepción cuando el usuario ya existe
        assertThrows(ValueConflictException.class, () -> userService.createUser(userRequest));

        // Verificar que el método `save` nunca fue llamado
        verify(userRepository, never()).save(any());
    }

    @Test
    void testGetUserSuccess() {
        // Arrange: Simular que el usuario existe
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.toUserResponse(any(User.class))).thenReturn(userResponse);

        // Act: Se ejecuta el método para obtener el usuario
        Optional<UserResponse> result = userService.getUser(user.getId());

        // Assert: Se verifica que el usuario obtenido corresponda con el solicitado
        assertTrue(result.isPresent());
        assertEquals(userResponse.id(), result.get().id());
        // Verificar que los mocks fueron llamados correctamente
        verify(userRepository).findById(user.getId());
        verify(userMapper).toUserResponse(any(User.class));
    }

    @Test
    void testGetUserNotFound() {
        // Arrange: Simular que el usuario no existe
        when(userRepository.findById("99")).thenReturn(Optional.empty());

        // Act: Se ejecuta el método para obtener el usuario
        Optional<UserResponse> result = userService.getUser("99");

        // Assert: Se verifica que la respuesta este vacía.
        assertFalse(result.isPresent());

        verify(userRepository).findById("99");
        verify(userMapper, never()).toUserResponse(any());
    }
}
