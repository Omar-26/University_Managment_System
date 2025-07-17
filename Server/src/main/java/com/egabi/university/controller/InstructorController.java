package com.egabi.university.controller;

import com.egabi.university.dto.InstructorDTO;
import com.egabi.university.service.domain.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.egabi.university.util.ApiPaths.INSTRUCTORS;

/**
 * REST Controller for managing instructors.
 * Handles HTTP requests related to Instructor CRUD actions.
 * Provides endpoints to create, read, update, and delete instructors.
 */
@RestController
@RequestMapping(INSTRUCTORS)
@RequiredArgsConstructor
public class InstructorController {
    
    private final InstructorService instructorService;
    
    /**
     * Retrieves all instructors.
     *
     * @return List of InstructorDTO
     */
    @GetMapping
    public ResponseEntity<List<InstructorDTO>> getAllInstructors() {
        return ResponseEntity.ok(instructorService.getAllInstructors());
    }
    
    /**
     * Retrieves an instructor by its ID.
     *
     * @param instructorId the ID of the instructor
     * @return InstructorDTO
     */
    @GetMapping("/{instructorId}")
    public ResponseEntity<InstructorDTO> getInstructorById(@PathVariable Long instructorId) {
        return ResponseEntity.ok(instructorService.getInstructorById(instructorId));
    }
    
    /**
     * Creates a new instructor.
     *
     * @param instructorDTO the InstructorDTO containing the details of the instructor to create
     * @return ResponseEntity with the created InstructorDTO and location URI
     */
    @PostMapping
    public ResponseEntity<InstructorDTO> createInstructor(@RequestBody InstructorDTO instructorDTO) {
        InstructorDTO createdInstructor = instructorService.createInstructor(instructorDTO);
        URI location = URI.create(INSTRUCTORS + "/" + createdInstructor.getId());
        return ResponseEntity.created(location).body(createdInstructor);
    }
    
    /**
     * Updates an existing instructor.
     *
     * @param instructorId  the ID of the instructor to update
     * @param instructorDTO the InstructorDTO containing the updated details
     * @return ResponseEntity with the updated InstructorDTO
     */
    @PutMapping("/{instructorId}")
    public ResponseEntity<InstructorDTO> updateInstructor(@PathVariable Long instructorId, @RequestBody InstructorDTO instructorDTO) {
        InstructorDTO updatedInstructor = instructorService.updateInstructor(instructorId, instructorDTO);
        return ResponseEntity.ok(updatedInstructor);
    }
    
    /**
     * Deletes an instructor by its ID.
     *
     * @param instructorId the ID of the instructor to delete
     * @return ResponseEntity with no content status
     */
    @DeleteMapping("/{instructorId}")
    public ResponseEntity<Void> deleteInstructor(@PathVariable Long instructorId) {
        instructorService.deleteInstructor(instructorId);
        return ResponseEntity.noContent().build();
    }
}
