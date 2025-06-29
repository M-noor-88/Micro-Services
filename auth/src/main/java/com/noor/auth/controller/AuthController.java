package com.noor.auth.controller;

import com.noor.auth.dto.AuthRequest;
import com.noor.auth.dto.AuthResponse;
import com.noor.auth.dto.UserDto;
import com.noor.auth.model.User;
import com.noor.auth.service.JwtService;
import com.noor.auth.service.UserService;
import com.noor.auth.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (user.getRole() == Role.TRAINER) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "You are not allowed to register as TRAINER via this endpoint."));
        }

        return ResponseEntity.ok(userService.save(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        Optional<User> user = userService.findByName(request.getName());

        if (user.isPresent() && new BCryptPasswordEncoder().matches(request.getPassword(), user.get().getPassword())) {
            String token = jwtService.generateToken(user.get());
            return ResponseEntity.ok(new AuthResponse(token));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }


    @PostMapping("/admin/register-trainer")
    public ResponseEntity<?> registerTrainer(@RequestHeader("Authorization") String authHeader,
                                             @RequestBody User trainer) {
        // Extract token and check role
        String token = authHeader.replace("Bearer ", "");
        String role = jwtService.extractRole(token);

        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only ADMIN can register trainers");
        }

        trainer.setRole(Role.TRAINER); // Force role to TRAINER
        User saved = userService.save(trainer);

        return ResponseEntity.ok(saved);
    }


    // ✅ New: Get user by ID
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> userOpt = userService.findById(id);
        if (userOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = userOpt.get();
        return ResponseEntity.ok(new UserDto(user.getId(), user.getName(), user.getRole().name()));
    }

    // ✅ New: Validate user role
    @GetMapping("/validate-role")
    public boolean hasRoleFromToken(@RequestHeader("Authorization") String authHeader,
                                    @RequestParam String role) {
        String token = authHeader.replace("Bearer ", "");
        String extractedRole = jwtService.extractRole(token);
        return extractedRole.equalsIgnoreCase(role);
    }


    @GetMapping("/has-role")
    public boolean hasRole(@RequestHeader("Authorization") String authHeader,
                           @RequestParam String role) {
        String token = authHeader.replace("Bearer ", "");
        String extractedRole = jwtService.extractRole(token);
        return extractedRole.equalsIgnoreCase(role);
    }

    @GetMapping("/extract-id")
    public Long extractUserId(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        return jwtService.extractUserId(token);
    }

}
