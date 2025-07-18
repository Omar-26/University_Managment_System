package com.egabi.university.service.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

/**
 * Default implementation of {@link JwtService}.
 * Provides methods for generating, validating, and extracting claims from JWT tokens.
 */
@Service
public class JwtServiceImpl implements JwtService {
    
    // ================================================================
    // Configuration Properties
    // ================================================================
    
    /**
     * JWT secret key used for signing tokens.
     * This key should be kept secret and secure.
     * It is used to verify the integrity of the JWT and ensure that it has not been tampered with.
     */
    @Value("${jwt.secret}")
    private String secretKey;
    
    /**
     * JWT expiration time in milliseconds.
     * This defines how long the JWT is valid before it expires.
     * After this time, the token will no longer be accepted for authentication.
     */
    @Value("${jwt.expiration.time}")
    private long expirationTimeInMillis;
    
    // ================================================================
    // Token Generation and Validation Methods
    // ================================================================
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String generateToken(UserDetails userDetails) {
        return generateToken(userDetails, Map.of());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String generateToken(UserDetails userDetails, Map<String, Object> extraClaims) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeInMillis))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    
    // ================================================================
    // Claims Extraction Methods
    // ================================================================
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    // ================================================================
    // Helper methods
    // ================================================================
    
    /**
     * Returns the signing key used for JWT token generation and validation.
     *
     * @return the signing key
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token the JWT token
     * @return the expiration date of the token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    
    /**
     * Checks if the given JWT token is expired.
     *
     * @param token the JWT token to check
     * @return true if the token is expired, false otherwise
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}
