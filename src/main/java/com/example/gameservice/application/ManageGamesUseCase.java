package com.example.gameservice.application;

import com.example.gameservice.domain.Game;
import com.example.gameservice.port.GameRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ManageGamesUseCase {

    private final GameRepository gameRepository;

    // Añadir un juego a la biblioteca
    public Game addGame(Game game) {
        if (game == null || game.getTitle() == null || game.getTitle().isEmpty()) {
            throw new IllegalArgumentException("El título del juego es obligatorio");
        }
        return gameRepository.save(game);
    }

    // Eliminar un juego por su ID
    public void deleteGame(Long gameId) {
        if (!gameRepository.existsById(gameId)) {
            throw new EntityNotFoundException("Juego no encontrado con ID: " + gameId);
        }
        gameRepository.deleteById(gameId);
    }

    // Listar todos los juegos de la biblioteca
    public List<Game> listGames() {
        return gameRepository.findAll();
    }
    
    //Buscar el juego por ID
    public Game getGameById(Long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("La ID " + gameId + " no ha sido encontrada"));
    }
    
    public Game updateGame(Long gameId, Game updatedGame) {
        return gameRepository.findById(gameId)
            .map(existingGame -> {
                existingGame.setTitle(updatedGame.getTitle());
                existingGame.setDescription(updatedGame.getDescription());
                existingGame.setGenre(updatedGame.getGenre());
                return gameRepository.save(existingGame);
            })
            .orElseThrow(() -> new EntityNotFoundException("La ID " + gameId + " no ha sido encontrada"));
    }
    
    // Buscar juegos por título
    public List<Game> findByTitle(String title) {
        return gameRepository.findByTitle(title);
    }
    
 // Buscar juegos por género
    public List<Game> findByGenre(String genre) {
        return gameRepository.findByGenre(genre);
    }

    // Buscar juegos por ID de usuario
    public List<Game> findByUserId(Long userId) {
        return gameRepository.findByUserId(userId);
    }

    // Buscar juegos por título y género
    public List<Game> findByTitleAndGenre(String title, String genre) {
        return gameRepository.findByTitleAndGenre(title, genre);
    }

    // Listar todos los juegos ordenados por título ascendente
    public List<Game> findAllByOrderByTitleAsc() {
        return gameRepository.findAllByOrderByTitleAsc();
    }
    
}
