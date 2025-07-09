package com.egabi.university.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Represents a standardized API error response.
 * Contains HTTP status, error message, timestamp, path, and custom error code.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    /**
     * HTTP status code.
     */
    private int status;
    
    /**
     * Short description of the error.
     */
    private String error;
    
    /**
     * Detailed error message.
     */
    private String message;
    
    /**
     * Request path that caused the error.
     */
    private String path;
    
    /**
     * Application-specific error code for identifying the error type.
     */
    private String errorCode;
    
    /**
     * Time when the error occurred.
     */
    private LocalDateTime timestamp = LocalDateTime.now();
}
