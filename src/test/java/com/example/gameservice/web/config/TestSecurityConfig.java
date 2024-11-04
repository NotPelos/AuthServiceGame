package com.example.gameservice.web.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@TestConfiguration
public class TestSecurityConfig {

    @Bean
    @Primary // bean como primario en el contexto de pruebas
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
