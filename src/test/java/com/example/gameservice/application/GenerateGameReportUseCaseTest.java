package com.example.gameservice.application;

import com.example.gameservice.domain.GameSession;
import com.example.gameservice.port.GameSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GenerateGameReportUseCaseTest {

    @Mock
    private GameSessionRepository gameSessionRepository;

    @InjectMocks
    private GenerateGameReportUseCase generateGameReportUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateReport() {
        Long userId = 1L;
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 1, 31);

        List<GameSession> sessions = Arrays.asList(
            new GameSession(1L, null, startDate.atStartOfDay(), 120, 0, 0, 0, null),
            new GameSession(2L, null, startDate.plusDays(1).atStartOfDay(), 90, 0, 0, 0, null)
        );

        when(gameSessionRepository.findByUserIdAndSessionDateBetween(userId, startDate, endDate)).thenReturn(sessions);

        GameReportDTO report = generateGameReportUseCase.generateReport(userId, startDate, endDate);

        assertEquals(2, report.getTotalSessions());
        assertEquals(3, report.getTotalHours());
        verify(gameSessionRepository, times(1)).findByUserIdAndSessionDateBetween(userId, startDate, endDate);
    }
}
