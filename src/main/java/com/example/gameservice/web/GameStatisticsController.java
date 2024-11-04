package com.example.gameservice.web;

import com.example.gameservice.application.GenerateStatisticsUseCase;
import com.example.gameservice.dto.GameStatsDTO;
import com.example.gameservice.dto.ComparativeGameStatsDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statistics")
@AllArgsConstructor
public class GameStatisticsController {

    private final GenerateStatisticsUseCase generateStatisticsUseCase;

    @GetMapping("/game/{gameId}")
    public ResponseEntity<GameStatsDTO> getGameStats(@PathVariable Long gameId) {
        GameStatsDTO stats = generateStatisticsUseCase.getGameStats(gameId);
        return ResponseEntity.ok(stats);
    }

    @PostMapping("/compare")
    public ResponseEntity<ComparativeGameStatsDTO> compareGameStats(@RequestBody List<Long> gameIds) {
        ComparativeGameStatsDTO stats = generateStatisticsUseCase.getComparativeStats(gameIds);
        return ResponseEntity.ok(stats);
    }
}
