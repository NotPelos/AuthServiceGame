package com.example.authservice.web;

import com.example.authservice.AuthServiceApplication;
import com.example.authservice.domain.Role;
import com.example.authservice.domain.User;
import com.example.authservice.infrastructure.UserJpaRepository;
import com.example.authservice.infrastructure.security.JwtUtil;
import com.example.authservice.port.UserRepository;
import com.example.gameservice.application.GenerateGameReportUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = AuthServiceApplication.class, properties = {"spring.profiles.active=test"})
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIntegrationTest {
	
	@Configuration
    static class TestConfig {
        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserJpaRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @MockBean
    private GenerateGameReportUseCase generateGameReportUseCase;

    @BeforeEach
    void setUp() {
        // Limpiar la base de datos y crear un usuario de prueba
        userRepository.deleteAll();

        User testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword(passwordEncoder.encode("password123")); // Contraseña encriptada
        testUser.setRole(Role.PLAYER);
        userRepository.save(testUser);
    }

    @Test
    @WithMockUser
    void testRegisterUser() throws Exception {
        UserController.UserDTO userDTO = new UserController.UserDTO();
        userDTO.setUsername("newuser");
        userDTO.setEmail("newuser@example.com");
        userDTO.setPassword("newpassword");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON)) // Verifica que sea JSON
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.email").value("newuser@example.com"));
    }

    @Test
    void testLogin() throws Exception {
        UserController.AuthRequest authRequest = new UserController.AuthRequest();
        authRequest.setUsername("testuser");
        authRequest.setPassword("password123");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN)) // Ajuste para compatibilidad
                .andExpect(content().string(org.hamcrest.Matchers.notNullValue())); // Verifica que el token no sea nulo
    }
    
    @Test
    void testRegisterUserWithDuplicateUsername() throws Exception {
        UserController.UserDTO userDTO = new UserController.UserDTO();
        userDTO.setUsername("testuser"); // Username duplicado
        userDTO.setEmail("duplicate@example.com");
        userDTO.setPassword("newpassword");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isBadRequest()); // Asegura que el estatus sea 400 Bad Request
    }
    
    
    @Test
    void testLoginWithInvalidCredentials() throws Exception {
        UserController.AuthRequest authRequest = new UserController.AuthRequest();
        authRequest.setUsername("testuser");
        authRequest.setPassword("wrongpassword"); // Contraseña incorrecta

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isUnauthorized()) // Verifica que devuelve un estado de autorización fallida
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Bad credentials"))); // Asegura el mensaje de error
    }

}
