package com.example.authapi.Controller;


import com.example.authapi.DTO.AuthRequest;
import com.example.authapi.DTO.TokenResponse;
import com.example.authapi.Entity.UserEntity;
import com.example.authapi.Repository.UserRepository;
import com.example.authapi.Util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest req) {
        // ВИПРАВЛЕНО: req.email() замість req.getEmail()
        if (userRepository.findByEmail(req.email()) != null) {
            return ResponseEntity.badRequest().body("Exists");
        }

        UserEntity user = UserEntity.builder()
                .email(req.email()) // ВИПРАВЛЕНО
                .passwordHash(passwordEncoder.encode(req.password())) // ВИПРАВЛЕНО: req.password()
                .build();

        userRepository.save(user);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        UserEntity user = userRepository.findByEmail(req.email());

        if (user == null || !passwordEncoder.matches(req.password(), user.getPasswordHash())) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(new TokenResponse(jwtUtil.generateToken(user.getEmail())));
    }
}