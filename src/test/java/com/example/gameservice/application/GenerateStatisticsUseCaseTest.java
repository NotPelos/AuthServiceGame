package com.example.gameservice.application;

import com.example.gameservice.domain.Game;
import com.example.gameservice.domain.GameSession;
import com.example.gameservice.dto.GameStatsDTO;
import com.example.gameservice.dto.ComparativeGameStatsDTO;
import com.example.gameservice.port.GameRepository;
import com.example.gameservice.port.GameSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GenerateStatisticsUseCaseTest {

    @Mock
    private GameSessionRepository gameSessionRepository;

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GenerateStatisticsUseCase generateStatisticsUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetGameStats() {
        Long gameId = 1L;
        Game game = new Game(gameId, null, "Juego de Prueba", "Descripción", "Aventura");
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(gameSessionRepository.countByGameId(gameId)).thenReturn(3L);
        when(gameSessionRepository.findTotalTimeByGameId(gameId)).thenReturn(180);
        when(gameSessionRepository.findMaxSessionDurationByGameId(gameId)).thenReturn(120);
        when(gameSessionRepository.findMinSessionDurationByGameId(gameId)).thenReturn(30);

        GameStatsDTO stats = generateStatisticsUseCase.getGameStats(gameId);

        assertEquals(3, stats.getTotalSessions());
        assertEquals(180, stats.getTotalTimeInMinutes());
        assertEquals(60, stats.getAverageSessionDuration());
        assertEquals(120, stats.getMaxSessionDuration());
        assertEquals(30, stats.getMinSessionDuration());

        verify(gameRepository, times(1)).findById(gameId);
        verify(gameSessionRepository, times(1)).countByGameId(gameId);
        verify(gameSessionRepository, times(1)).findTotalTimeByGameId(gameId);
        verify(gameSessionRepository, times(1)).findMaxSessionDurationByGameId(gameId);
        verify(gameSessionRepository, times(1)).findMinSessionDurationByGameId(gameId);
    }

    @Test
    void testGetComparativeStats() {
        Long gameId1 = 1L;
        Long gameId2 = 2L;

        Game game1 = new Game(gameId1, null, "Juego 1", "Descripción 1", "Aventura");
        Game game2 = new Game(gameId2, null, "Juego 2", "Descripción 2", "Acción");

        when(gameRepository.findById(gameId1)).thenReturn(Optional.of(game1));
        when(gameRepository.findById(gameId2)).thenReturn(Optional.of(game2));

        when(gameSessionRepository.countByGameId(gameId1)).thenReturn(3L);
        when(gameSessionRepository.findTotalTimeByGameId(gameId1)).thenReturn(180);
        when(gameSessionRepository.findMaxSessionDurationByGameId(gameId1)).thenReturn(120);
        when(gameSessionRepository.findMinSessionDurationByGameId(gameId1)).thenReturn(30);

        when(gameSessionRepository.countByGameId(gameId2)).thenReturn(2L);
        when(gameSessionRepository.findTotalTimeByGameId(gameId2)).thenReturn(90);
        when(gameSessionRepository.findMaxSessionDurationByGameId(gameId2)).thenReturn(60);
        when(gameSessionRepository.findMinSessionDurationByGameId(gameId2)).thenReturn(30);

        ComparativeGameStatsDTO comparativeStats = generateStatisticsUseCase.getComparativeStats(Arrays.asList(gameId1, gameId2));

        assertEquals(2, comparativeStats.getTotalGamesCompared());
        assertEquals(5, comparativeStats.getOverallTotalSessions());
        assertEquals(270, comparativeStats.getOverallTotalTimeInMinutes());
        assertEquals(54, comparativeStats.getAverageSessionDurationAcrossGames());

        verify(gameRepository, times(1)).findById(gameId1);
        verify(gameRepository, times(1)).findById(gameId2);
        verify(gameSessionRepository, times(1)).countByGameId(gameId1);
        verify(gameSessionRepository, times(1)).countByGameId(gameId2);
    }
}
