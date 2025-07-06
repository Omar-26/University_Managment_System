package com.egabi.university.controller;

import com.egabi.university.dto.InstructorDTO;
import com.egabi.university.service.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.egabi.university.util.ApiPaths.INSTRUCTORS;

@RestController
@RequestMapping(INSTRUCTORS)
@RequiredArgsConstructor
public class InstructorController {
    // Add methods to handle instructor-related requests here such as:
    // Get all instructors
    // Get instructor by ID
    // Add a new instructor
    // Update an existing instructor
    // Delete an instructor
    
    private final InstructorService instructorService;
    
    @GetMapping
    public ResponseEntity<List<InstructorDTO>> getAllInstructors() {
        return ResponseEntity.ok(instructorService.getAllInstructors());
    }
    
    @GetMapping("/{instructorId}")
    public ResponseEntity<InstructorDTO> getInstructorById(@PathVariable Long instructorId) {
        return ResponseEntity.ok(instructorService.getInstructorById(instructorId));
    }
    
    @PostMapping
    public ResponseEntity<InstructorDTO> createInstructor(@RequestBody InstructorDTO instructorDTO) {
        InstructorDTO createdInstructor = instructorService.createInstructor(instructorDTO);
        URI location = URI.create(INSTRUCTORS + "/" + createdInstructor.getId());
        return ResponseEntity.created(location).body(createdInstructor);
    }
    
    @PutMapping("/{instructorId}")
    public ResponseEntity<InstructorDTO> updateInstructor(@PathVariable Long instructorId, @RequestBody InstructorDTO instructorDTO) {
        instructorDTO.setId(instructorId);
        InstructorDTO updatedInstructor = instructorService.updateInstructor(instructorId, instructorDTO);
        return ResponseEntity.ok(updatedInstructor);
    }
    
    @DeleteMapping("/{instructorId}")
    public ResponseEntity<Void> deleteInstructor(@PathVariable Long instructorId) {
        instructorService.deleteInstructor(instructorId);
        return ResponseEntity.noContent().build();
    }
}
