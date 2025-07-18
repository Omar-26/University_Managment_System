package com.egabi.university.repository;

import com.egabi.university.entity.authentication.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * UserRepository interface for managing User entities.
 * Provides methods to find users by email and check existence of email.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    
    /**
     * Finds a User by their email address.
     *
     * @param username the email address of the user
     * @return an Optional containing the User if found, or empty if not found
     */
    Optional<User> findByEmail(String username);
    
    /**
     * Checks if a User with the given email exists, ignoring case.
     *
     * @param email the email address to check
     * @return true if a User with the given email exists, false otherwise
     */
    boolean existsByEmailIgnoreCase(String email);
}
