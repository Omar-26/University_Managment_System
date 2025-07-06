package com.egabi.university.exception;

import org.springframework.http.HttpStatus;

public abstract class ApiException extends RuntimeException {
    public ApiException(String message) {
        super(message);
    }
    
    public abstract HttpStatus getStatus();
    
    public abstract String getErrorCode();
}

//NotFoundException → Resource not found.
//BadRequestException → User provides invalid input not caught by validation
//ConflictException → Duplicate keys, duplicate usernames, same unique code
//UnauthorizedException → Login required but missing or invalid token
//ForbiddenException → Authenticated but not allowed to do the action