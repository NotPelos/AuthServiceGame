package com.example.gameservice.application;

import com.example.gameservice.domain.Game;
import com.example.gameservice.dto.GameStatsDTO;
import com.example.gameservice.dto.ComparativeGameStatsDTO;
import com.example.gameservice.port.GameRepository;
import com.example.gameservice.port.GameSessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GenerateStatisticsUseCase {

    private final GameSessionRepository gameSessionRepository;
    private final GameRepository gameRepository;

    // Método para obtener estadísticas de un juego específico
    public GameStatsDTO getGameStats(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("El juego no fue encontrado"));

        int totalSessions = (int) gameSessionRepository.countByGameId(gameId);
        
        Integer totalTimeInMinutesResult = gameSessionRepository.findTotalTimeByGameId(gameId);
        int totalTimeInMinutes = totalTimeInMinutesResult != null ? totalTimeInMinutesResult : 0;

        double averageSessionDuration = totalSessions > 0 ? (double) totalTimeInMinutes / totalSessions : 0;
        
        Integer maxSessionDurationResult = gameSessionRepository.findMaxSessionDurationByGameId(gameId);
        int maxSessionDuration = maxSessionDurationResult != null ? maxSessionDurationResult : 0;
        
        Integer minSessionDurationResult = gameSessionRepository.findMinSessionDurationByGameId(gameId);
        int minSessionDuration = minSessionDurationResult != null ? minSessionDurationResult : 0;

        return new GameStatsDTO(
                game.getId(),
                game.getTitle(),
                totalSessions,
                totalTimeInMinutes,
                averageSessionDuration,
                maxSessionDuration,
                minSessionDuration
        );
    }

    // Método para comparar estadísticas entre varios juegos
    public ComparativeGameStatsDTO getComparativeStats(List<Long> gameIds) {
        List<GameStatsDTO> gameStatsList = gameIds.stream()
                .map(this::getGameStats)
                .collect(Collectors.toList());

        int totalGamesCompared = gameStatsList.size();
        int overallTotalSessions = gameStatsList.stream().mapToInt(GameStatsDTO::getTotalSessions).sum();
        int overallTotalTimeInMinutes = gameStatsList.stream().mapToInt(GameStatsDTO::getTotalTimeInMinutes).sum();
        double averageSessionDurationAcrossGames = totalGamesCompared > 0 ? 
                (double) overallTotalTimeInMinutes / overallTotalSessions : 0;

        return new ComparativeGameStatsDTO(
                gameStatsList,
                totalGamesCompared,
                overallTotalSessions,
                overallTotalTimeInMinutes,
                averageSessionDurationAcrossGames
        );
    }
}
