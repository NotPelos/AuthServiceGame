package com.example.gameservice.application;

import com.example.gameservice.domain.Game;
import com.example.gameservice.port.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

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
}
