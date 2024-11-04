package com.example.gameservice.application;

import com.example.gameservice.domain.Game;
import com.example.gameservice.domain.GameSession;
import com.example.gameservice.port.GameSessionRepository;

import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

        GameSession session = new GameSession(null, game, LocalDateTime.now(), 60, 0, 0, 0, null);
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
            new GameSession(1L, null, LocalDateTime.now(), 60, 0, 0, 0, null),
            new GameSession(2L, null, LocalDateTime.now(), 120, 0, 0, 0, null)
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
    
    @Test
    void testUpdateGameSession() {
        Long sessionId = 1L;
        Game game = new Game();
        game.setId(1L);

        GameSession existingSession = new GameSession(sessionId, game, LocalDateTime.now(), 60, 0, 0, 0, null);
        GameSession updatedSession = new GameSession(sessionId, game, LocalDateTime.now(), 90, 0, 0, 0, null);

        when(gameSessionRepository.findById(sessionId)).thenReturn(Optional.of(existingSession));
        when(gameSessionRepository.save(any(GameSession.class))).thenReturn(updatedSession);

        GameSession result = manageGameSessionsUseCase.updateGameSession(sessionId, updatedSession);

        assertEquals(90, result.getDurationInMinutes());
        verify(gameSessionRepository, times(1)).save(existingSession);
    }

    @Test
    void testDeleteGameSession() {
        Long sessionId = 1L;
        when(gameSessionRepository.existsById(sessionId)).thenReturn(true);
        doNothing().when(gameSessionRepository).deleteById(sessionId);

        manageGameSessionsUseCase.deleteGameSession(sessionId);

        verify(gameSessionRepository, times(1)).deleteById(sessionId);
    }

    @Test
    void testDeleteGameSessionNotFound() {
        Long sessionId = 1L;
        when(gameSessionRepository.existsById(sessionId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> manageGameSessionsUseCase.deleteGameSession(sessionId));
        verify(gameSessionRepository, times(1)).existsById(sessionId);
    }
}
