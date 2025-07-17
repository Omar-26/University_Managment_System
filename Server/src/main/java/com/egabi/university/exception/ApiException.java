package com.egabi.university.exception;

import org.springframework.http.HttpStatus;

/**
 * Base class for all custom API exceptions.
 * <p>
 * Extends {@link RuntimeException} and provides a standard contract
 * for HTTP status and error codes.
 * Used to standardize error handling across the application.
 * </p>
 */
public abstract class ApiException extends RuntimeException {
    
    /**
     * Constructs a new ApiException with the specified message.
     *
     * @param message the detail message
     */
    public ApiException(String message) {
        super(message);
    }
    
    /**
     * Returns the associated HTTP status for this exception.
     *
     * @return HTTP status
     */
    public abstract HttpStatus getStatus();
    
    /**
     * Returns an application-specific error code.
     *
     * @return custom error code string
     */
    public abstract String getErrorCode();
}


//NotFoundException → Resource not found.
//BadRequestException → User provides invalid input not caught by validation
//ConflictException → Duplicate keys, duplicate usernames, same unique code
//UnauthorizedException → Login required but missing or invalid token
//ForbiddenException → Authenticated but not allowed to do the action

//401 Unauthorized means:
//        “Authentication failed: the credentials are missing, invalid, or do not match any user.”
//
//UsernameNotFoundException and BadCredentialsException both mean bad login → same semantics → 401.
//
//        403 Forbidden is only for:
//        “You are authenticated but your role/permission doesn’t allow access.”
//Example: A student tries to access an admin-only resource — then it’s 403.