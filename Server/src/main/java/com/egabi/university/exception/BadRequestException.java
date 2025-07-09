package com.egabi.university.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when the user provides invalid input.
 * Maps to HTTP 400 Bad Request.
 */
public class BadRequestException extends ApiException {
    
    private final String errorCode;
    
    public BadRequestException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.BAD_REQUEST;
    }
    
    @Override
    public String getErrorCode() {
        return errorCode;
    }
}
