package com.example.gameservice.web;

import com.example.authservice.AuthServiceApplication;
import com.example.authservice.infrastructure.security.JwtUtil;
import com.example.gameservice.application.GenerateGameReportUseCase;
import com.example.gameservice.domain.Game;
import com.example.gameservice.dto.GameStatsDTO;
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

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {AuthServiceApplication.class, TestSecurityConfig.class}, properties = "spring.main.allow-bean-definition-overriding=true")
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ComponentScan(basePackages = {"com.example.authservice", "com.example.gameservice"})
class GameStatisticsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @MockBean
    private GameSessionRepository gameSessionRepository;

    @MockBean
    private GameRepository gameRepository;
    
    @MockBean
    private PasswordEncoder passwordEncoder; 
    
    @MockBean 
    private GenerateGameReportUseCase generateGameReportUseCase;
    
    @MockBean
    private NotificationRepository notificationRepository;

    private String token;

    @BeforeEach
    void setUp() {
        token = jwtUtil.generateToken("testuser", List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void testGetGameStats() throws Exception {
        Long gameId = 1L;
        GameStatsDTO stats = new GameStatsDTO(gameId, "Juego de Prueba", 3, 180, 60, 120, 30);

        when(gameRepository.existsById(gameId)).thenReturn(true);
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(new Game(gameId, null, "Juego de Prueba", "Descripción", "Aventura")));
        when(gameSessionRepository.countByGameId(gameId)).thenReturn(3L);
        when(gameSessionRepository.findTotalTimeByGameId(gameId)).thenReturn(180);
        when(gameSessionRepository.findMaxSessionDurationByGameId(gameId)).thenReturn(120); 
        when(gameSessionRepository.findMinSessionDurationByGameId(gameId)).thenReturn(30);  

        mockMvc.perform(get("/statistics/game/{gameId}", gameId)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalSessions").value(3))
                .andExpect(jsonPath("$.totalTimeInMinutes").value(180))
                .andExpect(jsonPath("$.averageSessionDuration").value(60))
                .andExpect(jsonPath("$.maxSessionDuration").value(120))  
                .andExpect(jsonPath("$.minSessionDuration").value(30));   
    }
    
    @Test
    void testCompareGameStats() throws Exception {
        List<Long> gameIds = List.of(1L, 2L);

        // Configura los mocks para cada juego en la comparación
        when(gameRepository.findById(1L)).thenReturn(Optional.of(new Game(1L, null, "Juego 1", "Descripción 1", "Aventura")));
        when(gameRepository.findById(2L)).thenReturn(Optional.of(new Game(2L, null, "Juego 2", "Descripción 2", "Acción")));

        // Configura los valores simulados para las métricas de sesión de juego
        when(gameSessionRepository.countByGameId(1L)).thenReturn(3L);
        when(gameSessionRepository.findTotalTimeByGameId(1L)).thenReturn(180);  // 3 sesiones de 60 minutos cada una
        when(gameSessionRepository.findMaxSessionDurationByGameId(1L)).thenReturn(120);
        when(gameSessionRepository.findMinSessionDurationByGameId(1L)).thenReturn(30);

        when(gameSessionRepository.countByGameId(2L)).thenReturn(2L);
        when(gameSessionRepository.findTotalTimeByGameId(2L)).thenReturn(90);   // 2 sesiones de 45 minutos cada una
        when(gameSessionRepository.findMaxSessionDurationByGameId(2L)).thenReturn(60);
        when(gameSessionRepository.findMinSessionDurationByGameId(2L)).thenReturn(30);

        mockMvc.perform(post("/statistics/compare")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gameIds)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalGamesCompared").value(2))
                .andExpect(jsonPath("$.overallTotalSessions").value(5)) // 3 sesiones + 2 sesiones
                .andExpect(jsonPath("$.overallTotalTimeInMinutes").value(270)) // 180 + 90
                .andExpect(jsonPath("$.averageSessionDurationAcrossGames").value(54)); // 270 / 5 = 54
    }
}
