package com.egabi.university.service.impl;

import com.egabi.university.dto.EnrollmentDTO;
import com.egabi.university.entity.Enrollment;
import com.egabi.university.entity.EnrollmentId;
import com.egabi.university.exception.NotFoundException;
import com.egabi.university.mapper.EnrollmentMapper;
import com.egabi.university.repository.EnrollmentRepository;
import com.egabi.university.service.EnrollmentService;
import com.egabi.university.service.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final ValidationService validationService;
    
    
    @Override
    public List<EnrollmentDTO> getAllEnrollments() {
        List<Enrollment> enrollments = enrollmentRepository.findAll();
        return enrollmentMapper.toDTOs(enrollments);
    }
    
    @Override
    public EnrollmentDTO getEnrollmentById(Long studentId, String courseCode) {
        // Validate enrollment id keys existence and build the EnrollmentId
        EnrollmentId enrollmentId = validateAndBuildEnrollmentId(studentId, courseCode);
        
        // Check if the enrollment exists
        Enrollment enrollment = validationService.getEnrollmentByIdOrThrow(enrollmentId);
        
        // Map the entity to DTO
        return enrollmentMapper.toDTO(enrollment);
    }
    
    @Override
    public EnrollmentDTO createEnrollment(EnrollmentDTO enrollmentDTO) {
        // Validate enrollment id keys existence and build the EnrollmentId
        EnrollmentId enrollmentId = validateAndBuildEnrollmentId(enrollmentDTO.getStudentId(), enrollmentDTO.getCourseCode());
        
        // Check if the enrollment already exists (it shouldn't exist for creation)
        validationService.assertEnrollmentExists(enrollmentId, false);
        
        // Map the DTO to the entity
        Enrollment enrollment = enrollmentMapper.toEntity(enrollmentDTO);
        
        // Validate the grade and save the enrollment
        enrollment = validateAndSaveEnrollment(enrollment);
        
        // Return the created enrollment as DTO
        return enrollmentMapper.toDTO(enrollment);
    }
    
    @Override
    public EnrollmentDTO updateEnrollment(Long studentId, String courseCode, EnrollmentDTO enrollmentDTO) {
        // Validate enrollment id keys existence and build the EnrollmentId
        EnrollmentId enrollmentId = validateAndBuildEnrollmentId(studentId, courseCode);
        
        // Check if the enrollment exists (it should exist for update)
        validationService.assertEnrollmentExists(enrollmentId, true);
        
        // Map the DTO to the entity
        Enrollment updatedEnrollment = enrollmentMapper.toEntity(enrollmentDTO);
        
        // Validate the grade and save the updated enrollment
        updatedEnrollment = validateAndSaveEnrollment(updatedEnrollment);
        
        // Return the updated enrollment as DTO
        return enrollmentMapper.toDTO(updatedEnrollment);
    }
    
    @Override
    public void deleteEnrollment(Long studentId, String courseCode) {
        // Validate enrollment id keys existence and build the EnrollmentId
        EnrollmentId enrollmentId = validateAndBuildEnrollmentId(studentId, courseCode);
        
        // Check if the enrollment exists (it should exist for deletion)
        validationService.assertEnrollmentExists(enrollmentId, true);
        
        // Delete the enrollment
        enrollmentRepository.deleteById(enrollmentId);
    }
    
    @Override
    public List<EnrollmentDTO> getEnrollmentsByStudentId(Long studentId) {
        // Validate that the student exists
        validationService.assertStudentExists(studentId, true);
        
        // Fetch enrollments by student ID
        List<Enrollment> enrollments = enrollmentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new NotFoundException("No enrollments found for student_id " + studentId,
                        "ENROLLMENTS_NOT_FOUND"));
        return enrollmentMapper.toDTOs(enrollments);
    }
    
    @Override
    public List<EnrollmentDTO> getEnrollmentsByCourseId(String courseCode) {
        // Validate that the course exists
        validationService.assertCourseExists(courseCode, true);
        
        // Fetch enrollments by course ID
        List<Enrollment> enrollments = enrollmentRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new NotFoundException("No enrollments found for course_id " + courseCode,
                        "ENROLLMENTS_NOT_FOUND"));
        return enrollmentMapper.toDTOs(enrollments);
    }
    
    @Override
    public List<EnrollmentDTO> getEnrollmentsByFacultyId(Long facultyId) {
        return List.of();
    }
    
    //-- Helper methods
    // Helper to validate enrollment id keys existence and build the EnrollmentId
    private EnrollmentId validateAndBuildEnrollmentId(Long studentId, String courseCode) {
        // Validate that the student exists
        validationService.assertStudentExists(studentId, true);
        
        // Validate that the course exists
        validationService.assertCourseExists(courseCode, true);
        
        // Create the composite key for the enrollment
        return new EnrollmentId(studentId, courseCode);
    }
    
    // Helper to validate that the grade is set and in range, then save the enrollment
    private Enrollment validateAndSaveEnrollment(Enrollment enrollment) {
        // Validate grade is set
        var grade = Optional.ofNullable(enrollment.getGrade())
                .orElseThrow(() -> new NotFoundException("Grade must be set for enrollment",
                        "GRADE_NOT_SET"));
        
        // Validate faculty is in range (between 0 and 100)
        validationService.validateGradeInRange(grade);
        enrollment.setGrade(grade);
        
        // save the enrollment
        return enrollmentRepository.save(enrollment);
    }
}
