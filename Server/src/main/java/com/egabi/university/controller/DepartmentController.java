package com.egabi.university.controller;

import com.egabi.university.dto.DepartmentDTO;
import com.egabi.university.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.egabi.university.util.ApiPaths.DEPARTMENTS;

@RestController
@RequestMapping(DEPARTMENTS)
@RequiredArgsConstructor
public class DepartmentController {
    // This class will handle HTTP requests related to departments such as:
    // - Get all departments
    // - Get department by ID
    // - Create a new department
    // - Update an existing department
    // - Delete a department
    
    private final DepartmentService departmentService;
    
    @GetMapping
    public ResponseEntity<List<DepartmentDTO>> getAllDepartments() {
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<DepartmentDTO> getDepartmentById(@PathVariable Long id) {
        return ResponseEntity.ok(departmentService.getDepartmentById(id));
    }
    
    @PostMapping
    public ResponseEntity<DepartmentDTO> createDepartment(@RequestBody DepartmentDTO departmentDTO) {
        DepartmentDTO createdDepartment = departmentService.createDepartment(departmentDTO);
        URI location = URI.create(DEPARTMENTS + "/" + createdDepartment.getId());
        return ResponseEntity.created(location).body(createdDepartment);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<DepartmentDTO> updateDepartment(@PathVariable Long id, @RequestBody DepartmentDTO departmentDTO) {
        departmentDTO.setId(id);
        DepartmentDTO updatedDepartment = departmentService.updateDepartment(departmentDTO);
        return ResponseEntity.ok(updatedDepartment);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }
    
    
}
