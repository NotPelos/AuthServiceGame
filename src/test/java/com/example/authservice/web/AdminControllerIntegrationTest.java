package com.example.authservice.web;

import com.example.authservice.AuthServiceApplication;
import com.example.authservice.domain.Role;
import com.example.authservice.domain.User;
import com.example.authservice.infrastructure.UserJpaRepository;
import com.example.authservice.infrastructure.security.JwtUtil;
import com.example.gameservice.application.GenerateGameReportUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.lang.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@SpringBootTest(classes = AuthServiceApplication.class, properties = {"spring.profiles.active=test"})
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminControllerIntegrationTest {

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

    private String adminToken;
    private String playerToken;
    
    @MockBean
    private GenerateGameReportUseCase generateGameReportUseCase;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        // Crear un usuario administrador
        User admin = new User();
        admin.setUsername("adminuser");
        admin.setPassword(passwordEncoder.encode("adminpass"));
        admin.setEmail("admin@example.com");
        admin.setRole(Role.ADMIN);
        userRepository.save(admin);
        List<GrantedAuthority> adminAuthorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        adminToken = jwtUtil.generateToken(admin.getUsername(), adminAuthorities);

        // Crear un usuario jugador
        User player = new User();
        player.setUsername("playeruser");
        player.setPassword(passwordEncoder.encode("playerpass"));
        player.setEmail("player@example.com");
        player.setRole(Role.PLAYER);
        userRepository.save(player);
        List<GrantedAuthority> playerAuthorities = List.of(new SimpleGrantedAuthority("ROLE_PLAYER"));
        playerToken = jwtUtil.generateToken(player.getUsername(), playerAuthorities);
    }

    @Test
    void testAdminAccessToGetAllUsers() throws Exception {
        mockMvc.perform(get("/admin/users")
                .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk());
    }

    @Test
    void testPlayerAccessDeniedToGetAllUsers() throws Exception {
        mockMvc.perform(get("/admin/users")
                .header("Authorization", "Bearer " + playerToken))
                .andExpect(status().isForbidden());
    }
}
