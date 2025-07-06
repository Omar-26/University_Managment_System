package com.egabi.university.service;

import com.egabi.university.dto.CourseDTO;

import java.util.List;

public interface CourseService {
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
     * @param courseDTO the course data transfer object containing updated course details
     * @return the updated CourseDTO object
     */
    CourseDTO updateCourse(CourseDTO courseDTO);
    
    /**
     * Delete a course by its code.
     *
     * @param code the code of the course to be deleted
     */
    void deleteCourse(String code);
}
