package com.example.authservice.application;

import org.springframework.stereotype.Service;

import com.example.authservice.domain.Role;
import com.example.authservice.domain.User;
import com.example.authservice.port.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;

    public User register(String username, String email, String password) {
        if (userRepository.findByUsername(username) != null) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }
        User user = new User(null, username, email, password, Role.PLAYER);
        return userRepository.save(user);
    }
}

