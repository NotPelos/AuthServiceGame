package com.example.gameservice.web;

import com.example.gameservice.application.ManageGameSessionsUseCase;
import com.example.gameservice.domain.GameSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/gamesessions")
@AllArgsConstructor
public class GameSessionController {

    private final ManageGameSessionsUseCase manageGameSessionsUseCase;

    @PostMapping
    public ResponseEntity<GameSession> addGameSession(@RequestBody GameSession gameSession) {
        GameSession savedSession = manageGameSessionsUseCase.addGameSession(gameSession);
        return ResponseEntity.ok(savedSession);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<List<GameSession>> listGameSessions(@PathVariable Long gameId) {
        List<GameSession> sessions = manageGameSessionsUseCase.listGameSessions(gameId);
        return ResponseEntity.ok(sessions);
    }

    // Endpoint para actualizar una sesión de juego
    @PutMapping("/update/{sessionId}")
    public ResponseEntity<GameSession> updateGameSession(@PathVariable Long sessionId, @RequestBody GameSession updatedSession) {
        GameSession modifiedSession = manageGameSessionsUseCase.updateGameSession(sessionId, updatedSession);
        return ResponseEntity.ok(modifiedSession);
    }

    // Endpoint para eliminar una sesión de juego
    @DeleteMapping("/delete/{sessionId}")
    public ResponseEntity<Void> deleteGameSession(@PathVariable Long sessionId) {
        manageGameSessionsUseCase.deleteGameSession(sessionId);
        return ResponseEntity.noContent().build();
    }
}
