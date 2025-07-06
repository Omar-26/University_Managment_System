package com.egabi.university.exception;

import org.springframework.http.HttpStatus;

/**
 * 404 Not Found — the requested resource could not be found.
 * Example: Trying to access a course that does not exist.
 */
public class NotFoundException extends ApiException {
    private final String errorCode;
    
    public NotFoundException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
    
    @Override
    public String getErrorCode() {
        return this.errorCode;
    }
}
