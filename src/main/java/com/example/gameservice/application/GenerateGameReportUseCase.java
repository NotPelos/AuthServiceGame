package com.example.gameservice.application;

import com.example.gameservice.domain.GameSession;
import com.example.gameservice.port.GameSessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class GenerateGameReportUseCase {

    private final GameSessionRepository gameSessionRepository;

    public GameReportDTO generateReport(Long userId, LocalDate startDate, LocalDate endDate) {
        List<GameSession> sessions = gameSessionRepository.findByUserIdAndSessionDateBetween(userId, startDate, endDate);

        int totalSessions = sessions.size();
        int totalHours = sessions.stream()
                                 .mapToInt(GameSession::getDurationInMinutes)
                                 .sum() / 60;

        return new GameReportDTO(totalSessions, totalHours, sessions);
    }
}
