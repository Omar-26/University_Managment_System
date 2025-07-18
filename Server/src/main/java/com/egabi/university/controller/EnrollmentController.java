package com.egabi.university.controller;

import com.egabi.university.dto.EnrollmentDTO;
import com.egabi.university.service.academic.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.egabi.university.util.ApiPaths.ENROLLMENTS;

/**
 * REST Controller for managing enrollments.
 * Handles HTTP requests related to Enrollment CRUD actions.
 * Provides endpoints to create, read, update, and delete enrollments.
 */
@RestController
@RequestMapping(ENROLLMENTS)
@RequiredArgsConstructor
public class EnrollmentController {
    
    private final EnrollmentService enrollmentService;
    
    /**
     * Retrieves all enrollments.
     *
     * @return List of EnrollmentDTO
     */
    @GetMapping
    public ResponseEntity<List<EnrollmentDTO>> getAllEnrollments() {
        return ResponseEntity.ok(enrollmentService.getAllEnrollments());
    }
    
    /**
     * Retrieves an enrollment by student ID and course code.
     *
     * @param studentId  the ID of the student
     * @param courseCode the code of the course
     * @return EnrollmentDTO
     */
    @GetMapping("/{studentId}/{courseCode}")
    public ResponseEntity<EnrollmentDTO> getEnrollment(@PathVariable Long studentId, @PathVariable String courseCode) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentById(studentId, courseCode));
    }
    
    /**
     * Creates a new enrollment.
     *
     * @param enrollmentDTO the EnrollmentDTO containing the details of the enrollment to create
     * @return ResponseEntity with the created EnrollmentDTO and location URI
     */
    @PostMapping
    public ResponseEntity<EnrollmentDTO> createEnrollment(@RequestBody EnrollmentDTO enrollmentDTO) {
        EnrollmentDTO createdEnrollment = enrollmentService.createEnrollment(enrollmentDTO);
        URI location = URI.create(ENROLLMENTS + "/" + createdEnrollment.getStudentId() + "/" + createdEnrollment.getCourseCode());
        return ResponseEntity.created(location).body(createdEnrollment);
    }
    
    /**
     * Updates an existing enrollment.
     *
     * @param studentId     the ID of the student
     * @param courseCode    the code of the course
     * @param enrollmentDTO the EnrollmentDTO containing the updated details
     * @return ResponseEntity with the updated EnrollmentDTO
     */
    @PutMapping("/{studentId}/{courseCode}")
    public ResponseEntity<EnrollmentDTO> updateEnrollment(
            @PathVariable Long studentId,
            @PathVariable String courseCode,
            @RequestBody EnrollmentDTO enrollmentDTO) {
        EnrollmentDTO updatedEnrollment = enrollmentService.updateEnrollment(studentId, courseCode, enrollmentDTO);
        return ResponseEntity.ok(updatedEnrollment);
    }
    
    /**
     * Deletes an enrollment by student ID and course code.
     *
     * @param studentId  the ID of the student
     * @param courseCode the code of the course
     * @return ResponseEntity with no content status
     */
    @DeleteMapping("/{studentId}/{courseCode}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long studentId, @PathVariable String courseCode) {
        enrollmentService.deleteEnrollment(studentId, courseCode);
        return ResponseEntity.noContent().build();
    }
}
