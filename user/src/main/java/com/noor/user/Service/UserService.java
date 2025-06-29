package com.noor.user.Service;

;
import com.noor.user.Entity.Role;
import com.noor.user.Entity.User;
import com.noor.user.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User register(User user) {
        return userRepo.save(user);
    }

    public Optional<User> login(String username, String password) {
        return userRepo.findByName(username)
                .filter(u -> u.getPassword().equals(password));
    }

    public Optional<User> getUser(Long id) {
        return userRepo.findById(id);
    }

    public User updateUser(Long id, User newData) {
        return userRepo.findById(id).map(user -> {
            user.setEmail(newData.getEmail());
            user.setName(newData.getName());
            return userRepo.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> getByRole(Role role) {
        return userRepo.findByRole(role);
    }
}
