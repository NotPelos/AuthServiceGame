package com.example.authservice.port;

import com.example.authservice.domain.User;

public interface UserRepository {
    User save(User user);
    User findByUsername(String username);
}
