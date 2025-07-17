package com.egabi.university.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler for all REST controllers.
 * <p>
 * Handles custom {@link ApiException} types, Spring validation errors,
 * malformed JSON, database integrity violations, and all other uncaught exceptions.
 * <p>
 * Produces a consistent {@link ApiError} response body for the client.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /**
     * Handles all custom application exceptions.
     *
     * @param ex      the custom ApiException
     * @param request the HTTP request
     * @return a formatted {@link ApiError} response
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> handleApiException(ApiException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                ex.getStatus().value(),
                ex.getStatus().getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                ex.getErrorCode(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, ex.getStatus());
    }
    
    /**
     * Handles validation errors thrown by @Valid on request bodies.
     *
     * @param ex      the validation exception
     * @param request the HTTP request
     * @return a {@link ApiError} with detailed field error messages
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.toList());
        
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Validation failed: " + String.join("; ", errors),
                request.getRequestURI(),
                "VALIDATION_ERROR",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handles malformed JSON in request bodies.
     *
     * @param ex      the parsing exception
     * @param request the HTTP request
     * @return a {@link ApiError} describing the JSON format issue
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleInvalidFormat(HttpMessageNotReadableException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Malformed JSON: " + ex.getMostSpecificCause().getMessage(),
                request.getRequestURI(),
                "INVALID_JSON",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
    
    /**
     * Handles database constraint violations (e.g. unique constraints).
     *
     * @param ex      the database exception
     * @param request the HTTP request
     * @return a {@link ApiError} describing the conflict
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                "Database error: " + ex.getMostSpecificCause().getMessage(),
                request.getRequestURI(),
                "DATA_INTEGRITY_VIOLATION",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, HttpStatus.CONFLICT);
    }
    
    // handle bad credentials
    
    /**
     * Handles authentication errors, such as invalid credentials.
     *
     * @param ex      the authentication exception
     * @param request the HTTP request
     * @return a {@link ApiError} indicating authentication failure
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                "Authentication failed: " + ex.getMessage(),
                request.getRequestURI(),
                "AUTHENTICATION_ERROR",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
    }
    
    /**
     * Fallback for all unhandled exceptions.
     *
     * @param ex      any other exception
     * @param request the HTTP request
     * @return a generic {@link ApiError} response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage(),
                request.getRequestURI(),
                "GENERIC_ERROR",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
