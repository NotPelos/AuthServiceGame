package com.example.gameservice.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class GameSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;  // Relación con el juego

    private LocalDateTime sessionDate;  // Fecha de la sesión

    @Min(1)
    private int durationInMinutes;  // Duración de la sesión en minutos

    // Nuevos campos de rendimiento
    private int score;
    private int level;
    private int achievements;
    private String sessionNotes;
}
