package com.egabi.university.repository;

import com.egabi.university.entity.authentication.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String username);
    
    boolean existsByEmailIgnoreCase(String email);
}
