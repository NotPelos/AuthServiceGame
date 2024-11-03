package com.example.gameservice.application;

import com.example.gameservice.domain.GameSession;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class GameReportDTO {
    private int totalSessions;
    private int totalHours;
    private List<GameSession> sessions;
}
