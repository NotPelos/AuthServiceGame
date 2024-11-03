package com.example.gameservice.port;

import com.example.gameservice.domain.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;
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
}
