package com.egabi.university.exception;

import org.springframework.http.HttpStatus;

/**
 * 409 Conflict — request causes a conflict.
 * Example: Trying to insert a duplicate course code.
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
        return this.errorCode;
    }
}
