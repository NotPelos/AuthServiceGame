package com.example.gameservice.application;

import com.example.gameservice.domain.Game;
import com.example.gameservice.port.GameRepository;

import jakarta.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class ManageGamesUseCaseTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private ManageGamesUseCase manageGamesUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddGame() {
        Game game = new Game(null, null, "Juego de Prueba", "Descripción", "Aventura");
        when(gameRepository.save(game)).thenReturn(game);

        Game savedGame = manageGamesUseCase.addGame(game);

        assertEquals(game, savedGame);
        verify(gameRepository, times(1)).save(game);
    }

    @Test
    void testListGames() {
        List<Game> games = Arrays.asList(
            new Game(1L, null, "Juego 1", "Descripción 1", "Aventura"),
            new Game(2L, null, "Juego 2", "Descripción 2", "Acción")
        );
        when(gameRepository.findAll()).thenReturn(games);

        List<Game> result = manageGamesUseCase.listGames();

        assertEquals(2, result.size());
        assertEquals("Juego 1", result.get(0).getTitle());
        verify(gameRepository, times(1)).findAll();
    }
    
    @Test
    void testFindByTitle() {
        String title = "Juego 1";
        List<Game> games = Arrays.asList(
            new Game(1L, null, title, "Descripción 1", "Aventura")
        );
        when(gameRepository.findByTitle(title)).thenReturn(games);

        List<Game> result = manageGamesUseCase.findByTitle(title);

        assertEquals(1, result.size());
        assertEquals(title, result.get(0).getTitle());
        verify(gameRepository, times(1)).findByTitle(title);
    }
    
    
    @Test
    void testFindByGenre() {
        String genre = "Aventura";
        List<Game> games = Arrays.asList(
            new Game(1L, null, "Juego 1", "Descripción 1", genre),
            new Game(2L, null, "Juego 2", "Descripción 2", genre)
        );
        when(gameRepository.findByGenre(genre)).thenReturn(games);

        List<Game> result = manageGamesUseCase.findByGenre(genre);

        assertEquals(2, result.size());
        assertEquals(genre, result.get(0).getGenre());
        verify(gameRepository, times(1)).findByGenre(genre);
    }
    
    
    @Test
    void testFindByUserId() {
        Long userId = 1L;
        List<Game> games = Arrays.asList(
            new Game(1L, null, "Juego 1", "Descripción 1", "Aventura")
        );
        when(gameRepository.findByUserId(userId)).thenReturn(games);

        List<Game> result = manageGamesUseCase.findByUserId(userId);

        assertEquals(1, result.size());
        verify(gameRepository, times(1)).findByUserId(userId);
    }
    
    
    @Test
    void testFindByTitleAndGenre() {
        String title = "Juego 1";
        String genre = "Aventura";
        List<Game> games = Arrays.asList(
            new Game(1L, null, title, "Descripción 1", genre)
        );
        when(gameRepository.findByTitleAndGenre(title, genre)).thenReturn(games);

        List<Game> result = manageGamesUseCase.findByTitleAndGenre(title, genre);

        assertEquals(1, result.size());
        assertEquals(title, result.get(0).getTitle());
        assertEquals(genre, result.get(0).getGenre());
        verify(gameRepository, times(1)).findByTitleAndGenre(title, genre);
    }
    
    
    @Test
    void testFindAllByOrderByTitleAsc() {
        List<Game> games = Arrays.asList(
            new Game(1L, null, "Juego A", "Descripción A", "Aventura"),
            new Game(2L, null, "Juego B", "Descripción B", "Acción")
        );
        when(gameRepository.findAllByOrderByTitleAsc()).thenReturn(games);

        List<Game> result = manageGamesUseCase.findAllByOrderByTitleAsc();

        assertEquals(2, result.size());
        assertEquals("Juego A", result.get(0).getTitle());
        verify(gameRepository, times(1)).findAllByOrderByTitleAsc();
    }
    
    
    @Test
    void testGetGameById() {
        Long gameId = 1L;
        Game game = new Game(gameId, null, "Juego de Prueba", "Descripción", "Aventura");

        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));

        Game result = manageGamesUseCase.getGameById(gameId);

        assertEquals(game, result);
        verify(gameRepository, times(1)).findById(gameId);
    }
    
    @Test
    void testGetGameByIdNotFound() {
        Long gameId = 1L;

        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> manageGamesUseCase.getGameById(gameId));
        verify(gameRepository, times(1)).findById(gameId);
    }
}
