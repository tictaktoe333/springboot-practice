package com.example.task_manager.controller;

import com.example.task_manager.model.UserEntity;
import com.example.task_manager.repository.UserRepository;
import com.example.task_manager.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository repo;
    private final PasswordEncoder encoder;

    public AuthController(UserRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @PostMapping("/register")
    public UserEntity register(@RequestBody UserEntity user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return repo.save(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserEntity login) {
        return repo.findByUsername(login.getUsername())
                .filter(user -> encoder.matches(login.getPassword(), user.getPassword()))
                .map(user -> ResponseEntity.ok(JwtUtil.generateToken(user.getUsername())))
                .orElse(ResponseEntity.status(401).body("Invalid credentials"));
    }
}
