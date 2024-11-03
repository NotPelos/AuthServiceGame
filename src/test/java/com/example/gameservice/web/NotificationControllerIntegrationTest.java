package com.example.gameservice.web;

import com.example.authservice.AuthServiceApplication;
import com.example.authservice.infrastructure.security.JwtUtil;
import com.example.gameservice.application.GenerateGameReportUseCase;
import com.example.gameservice.domain.Notification;
import com.example.gameservice.port.GameRepository;
import com.example.gameservice.port.GameSessionRepository;
import com.example.gameservice.port.NotificationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = AuthServiceApplication.class, properties = "spring.main.allow-bean-definition-overriding=true")
@ComponentScan(basePackages = {"com.example.authservice", "com.example.gameservice"})
@AutoConfigureMockMvc
@ActiveProfiles("test")
class NotificationControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NotificationRepository notificationRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private String tokenDePrueba;

    private Notification notification;
    
    @MockBean
    private GenerateGameReportUseCase generateGameReportUseCase;
    
    @MockBean
    private GameRepository gameRepository;
    
    @MockBean
    private GameSessionRepository gamesessionrepository;

    @BeforeEach
    void setUp() {
        notification = new Notification();
        notification.setUserId(1L);
        notification.setMessage("Nueva sesión completada.");
        notification.setRead(false);

        when(notificationRepository.findByUserIdAndReadFalse(1L)).thenReturn(List.of(notification));

        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_PLAYER"));
        tokenDePrueba = jwtUtil.generateToken("playeruser", authorities);
    }

    @Test
    void testGetUnreadNotifications() throws Exception {
        mockMvc.perform(get("/notifications/unread")
                .header("Authorization", "Bearer " + tokenDePrueba)
                .header("User-Id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(List.of(notification))));
    }
}
