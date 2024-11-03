package com.example.gameservice.application;

import com.example.gameservice.domain.Game;
import com.example.gameservice.domain.GameSession;
import com.example.gameservice.port.GameSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class ManageGameSessionsUseCaseTest {

    @Mock
    private GameSessionRepository gameSessionRepository;

    @InjectMocks
    private ManageGameSessionsUseCase manageGameSessionsUseCase;
    
    @Mock
    private GenerateNotificationUseCase generateNotificationUseCase; // Agregar el mock para GenerateNotificationUseCase

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddGameSession() {
        // Crear un juego de prueba y asignarlo a la sesión
        Game game = new Game();
        game.setId(1L);
        game.setTitle("Juego de Prueba");

        GameSession session = new GameSession(null, game, LocalDateTime.now(), 60); // Asignar el juego aquí
        when(gameSessionRepository.save(session)).thenReturn(session);

        GameSession savedSession = manageGameSessionsUseCase.addGameSession(session);

        assertEquals(session.getSessionDate(), savedSession.getSessionDate());
        assertEquals(session.getDurationInMinutes(), savedSession.getDurationInMinutes());
        verify(gameSessionRepository, times(1)).save(session);
        verify(generateNotificationUseCase, times(1)).generateSessionCompletionNotification(session); // Verificar que se llame a generateNotificationUseCase
    }

    @Test
    void testListGameSessions() {
        Long gameId = 1L;
        List<GameSession> sessions = Arrays.asList(
            new GameSession(1L, null, LocalDateTime.now(), 60),
            new GameSession(2L, null, LocalDateTime.now(), 120)
        );
        when(gameSessionRepository.findByGameId(gameId)).thenReturn(sessions);

        List<GameSession> result = manageGameSessionsUseCase.listGameSessions(gameId);

        assertEquals(2, result.size());
        assertEquals(60, result.get(0).getDurationInMinutes());
        verify(gameSessionRepository, times(1)).findByGameId(gameId);
    }

    @Test
    void testListGameSessionsEmpty() {
        Long gameId = 1L;
        when(gameSessionRepository.findByGameId(gameId)).thenReturn(Collections.emptyList());

        List<GameSession> result = manageGameSessionsUseCase.listGameSessions(gameId);

        assertTrue(result.isEmpty());
        verify(gameSessionRepository, times(1)).findByGameId(gameId);
    }
}
