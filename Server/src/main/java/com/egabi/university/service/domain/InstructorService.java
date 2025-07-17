package com.egabi.university.service.domain;

import com.egabi.university.dto.InstructorDTO;
import com.egabi.university.entity.authentication.User;

import java.util.List;

public interface InstructorService {
    
    // ================================================================
    // CRUD Methods
    // ================================================================
    
    /**
     * Get all instructors available in the system.
     *
     * @return a list of all instructors as InstructorDTO objects
     */
    List<InstructorDTO> getAllInstructors();
    
    /**
     * Get Instructor by ID.
     *
     * @param instructorId the ID of the instructor
     * @return InstructorDTO object if exists, null otherwise
     */
    InstructorDTO getInstructorById(Long instructorId);
    
    /**
     * Create a new instructor.
     *
     * @param instructorDTO the instructor data transfer object containing instructor details
     * @return the created InstructorDTO object
     */
    InstructorDTO createInstructor(InstructorDTO instructorDTO);
    
    /**
     * Update an existing instructor.
     *
     * @param instructorId  the ID of the instructor to be updated
     * @param instructorDTO the instructor data transfer object containing updated instructor details
     * @return the updated InstructorDTO object
     */
    InstructorDTO updateInstructor(Long instructorId, InstructorDTO instructorDTO);
    
    /**
     * Delete an instructor by its ID.
     *
     * @param instructorId the ID of the instructor to be deleted
     */
    void deleteInstructor(Long instructorId);
    
    
    // ================================================================
    // Business Logic Methods
    // ================================================================
    
    // User-related methods
    
    /**
     * Create a new instructor with the associated user.
     *
     * @param instructorDTO the instructor data transfer object containing instructor details
     * @param user          the user associated with the instructor
     * @return the created InstructorDTO object
     */
    InstructorDTO createInstructor(InstructorDTO instructorDTO, User user);
}
