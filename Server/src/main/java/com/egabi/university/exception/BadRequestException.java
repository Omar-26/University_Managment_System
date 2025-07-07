package com.egabi.university.exception;

import org.springframework.http.HttpStatus;

/**
 * 400 Bad Request — client sent invalid data.
 * Example: Required field missing, bad JSON, wrong format.
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
        return this.errorCode;
    }
}