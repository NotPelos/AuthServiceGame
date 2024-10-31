package com.example.gameservice.port;

import com.example.gameservice.domain.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    // Podemos añadir consultas personalizadas aquí si es necesario
}
