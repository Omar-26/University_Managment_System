package com.egabi.university.service.authentication;

import com.egabi.university.entity.authentication.AuthenticationRequest;
import com.egabi.university.entity.authentication.AuthenticationResponse;
import com.egabi.university.entity.authentication.RegistrationRequest;

public interface AuthenticationService {
    
    /**
     * Registers a new user with the provided registration request.
     *
     * @param registrationRequest the request containing user registration details
     * @return a response indicating the result of the registration
     */
    AuthenticationResponse register(RegistrationRequest registrationRequest);
    
    /**
     * Authenticates a user with the provided authentication request.
     *
     * @param authenticationRequest the request containing user authentication details
     * @return a response indicating the result of the authentication
     */
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
}
