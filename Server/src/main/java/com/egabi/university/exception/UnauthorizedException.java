package com.egabi.university.exception;

import org.springframework.http.HttpStatus;

/**
 * 401 Unauthorized — authentication failed.
 * Example: Missing or invalid token.
 */
public class UnauthorizedException extends ApiException {
    public UnauthorizedException(String message) {
        super(message);
    }
    
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.UNAUTHORIZED;
    }
    
    @Override
    public String getErrorCode() {
        return "UNAUTHORIZED";
    }
}