package com.egabi.university.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when authentication is required but missing or invalid.
 * Maps to HTTP 401 Unauthorized.
 */
public class UnauthorizedException extends ApiException {
    
    private final String errorCode;
    
    public UnauthorizedException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
    
    @Override
    public String getErrorCode() {
        return errorCode;
    }
}
