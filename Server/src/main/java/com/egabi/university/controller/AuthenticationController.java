package com.egabi.university.controller;

import com.egabi.university.dto.authentication.request.AuthenticationRequest;
import com.egabi.university.dto.authentication.request.RegistrationRequest;
import com.egabi.university.dto.authentication.response.AuthenticationResponse;
import com.egabi.university.service.authentication.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.egabi.university.util.ApiPaths.AUTH;

/**
 * REST Controller for handling authentication-related requests.
 * This controller is responsible for managing user authentication operations.
 */
@RestController
@RequestMapping(AUTH)
@RequiredArgsConstructor
public class AuthenticationController {
    
    private final AuthenticationService authenticationService;
    
    /**
     * Endpoint for user registration.
     * Accepts a RegistrationRequest object and returns an AuthenticationResponse.
     *
     * @param registrationRequest the request containing user registration details
     * @return ResponseEntity containing the authentication response
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody @Valid RegistrationRequest registrationRequest) {
        return ResponseEntity.ok(authenticationService.register(registrationRequest));
    }
    
    /**
     * Endpoint for user authentication.
     * Accepts an AuthenticationRequest object and returns an AuthenticationResponse.
     *
     * @param authenticationRequest the request containing user authentication details
     * @return ResponseEntity containing the authentication response
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }
}
