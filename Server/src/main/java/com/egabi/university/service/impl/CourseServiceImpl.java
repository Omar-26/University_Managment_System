package com.egabi.university.service.impl;

import com.egabi.university.dto.CourseDTO;
import com.egabi.university.entity.Course;
import com.egabi.university.entity.Department;
import com.egabi.university.entity.Level;
import com.egabi.university.exception.ConflictException;
import com.egabi.university.exception.NotFoundException;
import com.egabi.university.mapper.CourseMapper;
import com.egabi.university.repository.CourseRepository;
import com.egabi.university.repository.DepartmentRepository;
import com.egabi.university.repository.LevelRepository;
import com.egabi.university.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final LevelRepository levelRepository;
    private final DepartmentRepository departmentRepository;
    
    @Override
    public List<CourseDTO> getAllCourses() {
        List<Course> courses = courseRepository.findAll();
        return courseMapper.toDTOs(courses);
    }
    
    @Override
    public CourseDTO getCourseByCode(String code) {
        Course course = courseRepository.findById(code)
                .orElseThrow(() -> new NotFoundException("Course with code " + code + " not found", "COURSE_NOT_FOUND"));
        return courseMapper.toDTO(course);
    }
    
    @Override
    public CourseDTO createCourse(CourseDTO courseDTO) {
        // Check that the code does not already exist
        if (courseRepository.existsByCodeIgnoreCase(courseDTO.getCode()))
            throw new ConflictException("Course code already exists", "COURSE_EXISTS");
        
        // Map the DTO to the entity
        Course course = courseMapper.toEntity(courseDTO);
        
        // Validate department - level and save the course
        course = validateAndSaveCourse(course);
        
        return courseMapper.toDTO(course);
    }
    
    @Override
    public CourseDTO updateCourse(CourseDTO courseDTO) {
        // Check if the course exists
        if (!courseRepository.existsByCodeIgnoreCase(courseDTO.getCode()))
            throw new NotFoundException("Course with code " + courseDTO.getCode() + " not found", "COURSE_NOT_FOUND");
        
        // Map the DTO to the entity
        Course updatedCourse = courseMapper.toEntity(courseDTO);
        
        // Validate department - level and update the course
        updatedCourse = validateAndSaveCourse(updatedCourse);
        
        return courseMapper.toDTO(updatedCourse);
    }
    
    @Override
    public void deleteCourse(String code) {
        Course course = courseRepository.findByCodeIgnoreCase((code))
                .orElseThrow(() -> new NotFoundException("Course with code " + code + " not found", "COURSE_NOT_FOUND"));
        
        // Check if the course has any enrollments
        if (!course.getEnrollments().isEmpty())
            throw new ConflictException("Cannot delete course with existing enrollments", "COURSE_HAS_ENROLLMENTS");
        
        courseRepository.delete(course);
    }
    
    private Course validateAndSaveCourse(Course course) {
        // Validate level
        var levelId = Optional.ofNullable(course.getLevel()).map(Level::getId)
                .orElseThrow(() -> new NotFoundException("Course must have a level", "LEVEL_NOT_FOUND"));
        var level = levelRepository.findById(levelId)
                .orElseThrow(() -> new NotFoundException("Level with id " + levelId + " not found", "LEVEL_NOT_FOUND"));
        course.setLevel(level);
        
        // Validate department
        var departmentId = Optional.ofNullable(course.getDepartment()).map(Department::getId)
                .orElseThrow(() -> new NotFoundException("Course must have a department", "DEPARTMENT_NOT_FOUND"));
        var department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new NotFoundException("Department with id " + departmentId + " not found", "DEPARTMENT_NOT_FOUND"));
        course.setDepartment(department);
        
        // Save the course
        return courseRepository.save(course);
    }
    
}
