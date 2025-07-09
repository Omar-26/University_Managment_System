package com.egabi.university.controller;

import com.egabi.university.dto.FacultyDTO;
import com.egabi.university.service.FacultyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.egabi.university.util.ApiPaths.FACULTIES;

/**
 * REST Controller for managing faculties.
 * Handles HTTP requests related to Faculty CRUD actions.
 * Provides endpoints to create, read, update, and delete faculties.
 */
@RestController
@RequestMapping(FACULTIES)
@RequiredArgsConstructor
public class FacultyController {
    private final FacultyService facultyService;
    
    /**
     * Retrieves all faculties.
     *
     * @return List of FacultyDTO
     */
    @GetMapping
    public ResponseEntity<List<FacultyDTO>> getFaculties() {
        return ResponseEntity.ok(facultyService.getAllFaculties());
    }
    
    /**
     * Retrieves a faculty by its ID.
     *
     * @param facultyId the ID of the faculty
     * @return FacultyDTO
     */
    @GetMapping("/{facultyId}")
    public ResponseEntity<FacultyDTO> getFacultyById(@PathVariable Long facultyId) {
        return ResponseEntity.ok(facultyService.getFacultyById(facultyId));
    }
    
    @GetMapping("/{facultyId}/departments/count")
    public ResponseEntity<Long> countDepartmentsByFacultyId(@PathVariable Long facultyId) {
        Long count = facultyService.countDepartmentsByFacultyId(facultyId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Counts the number of students in a faculty by its ID.
     *
     * @param facultyId the ID of the faculty
     * @return the count of students in the faculty
     */
    @GetMapping("/{facultyId}/students/count")
    public ResponseEntity<Long> countStudentsByFacultyId(@PathVariable Long facultyId) {
        Long count = facultyService.countStudentsByFacultyId(facultyId);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Creates a new faculty.
     *
     * @param facultyDTO the FacultyDTO containing the details of the faculty to create
     * @return ResponseEntity with the created FacultyDTO and location URI
     */
    @PostMapping
    public ResponseEntity<FacultyDTO> createFaculty(@RequestBody FacultyDTO facultyDTO) {
        FacultyDTO createdFaculty = facultyService.createFaculty(facultyDTO);
        URI location = URI.create(FACULTIES + "/" + createdFaculty.getId());
        return ResponseEntity.created(location).body(createdFaculty);
    }
    
    /**
     * Updates an existing faculty.
     *
     * @param facultyId  the ID of the faculty to update
     * @param facultyDTO the FacultyDTO containing the updated details
     * @return ResponseEntity with the updated FacultyDTO
     */
    @PutMapping("/{facultyId}")
    public ResponseEntity<FacultyDTO> updateFaculty(@PathVariable Long facultyId, @RequestBody FacultyDTO facultyDTO) {
        FacultyDTO updatedFaculty = facultyService.updateFaculty(facultyId, facultyDTO);
        return ResponseEntity.ok(updatedFaculty);
    }
    
    /**
     * Deletes a faculty by its ID.
     *
     * @param facultyId the ID of the faculty to delete
     * @return ResponseEntity with no content status
     */
    @DeleteMapping("/{facultyId}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long facultyId) {
        facultyService.deleteFaculty(facultyId);
        return ResponseEntity.noContent().build();
    }
}
