package com.egabi.university.service.authentication;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    
    // ================================================================
    // Token Generation and Validation Methods
    // ================================================================
    
    /**
     * Generates a JWT token for the given user details.
     *
     * @param userDetails the user details for which the token is generated
     * @return the generated JWT token
     */
    String generateToken(UserDetails userDetails);
    
    /**
     * Generates a JWT token for the given user details with additional claims.
     *
     * @param userDetails the user details for which the token is generated
     * @param extraClaims additional claims to include in the token
     * @return the generated JWT token
     */
    String generateToken(UserDetails userDetails, Map<String, Object> extraClaims);
    
    /**
     * Validates the JWT token.
     *
     * @param token       the JWT token
     * @param userDetails the user details to validate against
     * @return true if the token is valid for the given user email, false otherwise
     */
    boolean isTokenValid(String token, UserDetails userDetails);
    
    // ================================================================
    // Claims Extraction Methods
    // ================================================================
    
    /**
     * Extracts all claims from the JWT token.
     *
     * @param token the JWT token
     * @return the claims extracted from the token
     */
    Claims extractAllClaims(String token);
    
    /**
     * Extracts a specific claim from the JWT token.
     *
     * @param token          the JWT token
     * @param claimsResolver a function to resolve the claim from the Claims object
     * @param <T>            the type of the claim to be extracted
     * @return the extracted claim
     */
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    
    /**
     * Extracts the username from the JWT token.
     *
     * @param token the JWT token
     * @return the username extracted from the token
     */
    String extractUsername(String token);
}
