package com.egabi.university.service;

import com.egabi.university.dto.EnrollmentDTO;

import java.util.List;

public interface EnrollmentService {
    /**
     * Get all enrollments available in the system.
     *
     * @return a list of all enrollments as EnrollmentDTO objects
     */
    List<EnrollmentDTO> getAllEnrollments();
    
    /**
     * Get Enrollment by ID.
     *
     * @param studentId  the ID of the student
     * @param courseCode the code of the course
     * @return EnrollmentDTO object if exists, null otherwise
     */
    EnrollmentDTO getEnrollmentById(Long studentId, String courseCode);
    
    /**
     * Create a new enrollment.
     *
     * @param enrollmentDTO the enrollment data transfer object containing enrollment details
     * @return the created EnrollmentDTO object
     */
    EnrollmentDTO createEnrollment(EnrollmentDTO enrollmentDTO);
    
    /**
     * Update an existing enrollment.
     *
     * @param studentId     the ID of the student
     * @param courseCode    the code of the course
     * @param enrollmentDTO the enrollment data transfer object containing updated enrollment details
     * @return the updated EnrollmentDTO object
     */
    EnrollmentDTO updateEnrollment(Long studentId, String courseCode, EnrollmentDTO enrollmentDTO);
    
    /**
     * Delete an enrollment by its ID.
     *
     * @param studentId  the ID of the student
     * @param courseCode the code of the course
     */
    void deleteEnrollment(Long studentId, String courseCode);
    
    /**
     * Get all enrollments for a specific student.
     *
     * @param studentId the ID of the student
     * @return a list of EnrollmentDTO objects for the specified student
     */
    List<EnrollmentDTO> getEnrollmentsByStudentId(Long studentId);
    
    /**
     * Get all enrollments for a specific course.
     *
     * @param courseCode the code of the course
     * @return a list of EnrollmentDTO objects for the specified course
     */
    List<EnrollmentDTO> getEnrollmentsByCourseId(String courseCode);
    
    /**
     * Get all enrollments for a specific faculty.
     *
     * @param facultyId the ID of the faculty
     * @return a list of EnrollmentDTO objects for the specified faculty
     */
    List<EnrollmentDTO> getEnrollmentsByFacultyId(Long facultyId);
}


