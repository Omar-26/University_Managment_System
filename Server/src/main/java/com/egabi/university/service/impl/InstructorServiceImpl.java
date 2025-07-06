package com.egabi.university.service.impl;

import com.egabi.university.dto.InstructorDTO;
import com.egabi.university.entity.Course;
import com.egabi.university.entity.Instructor;
import com.egabi.university.exception.NotFoundException;
import com.egabi.university.mapper.InstructorMapper;
import com.egabi.university.repository.CourseRepository;
import com.egabi.university.repository.DepartmentRepository;
import com.egabi.university.repository.InstructorRepository;
import com.egabi.university.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InstructorServiceImpl implements InstructorService {
    
    private final InstructorRepository instructorRepository;
    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    private final InstructorMapper instructorMapper;
    
    
    @Override
    public List<InstructorDTO> getAllInstructors() {
        List<Instructor> instructors = instructorRepository.findAll();
        return instructorMapper.toDTOs(instructors);
    }
    
    @Override
    public InstructorDTO getInstructorById(Long instructorId) {
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new NotFoundException("Instructor with id " + instructorId + " not found", "INSTRUCTOR_NOT_FOUND"));
        return instructorMapper.toDTO(instructor);
    }
    
    @Override
    public InstructorDTO createInstructor(InstructorDTO instructorDTO) {
        // Map the DTO to the entity
        Instructor instructor = instructorMapper.toEntity(instructorDTO);
        
        // Validate department - courses and save instructor
        instructor = validateAndSaveInstructor(instructor);
        
        // Return the saved student as a DTO
        return instructorMapper.toDTO(instructor);
    }
    
    @Override
    public InstructorDTO updateInstructor(Long instructorId, InstructorDTO instructorDTO) {
        // Check if the instructor exists
        if (!instructorRepository.existsById(instructorId))
            throw new NotFoundException("Instructor with id " + instructorId + " not found", "INSTRUCTOR_NOT_FOUND");
        
        // Map the DTO to the entity
        Instructor updatedInstructor = instructorMapper.toEntity(instructorDTO);
        
        // Validate department - courses and update instructor
        updatedInstructor = validateAndSaveInstructor(updatedInstructor);
        
        // Return the updated instructor as a DTO
        return instructorMapper.toDTO(updatedInstructor);
    }
    
    @Override
    public void deleteInstructor(Long instructorId) {
        // Check if the instructor exists
        Instructor instructor = instructorRepository.findById(instructorId)
                .orElseThrow(() -> new NotFoundException("Instructor with id " + instructorId + " not found", "INSTRUCTOR_NOT_FOUND"));
        
        // Check if the instructor is assigned to any courses and remove from those courses
        List<Course> courses = instructor.getCourses();
        if (courses != null && !courses.isEmpty())
            courses.forEach(course -> course.getInstructors().remove(instructor));
        
        // Delete the instructor
        instructorRepository.deleteById(instructorId);
    }
    
    private Instructor validateAndSaveInstructor(Instructor instructor) {
        // Validate department
        Optional.ofNullable(instructor.getDepartment())
                .orElseThrow(() -> new NotFoundException("Instructor must be in a department", "DEPARTMENT_NOT_FOUND"));
        if (!departmentRepository.existsById(instructor.getDepartment().getId()))
            throw new NotFoundException("Department with id " + instructor.getDepartment().getId() + " not found", "DEPARTMENT_NOT_FOUND");
        
        // Validate courses
        List<Course> instructorCourses = instructor.getCourses();
        if (instructorCourses != null) {
            for (Course course : instructorCourses) {
                // Check if the course exists
                if (!courseRepository.existsById(course.getCode()))
                    throw new NotFoundException("Course with code " + course.getCode() + " not found", "COURSE_NOT_FOUND");
                
                // Ensure the course is not already assigned to the instructor
                if (!course.getInstructors().contains(instructor))
                    course.getInstructors().add(instructor);
                
                // Ensure the instructor does not already have the course
                if (!instructorCourses.contains(course))
                    instructorCourses.add(course);
            }
        } else
            instructor.setCourses(List.of());
        
        // Save the instructor
        return instructorRepository.save(instructor);
    }
}
