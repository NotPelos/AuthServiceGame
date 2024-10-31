package com.example.gameservice.web;

import com.example.gameservice.application.ManageGameSessionsUseCase;
import com.example.gameservice.domain.GameSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gamesessions")
@AllArgsConstructor
public class GameSessionController {

    private final ManageGameSessionsUseCase manageGameSessionsUseCase;

    // Endpoint para añadir una nueva sesión de juego
    @PostMapping("/add")
    public ResponseEntity<GameSession> addGameSession(@Valid @RequestBody GameSession gameSession) {
        GameSession savedSession = manageGameSessionsUseCase.addGameSession(gameSession);
        return ResponseEntity.ok(savedSession);
    }

    // Endpoint para listar todas las sesiones de un juego por su ID
    @GetMapping("/list/{gameId}")
    public ResponseEntity<List<GameSession>> listGameSessions(@PathVariable Long gameId) {
        List<GameSession> sessions = manageGameSessionsUseCase.listGameSessions(gameId);
        return ResponseEntity.ok(sessions);
    }
}
