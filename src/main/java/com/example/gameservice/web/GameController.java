package com.example.gameservice.web;

import com.example.authservice.domain.User;
import com.example.authservice.port.UserRepository;
import com.example.gameservice.application.ManageGamesUseCase;
import com.example.gameservice.domain.Game;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/games")
@AllArgsConstructor
public class GameController {

    private final ManageGamesUseCase manageGamesUseCase;
    private final UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<Game> addGame(@Valid @RequestBody Game game, Principal principal) {
        String username = principal.getName();  // Extraer el nombre del usuario autenticado
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username));

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        game.setUser(user.get());  // Asignar el usuario al juego
        Game savedGame = manageGamesUseCase.addGame(game);
        return ResponseEntity.ok(savedGame);
    }

    // Endpoint para eliminar un juego por su ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        manageGamesUseCase.deleteGame(id);
        return ResponseEntity.noContent().build();
    }

    // Endpoint para listar todos los juegos
    @GetMapping("/list")
    public ResponseEntity<List<Game>> listGames() {
        List<Game> games = manageGamesUseCase.listGames();
        return ResponseEntity.ok(games);
    }
}
