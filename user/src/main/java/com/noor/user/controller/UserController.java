package com.noor.user.controller;


import com.noor.user.Entity.*;
import com.noor.user.Service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return userService.login(user.getName(), user.getPassword())
                .map(u -> "Login successful for user: " + u.getName())
                .orElse("Invalid credentials");
    }

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PutMapping("/users/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @GetMapping("/trainers")
    public List<User> getTrainers() {
        return userService.getByRole(Role.TRAINER);
    }

    @GetMapping("/learners")
    public List<User> getLearners() {
        return userService.getByRole(Role.LEARNER);
    }
}
