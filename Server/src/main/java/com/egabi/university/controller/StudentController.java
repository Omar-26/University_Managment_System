package com.egabi.university.controller;

import com.egabi.university.dto.StudentDTO;
import com.egabi.university.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.egabi.university.util.ApiPaths.STUDENTS;

@RestController
@RequestMapping(STUDENTS)
@RequiredArgsConstructor
public class StudentController {
    // This interface will define methods related to student operations.
    // You can add methods like:
    // - Get all students
    // - Get student by ID
    // - Create a new student
    // - Update an existing student
    // - Delete a student
    
    private final StudentService studentService;
    
    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }
    
    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO studentDTO) {
        StudentDTO createdStudent = studentService.createStudent(studentDTO);
        URI location = URI.create(STUDENTS + "/" + studentDTO.getId());
        return ResponseEntity.created(location).body(createdStudent);
    }
    
    @PutMapping("/{studentId}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long studentId, @RequestBody StudentDTO studentDTO) {
        studentDTO.setId(studentId);
        StudentDTO updatedStudent = studentService.updateStudent(studentId, studentDTO);
        return ResponseEntity.ok(updatedStudent);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}
