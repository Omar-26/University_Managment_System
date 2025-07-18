package com.egabi.university.config.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for JWT settings.
 * This class holds the properties required for JWT token generation and validation,
 * including the secret key and expiration time.
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    
    /**
     * JWT secret key used for signing tokens.
     * This key should be kept secret and secure.
     * It is used to verify the integrity of the JWT and ensure that it has not been tampered with.
     */
    private String secret;
    
    /**
     * JWT expiration time in milliseconds.
     * This defines how long the JWT is valid before it expires.
     * After this time, the token will no longer be accepted for authentication.
     */
    private long expiration;
}