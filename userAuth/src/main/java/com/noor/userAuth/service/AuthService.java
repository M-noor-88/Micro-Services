package com.noor.userAuth.service;


import com.noor.userAuth.dto.AuthResponse;
import com.noor.userAuth.dto.LoginRequest;
import com.noor.userAuth.dto.RegisterRequest;
import com.noor.userAuth.entity.User;
import com.noor.userAuth.repository.UserRepository;
import com.noor.userAuth.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    public AuthResponse register(RegisterRequest req) {
//        if (repo.findByEmail(req.getEmail()).isPresent()) {
//            throw new RuntimeException("Email already used");
//        }
//
//        User user = User.builder()
//                .name(req.getName())
//                .email(req.getEmail())
//                .password(encoder.encode(req.getPassword()))
//                .role(req.getRole())
//                .build();
//        repo.save(user);
//        return new AuthResponse(jwt.generateToken(user.getEmail()));

        try {
            if (repo.findByEmail(req.getEmail()).isPresent()) {
                throw new RuntimeException("Email already used");
            }

            User user = User.builder()
                    .name(req.getName())
                    .email(req.getEmail())
                    .password(encoder.encode(req.getPassword()))
                    .role(req.getRole())
                    .build();

            repo.save(user);

            return new AuthResponse(jwt.generateToken(user.getEmail()));
        } catch (Exception e) {
            // Optional: rethrow or log
            throw new RuntimeException("Registration failed: " + e.getMessage(), e);
        }
    }

    public AuthResponse login(LoginRequest req) {
        User user = repo.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!encoder.matches(req.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return new AuthResponse(jwt.generateToken(user.getEmail()));
    }
}

