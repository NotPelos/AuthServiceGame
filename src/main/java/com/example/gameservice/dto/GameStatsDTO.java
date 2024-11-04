package com.example.gameservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameStatsDTO {
    private Long gameId;
    private String gameTitle;
    private int totalSessions;           // Total de sesiones jugadas
    private int totalTimeInMinutes;      // Tiempo total jugado en minutos
    private double averageSessionDuration; // Duración promedio de las sesiones en minutos
    private int maxSessionDuration;      // Duración máxima de una sesión
    private int minSessionDuration;      // Duración mínima de una sesión
}
