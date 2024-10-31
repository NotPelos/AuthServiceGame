package com.example.authservice.web;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.authservice.application.RegisterUserUseCase;
import com.example.authservice.domain.User;
import com.example.authservice.infrastructure.security.JwtUtil;
import com.example.authservice.infrastructure.security.MyUserDetailsService;

import lombok.AllArgsConstructor;
import lombok.Data;


@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody UserDTO userDTO) {
        User user = registerUserUseCase.register(userDTO.getUsername(), userDTO.getEmail(), userDTO.getPassword());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(jwt);
    }

    @Data
    static class UserDTO {
        private String username;
        private String email;
        private String password;
    }

    @Data
    static class AuthRequest {
        private String username;
        private String password;
    }
}
