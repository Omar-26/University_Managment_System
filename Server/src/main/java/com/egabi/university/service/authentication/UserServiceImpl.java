package com.egabi.university.service.authentication;

import com.egabi.university.entity.authentication.User;
import com.egabi.university.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Default implementation of {@link UserService}.
 * Provides the functionality to load user details by username.
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    
    /**
     * Loads user details by username (email).
     *
     * @param username the email of the user to be loaded
     * @return User object containing user details
     * @throws UsernameNotFoundException if no user is found with the given email
     */
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }
}
