package com.egabi.university.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when the user is authenticated but not allowed
 * to perform the action. Maps to HTTP 403 Forbidden.
 */
public class ForbiddenException extends ApiException {
    
    private final String errorCode;
    
    public ForbiddenException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
    
    @Override
    public String getErrorCode() {
        return errorCode;
    }
}
