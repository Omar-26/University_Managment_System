package com.egabi.university.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a requested resource is not found.
 * Maps to HTTP 404 Not Found.
 */
public class NotFoundException extends ApiException {
    
    private final String errorCode;
    
    /**
     * Constructs a new NotFoundException with the specified message and error code.
     *
     * @param message   detailed message
     * @param errorCode custom error code
     */
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
        return errorCode;
    }
}
