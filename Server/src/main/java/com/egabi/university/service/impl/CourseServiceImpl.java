package com.egabi.university.service.impl;

import com.egabi.university.dto.CourseDTO;
import com.egabi.university.entity.Course;
import com.egabi.university.entity.Department;
import com.egabi.university.entity.Level;
import com.egabi.university.exception.ConflictException;
import com.egabi.university.exception.NotFoundException;
import com.egabi.university.mapper.CourseMapper;
import com.egabi.university.repository.CourseRepository;
import com.egabi.university.service.CourseService;
import com.egabi.university.service.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Default implementation of {@link CourseService}.
 * Provides CRUD operations for managing courses.
 */
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final ValidationService validationService;
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<CourseDTO> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courseMapper.toDTOs(courses);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CourseDTO getCourseByCode(String code) {
        Course course = validationService.getCourseByCodeOrThrow(code);
        return courseMapper.toDTO(course);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CourseDTO createCourse(CourseDTO courseDTO) {
        // Check if the course code already exists (it shouldn't exist for creation)
        validationService.assertCourseExists(courseDTO.getCode(), false);
        
        // Map the DTO to the entity
        Course course = courseMapper.toEntity(courseDTO);
        
        // Validate department - level and save the course
        course = validateAndSaveCourse(course);
        
        // Return the created course DTO
        return courseMapper.toDTO(course);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public CourseDTO updateCourse(String code, CourseDTO courseDTO) {
        // Check if the course exists (it should exist for update)
        validationService.assertCourseExists(code, true);
        
        // Map the DTO to the entity
        Course updatedCourse = courseMapper.toEntity(courseDTO);
        updatedCourse.setCode(code);
        
        // Validate department - level and update the course
        updatedCourse = validateAndSaveCourse(updatedCourse);
        
        // Return the updated course DTO
        return courseMapper.toDTO(updatedCourse);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteCourse(String code) {
        // Check if the course exists (it should exist for deletion)
        Course course = validationService.getCourseByCodeOrThrow(code);
        
        // Check if the course has any enrollments and prevent deletion
        if (!course.getEnrollments().isEmpty())
            throw new ConflictException("Cannot delete course " + code
                    + " because it has enrollments", "COURSE_HAS_ENROLLMENTS");
        
        // Check if the course has any instructors and remove it from those instructors
        if (!course.getInstructors().isEmpty())
            course.getInstructors().forEach(instructor -> {
                instructor.getCourses().remove(course);
            });
        
        // Delete the course
        courseRepository.delete(course);
    }
    
    // ================================================================
    // Helper methods
    // ================================================================
    
    /**
     * Validates the course's level and department, and saves the course.
     *
     * @param course The course to validate and save.
     * @return The saved course.
     * @throws NotFoundException if the level or department is not found.
     */
    private Course validateAndSaveCourse(Course course) {
        // Validate level
        // 1. Check if the level is set
        Long levelId = Optional.ofNullable(course.getLevel()).map(Level::getId)
                .orElseThrow(() -> new NotFoundException("Course must have a level", "LEVEL_NOT_FOUND"));
        
        // 2. Check if the level exists
        Level level = validationService.getLevelByIdOrThrow(levelId);
        course.setLevel(level);
        
        // Validate department
        // 1. Check if the department is set
        Long departmentId = Optional.ofNullable(course.getDepartment()).map(Department::getId)
                .orElseThrow(() -> new NotFoundException("Course must have a department", "DEPARTMENT_NOT_FOUND"));
        
        // 2. Check if the department exists
        Department department = validationService.getDepartmentByIdOrThrow(departmentId);
        course.setDepartment(department);
        
        // Save the course
        return courseRepository.save(course);
    }
    
}
