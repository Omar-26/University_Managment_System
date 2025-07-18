package com.egabi.university.service.authentication;

import com.egabi.university.dto.authentication.request.AuthenticationRequest;
import com.egabi.university.dto.authentication.request.RegistrationRequest;
import com.egabi.university.dto.authentication.response.AuthenticationResponse;
import com.egabi.university.entity.authentication.User;
import com.egabi.university.repository.UserRepository;
import com.egabi.university.service.authentication.registration.RegistrationHandler;
import com.egabi.university.service.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Default implementation of {@link AuthenticationService}.
 * Provides methods for user registration and authentication.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    
    private final UserRepository userRepository;
    private final ValidationService validationService;
    private final JwtService jwtService;
    private final List<RegistrationHandler> registrationHandlers;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public AuthenticationResponse register(RegistrationRequest registrationRequest) {
        // Validate Email , Password, and Role
        
        // 1. Check if a user with the provided email already exists
        validationService.assertUserEmailUnique(registrationRequest.getEmail());
        
        // 2.Validate the password meets the required criteria
        validationService.assertUserPasswordValid(registrationRequest.getPassword());
        
        // 3. Validate the role of the user
        validationService.assertUserRoleValid(registrationRequest.getRole().name());
        
        // Create a new user entity
        User user = User.builder()
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .role(registrationRequest.getRole())
                .locked(false)
                .enabled(true)
                .build();
        
        // Save the user to the repository
        userRepository.save(user);
        
        // Find the appropriate registration handler based on the user's role
        registrationHandlers.stream()
                .filter(handler -> handler.supports(user.getRole()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No handler found for role: " + user.getRole()))
                .handleRegistration(registrationRequest, user);
        
        // Generate JWT token for the registered user
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        // Authenticate the user using the provided email and password
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                ));
        
        // if authentication is successful, retrieve the user details
        User user = userRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with email: " + authenticationRequest.getEmail()
                ));
        
        // Generate JWT token for the authenticated user
        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(user))
                .build();
    }
}
