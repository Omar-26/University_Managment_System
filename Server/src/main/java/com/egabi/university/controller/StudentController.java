package com.egabi.university.controller;

import com.egabi.university.dto.StudentDTO;
import com.egabi.university.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.egabi.university.util.ApiPaths.STUDENTS;

/**
 * REST Controller for managing students.
 * Handles HTTP requests related to Student CRUD actions.
 * Provides endpoints to create, read, update, and delete students.
 */
@RestController
@RequestMapping(STUDENTS)
@RequiredArgsConstructor
public class StudentController {
    
    private final StudentService studentService;
    
    /**
     * Retrieves all students.
     *
     * @return ResponseEntity containing a list of StudentDTOs.
     */
    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }
    
    /**
     * Retrieves a student by ID.
     *
     * @param id the ID of the student to retrieve.
     * @return ResponseEntity containing the StudentDTO.
     */
    @GetMapping("/{studentId}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentService.getStudentById(studentId));
    }
    
    /**
     * Creates a new student.
     *
     * @param studentDTO the StudentDTO containing the details of the student to create.
     * @return ResponseEntity containing the created StudentDTO and the URI of the new resource.
     */
    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO studentDTO) {
        StudentDTO createdStudent = studentService.createStudent(studentDTO);
        URI location = URI.create(STUDENTS + "/" + createdStudent.getId());
        return ResponseEntity.created(location).body(createdStudent);
    }
    
    /**
     * Updates an existing student.
     *
     * @param studentId  the ID of the student to update.
     * @param studentDTO the StudentDTO containing the updated details of the student.
     * @return ResponseEntity containing the updated StudentDTO.
     */
    @PutMapping("/{studentId}")
    public ResponseEntity<StudentDTO> updateStudent(@PathVariable Long studentId, @RequestBody StudentDTO studentDTO) {
        StudentDTO updatedStudent = studentService.updateStudent(studentId, studentDTO);
        return ResponseEntity.ok(updatedStudent);
    }
    
    /**
     * Deletes a student by ID.
     *
     * @param id the ID of the student to delete.
     * @return ResponseEntity with no content.
     */
    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long studentId) {
        studentService.deleteStudent(studentId);
        return ResponseEntity.noContent().build();
    }
}
