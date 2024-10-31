package com.example.gameservice.port;

import com.example.gameservice.domain.Game;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    // Consultas para buscar por título, categoria, id, titlo&categoria, orden del filtro Asc/Desc
	List<Game> findByTitle(String title);
	
	List<Game> findByGenre(String genre);
	
	List<Game> findByUserId(Long userId);
	
	List<Game> findByTitleAndGenre(String title, String genre);
	
	List<Game> findAllByOrderByTitleAsc();
	List<Game> findAllByOrderByTitleDesc();

}
