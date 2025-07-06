package com.egabi.university.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ApiError {
    private LocalDateTime timestamp;
    private int status;
    private String error;       // HTTP status reason, e.g., "Not Found"
    private String message;     // human message, e.g., "Course with code X not found"
    private String path;        // the request path
    private String errorCode;   // your custom code, e.g., COURSE_NOT_FOUND
}
