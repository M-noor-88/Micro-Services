package com.noor.userAuth.controller;


import com.noor.userAuth.dto.*;
import com.noor.userAuth.entity.Role;
import com.noor.userAuth.entity.User;
import com.noor.userAuth.repository.UserRepository;
import com.noor.userAuth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class UserController {

    private final AuthService service;
    private final UserRepository repo;

    public UserController(AuthService service, UserRepository repo) {
        this.service = service;
        this.repo = repo;
    }

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest req,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        if (req.getRole() == Role.TRAINER) {
            System.out.println(userDetails);
            if (userDetails == null || userDetails.getAuthorities().stream()
                    .noneMatch(a -> a.getAuthority().equals("ADMIN"))) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only ADMIN can create TRAINERS");
            }
        }

        return service.register(req);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest req) {
        return service.login(req);
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        return repo.findById(id).orElseThrow();
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody RegisterRequest req) {
        User user = repo.findById(id).orElseThrow();
        user.setName(req.getName());
        return repo.save(user);
    }

    @GetMapping("/trainers")
    public List<User> getTrainers() {
        return repo.findAll().stream().filter(u -> u.getRole() == Role.TRAINER).toList();
    }

    @GetMapping("/learners")
    public List<User> getLearners() {
        return repo.findAll().stream().filter(u -> u.getRole() == Role.LEARNER).toList();
    }
}
