package com.egabi.university.controller;

import com.egabi.university.dto.FacultyDTO;
import com.egabi.university.service.FacultyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.egabi.university.util.ApiPaths.FACULTIES;

@RestController
@RequestMapping(FACULTIES)
@RequiredArgsConstructor
public class FacultyController {
    private final FacultyService facultyService;
    
    @GetMapping
    public ResponseEntity<List<FacultyDTO>> getFaculties() {
        return ResponseEntity.ok(facultyService.getAllFaculties());
    }
    
    @GetMapping("/{facultyId}")
    public ResponseEntity<FacultyDTO> getFacultyById(@PathVariable Long facultyId) {
        return ResponseEntity.ok(facultyService.getFacultyById(facultyId));
    }
    
    @GetMapping("/{facultyId}/departments/count")
    public ResponseEntity<Long> countDepartmentsByFacultyId(@PathVariable Long facultyId) {
        Long count = facultyService.countDepartmentsByFacultyId(facultyId);
        return ResponseEntity.ok(count);
    }
    
    @GetMapping("/{facultyId}/students/count")
    public ResponseEntity<Long> countStudentsByFacultyId(@PathVariable Long facultyId) {
        Long count = facultyService.countStudentsByFacultyId(facultyId);
        return ResponseEntity.ok(count);
    }
    
    @PostMapping
    public ResponseEntity<FacultyDTO> createFaculty(@RequestBody FacultyDTO facultyDTO) {
        FacultyDTO createdFaculty = facultyService.createFaculty(facultyDTO);
        URI location = URI.create(FACULTIES + "/" + createdFaculty.getId());
        return ResponseEntity.created(location).body(createdFaculty);
    }
    
    @PutMapping("/{facultyId}")
    public ResponseEntity<FacultyDTO> updateFaculty(@PathVariable Long facultyId, @RequestBody FacultyDTO facultyDTO) {
        facultyDTO.setId(facultyId);
        FacultyDTO updatedFaculty = facultyService.updateFaculty(facultyId, facultyDTO);
        return ResponseEntity.ok(updatedFaculty);
    }
    
    @DeleteMapping("/{facultyId}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long facultyId) {
        facultyService.deleteFaculty(facultyId);
        return ResponseEntity.noContent().build();
    }
}
