// PlayerController.java
package com.example.authservice.web;

import com.example.authservice.domain.User;
import com.example.authservice.infrastructure.UserJpaRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/player")
@AllArgsConstructor
@PreAuthorize("hasRole('PLAYER')")
public class PlayerController {

    private final UserJpaRepository userRepository;

    // Endpoint para que el jugador vea su perfil
    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(Principal principal) {
        User user = userRepository.findByUsername(principal.getName());
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
}
