package com.example.authservice.application;

import com.example.authservice.domain.Role;
import com.example.authservice.domain.User;
import com.example.authservice.port.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RegisterUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RegisterUserUseCase registerUserUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser() {
        // Datos de prueba
        String username = "testuser";
        String email = "test@example.com";
        String password = "password123";

        // Crea un usuario simulado
        User user = new User(null, username, email, password, Role.PLAYER);

        // Configura el mock del repositorio para devolver el usuario simulado
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Llama al método de registro
        User registeredUser = registerUserUseCase.register(username, email, password);

        // Verifica que el usuario registrado tenga los datos correctos
        assertEquals(username, registeredUser.getUsername());
        assertEquals(email, registeredUser.getEmail());
        assertEquals(password, registeredUser.getPassword());

        // Verifica que el repositorio se haya llamado una vez
        verify(userRepository, times(1)).save(any(User.class));
    }
}
