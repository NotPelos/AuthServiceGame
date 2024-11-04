package com.example.gameservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComparativeGameStatsDTO {
    private List<GameStatsDTO> gameStatsList; // Lista de estadísticas para cada juego comparado
    private int totalGamesCompared;           // Número total de juegos en la comparación
    private int overallTotalSessions;         // Total de sesiones combinadas de todos los juegos
    private int overallTotalTimeInMinutes;    // Tiempo total combinado en minutos
    private double averageSessionDurationAcrossGames; // Promedio de duración de sesión en todos los juegos
}
