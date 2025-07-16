package com.egabi.university.controller;

import com.egabi.university.dto.CourseDTO;
import com.egabi.university.dto.DepartmentDTO;
import com.egabi.university.service.CourseService;
import com.egabi.university.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.egabi.university.util.ApiPaths.DEPARTMENTS;

/**
 * REST Controller for managing departments.
 * Handles HTTP requests related to Department CRUD actions.
 * Provides endpoints to create, read, update, and delete departments.
 */
@RestController
@RequestMapping(DEPARTMENTS)
@RequiredArgsConstructor
public class DepartmentController {
    
    private final DepartmentService departmentService;
    private final CourseService courseService;
    
    // ================================================================
    // CRUD Endpoints
    // ================================================================
    
    /**
     * Retrieves all departments.
     *
     * @return List of DepartmentDTO
     */
    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }
    
    /**
     * Retrieves a department by its ID.
     *
     * @param departmentId the ID of the department
     * @return DepartmentDTO
     */
    @GetMapping("/{departmentId}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable Long departmentId) {
        return ResponseEntity.ok(departmentService.getDepartmentById(departmentId));
    }
    
    /**
     * Creates a new department.
     *
     * @param departmentDTO the DepartmentDTO containing the details of the department to create
     * @return ResponseEntity with the created DepartmentDTO and location URI
     */
    @PostMapping
    public ResponseEntity<DepartmentDTO> createDepartment(@RequestBody DepartmentDTO departmentDTO) {
        DepartmentDTO createdDepartment = departmentService.createDepartment(departmentDTO);
        URI location = URI.create(DEPARTMENTS + "/" + createdDepartment.getId());
        return ResponseEntity.created(location).body(createdDepartment);
    }
    
    /**
     * Updates an existing department.
     *
     * @param departmentId  the ID of the department to update
     * @param departmentDTO the DepartmentDTO containing the updated details
     * @return ResponseEntity with the updated DepartmentDTO
     */
    @PutMapping("/{departmentId}")
    public ResponseEntity<DepartmentDTO> updateDepartment(@PathVariable Long departmentId, @RequestBody DepartmentDTO departmentDTO) {
        DepartmentDTO updatedDepartment = departmentService.updateDepartment(departmentId, departmentDTO);
        return ResponseEntity.ok(updatedDepartment);
    }
    
    /**
     * Deletes a department by its ID.
     *
     * @param departmentId the ID of the department to delete
     * @return ResponseEntity with no content status
     */
    @DeleteMapping("/{departmentId}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long departmentId) {
        departmentService.deleteDepartment(departmentId);
        return ResponseEntity.noContent().build();
    }
    
    // ================================================================
    // Business Logic Endpoints
    // ================================================================
    
    /**
     * Retrieves all courses associated with a specific department.
     *
     * @param departmentId the ID of the department
     * @return List of CourseDTO associated with the specified department
     */
    @GetMapping("/{departmentId}/courses")
    public ResponseEntity<List<CourseDTO>> getCoursesByDepartmentId(@PathVariable Long departmentId) {
        return ResponseEntity.ok(courseService.getCoursesByDepartmentId(departmentId));
    }
    
    /**
     * Counts all courses associated with a specific department.
     *
     * @param departmentId the ID of the department
     * @return Count of courses associated with the specified department
     */
    @GetMapping("/{departmentId}/courses/count")
    public ResponseEntity<Long> countCoursesByDepartmentId(@PathVariable Long departmentId) {
        return ResponseEntity.ok(courseService.countCoursesByDepartmentId(departmentId));
    }
}
