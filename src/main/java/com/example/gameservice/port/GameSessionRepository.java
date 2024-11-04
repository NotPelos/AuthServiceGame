package com.example.gameservice.port;

import com.example.gameservice.domain.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, Long> {
    // Método para encontrar todas las sesiones de un juego específico
    List<GameSession> findByGameId(Long gameId);

    // Método opcional para ordenar las sesiones por fecha
    List<GameSession> findByGameIdOrderBySessionDateDesc(Long gameId);   
	
	List<GameSession> findByUserIdAndSessionDateBetween(Long userId, LocalDate startDate, LocalDate endDate);
	
	// Método para contar el número total de sesiones para un juego
    long countByGameId(Long gameId);

    // Método para sumar el tiempo total jugado en todas las sesiones de un juego específico
    @Query("SELECT SUM(gs.durationInMinutes) FROM GameSession gs WHERE gs.game.id = :gameId")
    Integer findTotalTimeByGameId(Long gameId);

    // Método para obtener la sesión con la duración máxima para un juego específico
    @Query("SELECT MAX(gs.durationInMinutes) FROM GameSession gs WHERE gs.game.id = :gameId")
    Integer findMaxSessionDurationByGameId(Long gameId);

    // Método para obtener la sesión con la duración mínima para un juego específico
    @Query("SELECT MIN(gs.durationInMinutes) FROM GameSession gs WHERE gs.game.id = :gameId")
    Integer findMinSessionDurationByGameId(Long gameId);
}
