package com.egabi.university.service.academic.impl;

import com.egabi.university.dto.InstructorDTO;
import com.egabi.university.entity.Course;
import com.egabi.university.entity.Department;
import com.egabi.university.entity.Instructor;
import com.egabi.university.entity.authentication.User;
import com.egabi.university.exception.BadRequestException;
import com.egabi.university.exception.NotFoundException;
import com.egabi.university.mapper.InstructorMapper;
import com.egabi.university.repository.InstructorRepository;
import com.egabi.university.service.academic.InstructorService;
import com.egabi.university.service.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Default implementation of {@link InstructorService}.
 * Provides CRUD operations for managing instructors.
 */
@Service
@RequiredArgsConstructor
public class InstructorServiceImpl implements InstructorService {
    
    private final InstructorRepository instructorRepository;
    private final InstructorMapper instructorMapper;
    private final ValidationService validationService;
    
    // ================================================================
    // CRUD Methods
    // ================================================================
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<InstructorDTO> getAllInstructors() {
        List<Instructor> instructors = instructorRepository.findAll();
        return instructorMapper.toDTOs(instructors);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public InstructorDTO getInstructorById(Long instructorId) {
        Instructor instructor = validationService.getInstructorByIdOrThrow(instructorId);
        return instructorMapper.toDTO(instructor);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public InstructorDTO createInstructor(InstructorDTO instructorDTO) {
        // Map the DTO to the entity
        Instructor instructor = instructorMapper.toEntity(instructorDTO);
        
        // Validate department - courses and save instructor
        instructor = validateAndSaveInstructor(instructor);
        
        // Return the saved student as a DTO
        return instructorMapper.toDTO(instructor);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public InstructorDTO updateInstructor(Long instructorId, InstructorDTO instructorDTO) {
        // Check if the instructor exists
        Instructor instructor = validationService.getInstructorByIdOrThrow(instructorId);
        
        // Map the DTO to the entity
        instructorMapper.updateInstructorFromDto(instructorDTO, instructor);
        instructor.setId(instructorId);
        
        // Validate department and courses, then update instructor
        instructor = validateAndSaveInstructor(instructor);
        
        // Return the updated instructor as a DTO
        return instructorMapper.toDTO(instructor);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteInstructor(Long instructorId) {
        // Check if the instructor exists
        Instructor instructor = validationService.getInstructorByIdOrThrow(instructorId);
        
        // Check if the instructor is assigned to any courses and remove from those courses
        if (instructor.getCourses() != null && !instructor.getCourses().isEmpty())
            instructor.getCourses().forEach(course -> course.getInstructors().remove(instructor));
        
        // Delete the instructor
        instructorRepository.delete(instructor);
    }
    
    // ================================================================
    // Business Logic Methods
    // ================================================================
    
    @Override
    @Transactional
    public InstructorDTO createInstructor(InstructorDTO instructorDTO, User user) {
        // Map the DTO to the entity
        Instructor instructor = instructorMapper.toEntity(instructorDTO);
        
        // Associate the instructor with the user
        instructor.setUser(user);
        
        // Validate department - courses and save instructor
        instructor = validateAndSaveInstructor(instructor);
        
        // Return the saved instructor as a DTO
        return instructorMapper.toDTO(instructor);
    }
    
    // ================================================================
    // Helper methods
    // ================================================================
    
    /**
     * Validates the instructor's department and courses, and saves the instructor.
     *
     * @param instructor The instructor to validate and save.
     * @return The saved instructor.
     * @throws BadRequestException if the department is not set.
     * @throws NotFoundException   if the department is not found.
     */
    private Instructor validateAndSaveInstructor(Instructor instructor) {
        // Validate department
        //1. ensure that the department is set
        Long departmentId = Optional.ofNullable(instructor.getDepartment()).map(Department::getId)
                .orElseThrow(() -> new BadRequestException("Instructor must be in a department", "DEPARTMENT_NOT_FOUND"));
        
        // 2. ensure that the department exists
        Department department = validationService.getDepartmentByIdOrThrow(departmentId);
        instructor.setDepartment(department);
        
        // Validate courses
        List<Course> instructorCourses = instructor.getCourses();
        if (instructorCourses != null) {
            for (Course course : instructorCourses) {
                // Check if the course exists
                validationService.assertCourseExists(course.getCode(), true);
                
                // Ensure the course is not already assigned to the instructor
                if (!course.getInstructors().contains(instructor))
                    course.getInstructors().add(instructor);
                
                // Ensure the instructor does not already have the course
                if (!instructorCourses.contains(course))
                    instructorCourses.add(course);
            }
        } else
            instructor.setCourses(new ArrayList<>());
        
        // Save the instructor
        return instructorRepository.save(instructor);
    }
}
