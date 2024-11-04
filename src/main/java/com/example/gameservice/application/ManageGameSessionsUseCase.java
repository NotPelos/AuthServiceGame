package com.example.gameservice.application;

import com.example.gameservice.domain.GameSession;
import com.example.gameservice.port.GameSessionRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ManageGameSessionsUseCase {

    private final GameSessionRepository gameSessionRepository;
    private final GenerateNotificationUseCase generateNotificationUseCase;

    // Añadir una nueva sesión de juego
    public GameSession addGameSession(GameSession gameSession) {
        GameSession savedSession = gameSessionRepository.save(gameSession);
        generateNotificationUseCase.generateSessionCompletionNotification(savedSession);
        return savedSession;
    }

    // Listar todas las sesiones de un juego
    public List<GameSession> listGameSessions(Long gameId) {
        return gameSessionRepository.findByGameId(gameId);
    }
    
    // Actualizar una sesión de juego existente
    public GameSession updateGameSession(Long sessionId, GameSession updatedSession) {
        return gameSessionRepository.findById(sessionId)
            .map(existingSession -> {
                existingSession.setDurationInMinutes(updatedSession.getDurationInMinutes());
                existingSession.setScore(updatedSession.getScore());
                existingSession.setLevel(updatedSession.getLevel());
                existingSession.setAchievements(updatedSession.getAchievements());
                existingSession.setSessionNotes(updatedSession.getSessionNotes());
                return gameSessionRepository.save(existingSession);
            })
            .orElseThrow(() -> new EntityNotFoundException("Sesión no encontrada"));
    }

    // Eliminar una sesión de juego por su ID
    public void deleteGameSession(Long sessionId) {
        if (!gameSessionRepository.existsById(sessionId)) {
            throw new EntityNotFoundException("Sesión no encontrada con ID: " + sessionId);
        }
        gameSessionRepository.deleteById(sessionId);
    }
}
