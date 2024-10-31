package com.example.authservice.application;

import org.springframework.stereotype.Service;

import com.example.authservice.domain.User;
import com.example.authservice.port.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RegisterUserUseCase {

    private final UserRepository userRepository;

    public User register(String username, String email, String password) {
        User user = new User(null, username, email, password);
        return userRepository.save(user);
    }
}

