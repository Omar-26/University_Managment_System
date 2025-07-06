package com.egabi.university.exception;

import org.springframework.http.HttpStatus;

/**
 * 403 Forbidden — authenticated but not allowed.
 * Example: Student tries to access admin-only data.
 */
public class ForbiddenException extends ApiException {
    public ForbiddenException(String message) {
        super(message);
    }
    
    @Override
    public HttpStatus getStatus() {
        return HttpStatus.FORBIDDEN;
    }
    
    @Override
    public String getErrorCode() {
        return "FORBIDDEN";
    }
}
