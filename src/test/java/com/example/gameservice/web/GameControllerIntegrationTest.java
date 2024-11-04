package com.example.gameservice.web;

import com.example.authservice.AuthServiceApplication;
import com.example.authservice.domain.User;
import com.example.authservice.infrastructure.UserJpaRepository;
import com.example.authservice.infrastructure.security.JwtUtil;
import com.example.authservice.port.UserRepository;
import com.example.gameservice.application.ManageGamesUseCase;
import com.example.gameservice.domain.Game;
import com.example.gameservice.port.GameRepository;
import com.example.gameservice.port.GameSessionRepository;
import com.example.gameservice.port.NotificationRepository;
import com.example.gameservice.web.config.TestSecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {AuthServiceApplication.class, TestSecurityConfig.class}, properties = "spring.main.allow-bean-definition-overriding=true")
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ComponentScan(basePackages = {"com.example.authservice", "com.example.gameservice"})
class GameControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GameRepository gameRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserJpaRepository userJpaRepository; 

    @MockBean
    private PasswordEncoder passwordEncoder; 
    
    @MockBean
    private GameSessionRepository gamesessionrepository;
    
    @MockBean
    private NotificationRepository notificationRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private String token;

    @BeforeEach
    void setUp() {
        // Configuración del token JWT para el usuario de prueba
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        when(userRepository.findByUsername("testuser")).thenReturn(user);

        token = jwtUtil.generateToken("testuser", List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void testAddGame() throws Exception {
        Game game = new Game();
        game.setTitle("Nuevo Juego");
        game.setDescription("Descripción del nuevo juego");
        game.setGenre("Aventura");

        when(gameRepository.save(any(Game.class))).thenReturn(game);

        mockMvc.perform(post("/games/add")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(game)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Nuevo Juego"))
                .andExpect(jsonPath("$.description").value("Descripción del nuevo juego"))
                .andExpect(jsonPath("$.genre").value("Aventura"));

        verify(gameRepository, times(1)).save(any(Game.class));
    }

    @Test
    void testDeleteGame() throws Exception {
        Long gameId = 1L;

        when(gameRepository.existsById(gameId)).thenReturn(true);
        doNothing().when(gameRepository).deleteById(gameId);

        mockMvc.perform(delete("/games/delete/{id}", gameId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        verify(gameRepository, times(1)).deleteById(gameId);
    }

    @Test
    void testListGames() throws Exception {
        Game game1 = new Game();
        game1.setTitle("Juego 1");
        game1.setDescription("Descripción 1");
        game1.setGenre("Aventura");

        Game game2 = new Game();
        game2.setTitle("Juego 2");
        game2.setDescription("Descripción 2");
        game2.setGenre("Acción");

        when(gameRepository.findAll()).thenReturn(List.of(game1, game2));

        mockMvc.perform(get("/games/list")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Juego 1"))
                .andExpect(jsonPath("$[1].title").value("Juego 2"));

        verify(gameRepository, times(1)).findAll();
    }

    @Test
    void testUpdateGame() throws Exception {
        Long gameId = 1L;
        Game existingGame = new Game();
        existingGame.setTitle("Old Title");
        existingGame.setDescription("Old Description");
        existingGame.setGenre("Old Genre");

        Game updatedGame = new Game();
        updatedGame.setTitle("New Title");
        updatedGame.setDescription("New Description");
        updatedGame.setGenre("New Genre");

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(existingGame));
        when(gameRepository.save(any(Game.class))).thenReturn(updatedGame);

        mockMvc.perform(put("/games/update/{id}", gameId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedGame)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Title"))
                .andExpect(jsonPath("$.description").value("New Description"))
                .andExpect(jsonPath("$.genre").value("New Genre"));

        verify(gameRepository, times(1)).save(existingGame);
    }
}
