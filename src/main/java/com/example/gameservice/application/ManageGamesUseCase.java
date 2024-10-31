package com.example.gameservice.application;

import com.example.gameservice.domain.Game;
import com.example.gameservice.port.GameRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ManageGamesUseCase {

    private final GameRepository gameRepository;

    // Añadir un juego a la biblioteca
    public Game addGame(Game game) {
        return gameRepository.save(game);
    }

    // Eliminar un juego por su ID
    public void deleteGame(Long gameId) {
        gameRepository.deleteById(gameId);
    }

    // Listar todos los juegos de la biblioteca
    public List<Game> listGames() {
        return gameRepository.findAll();
    }
}
