package com.egabi.university.util;

/**
 * Centralized API path constants for the University application.
 * <p>
 * This utility class defines the base URL prefix for all REST endpoints
 * and groups resource-specific paths for consistency and reuse.
 * <p>
 * Example usage:
 * <pre>{@code
 * @RequestMapping(ApiPaths.FACULTIES)
 * }</pre>
 * <p>
 * This ensures that all controller routes stay consistent and easy to manage.
 */
public final class ApiPaths {
    
    /**
     * Base prefix for all API endpoints.
     * All specific resource paths build upon this base path.
     */
    public static final String BASE_API = "/api";
    
    /**
     * Path for Faculty-related endpoints.
     * Example: /api/faculties
     */
    public static final String FACULTIES = BASE_API + "/faculties";
    
    /**
     * Path for Department-related endpoints.
     * Example: /api/departments
     */
    public static final String DEPARTMENTS = BASE_API + "/departments";
    
    /**
     * Path for Level-related endpoints.
     * Example: /api/levels
     */
    public static final String LEVELS = BASE_API + "/levels";
    
    /**
     * Path for Course-related endpoints.
     * Example: /api/courses
     */
    public static final String COURSES = BASE_API + "/courses";
    
    /**
     * Path for Student-related endpoints.
     * Example: /api/students
     */
    public static final String STUDENTS = BASE_API + "/students";
    
    /**
     * Path for Instructor-related endpoints.
     * Example: /api/instructors
     */
    public static final String INSTRUCTORS = BASE_API + "/instructors";
    
    /**
     * Path for Enrollment-related endpoints.
     * Example: /api/enrollments
     */
    public static final String ENROLLMENTS = BASE_API + "/enrollments";
    
    /**
     * Private constructor to prevent instantiation.
     * This is a utility class.
     */
    private ApiPaths() {
        // Prevent instantiation
    }
}
