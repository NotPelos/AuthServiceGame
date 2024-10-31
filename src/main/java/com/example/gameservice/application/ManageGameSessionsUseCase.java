package com.example.gameservice.application;

import com.example.gameservice.domain.GameSession;
import com.example.gameservice.port.GameSessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ManageGameSessionsUseCase {

    private final GameSessionRepository gameSessionRepository;

    // Añadir una nueva sesión de juego
    public GameSession addGameSession(GameSession gameSession) {
        return gameSessionRepository.save(gameSession);
    }

    // Listar todas las sesiones de un juego
    public List<GameSession> listGameSessions(Long gameId) {
        return gameSessionRepository.findByGameId(gameId);
    }
}
