package com.example.authservice.infrastructure;

import com.example.authservice.domain.User;
import com.example.authservice.port.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long>, UserRepository {

    // Método personalizado para encontrar un usuario por nombre de usuario
    User findByUsername(String username);
}
