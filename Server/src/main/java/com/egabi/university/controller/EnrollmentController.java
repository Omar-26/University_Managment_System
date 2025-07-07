package com.egabi.university.controller;

import com.egabi.university.dto.EnrollmentDTO;
import com.egabi.university.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.egabi.university.util.ApiPaths.ENROLLMENTS;

@RestController
@RequestMapping(ENROLLMENTS)
@RequiredArgsConstructor
public class EnrollmentController {
    
    private final EnrollmentService enrollmentService;
    
    @GetMapping
    public ResponseEntity<List<EnrollmentDTO>> getAllEnrollments() {
        return ResponseEntity.ok(enrollmentService.getAllEnrollments());
    }
    
    @GetMapping("/{studentId}/{courseCode}")
    public ResponseEntity<EnrollmentDTO> getEnrollment(@PathVariable Long studentId, @PathVariable String courseCode) {
        return ResponseEntity.ok(enrollmentService.getEnrollmentById(studentId, courseCode));
    }
    
    @PostMapping
    public ResponseEntity<EnrollmentDTO> createEnrollment(@RequestBody EnrollmentDTO enrollmentDTO) {
        EnrollmentDTO createdEnrollment = enrollmentService.createEnrollment(enrollmentDTO);
        URI location = URI.create(ENROLLMENTS + "/" + createdEnrollment.getStudentId() + "/" + createdEnrollment.getCourseCode());
        return ResponseEntity.created(location).body(createdEnrollment);
    }
    
    @PutMapping("/{studentId}/{courseCode}")
    public ResponseEntity<EnrollmentDTO> updateEnrollment(
            @PathVariable Long studentId,
            @PathVariable String courseCode,
            @RequestBody EnrollmentDTO enrollmentDTO) {
        EnrollmentDTO updatedEnrollment = enrollmentService.updateEnrollment(studentId, courseCode, enrollmentDTO);
        return ResponseEntity.ok(updatedEnrollment);
    }
    
    @DeleteMapping("/{studentId}/{courseCode}")
    public ResponseEntity<Void> deleteEnrollment(@PathVariable Long studentId, @PathVariable String courseCode) {
        enrollmentService.deleteEnrollment(studentId, courseCode);
        return ResponseEntity.noContent().build();
    }
}
