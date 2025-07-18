package com.egabi.university.service.academic;

import com.egabi.university.dto.CourseDTO;

import java.util.List;

public interface CourseService {
    // ================================================================
    // CRUD Methods
    // ================================================================
    
    /**
     * Get all courses available in the system.
     *
     * @return a list of all courses as CourseDTO objects
     */
    List<CourseDTO> getAllCourses();
    
    /**
     * Get Course by code.
     *
     * @param code the code of the course
     * @return Course object if exists, null otherwise
     */
    CourseDTO getCourseByCode(String code);
    
    /**
     * Create a new course.
     *
     * @param courseDTO the course data transfer object containing course details
     * @return the created CourseDTO object
     */
    CourseDTO createCourse(CourseDTO courseDTO);
    
    /**
     * Update an existing course.
     *
     * @param code      the code of the course to be updated
     * @param courseDTO the course data transfer object containing updated course details
     * @return the updated CourseDTO object
     */
    CourseDTO updateCourse(String code, CourseDTO courseDTO);
    
    /**
     * Delete a course by its code.
     *
     * @param code the code of the course to be deleted
     */
    void deleteCourse(String code);
    
    // ================================================================
    // Business Logic Methods
    // ================================================================
    
    /**
     * Get all courses by department ID.
     *
     * @param departmentId the ID of the department
     * @return a list of CourseDTO objects associated with the specified department
     */
    List<CourseDTO> getCoursesByDepartmentId(Long departmentId);
    
    /**
     * Count all courses for a specific department.
     *
     * @param departmentId the ID of the department
     * @return the count of courses associated with the specified department
     */
    Long countCoursesByDepartmentId(Long departmentId);
}
