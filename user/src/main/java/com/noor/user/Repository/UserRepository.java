package com.noor.user.Repository;

import com.noor.user.Entity.User;
import com.noor.user.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);  // <-- Fix here
    List<User> findByRole(Role role);
}
