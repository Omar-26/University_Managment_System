package com.egabi.university.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a conflict occurs (e.g., duplicate key).
 * Maps to HTTP 409 Conflict.
 */
public class ConflictException extends ApiException {
    
    private final String errorCode;
    
    public ConflictException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.CONFLICT;
    }
    
    @Override
    public String getErrorCode() {
        return errorCode;
    }
}
