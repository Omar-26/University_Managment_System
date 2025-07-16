package com.egabi.university.service;

import com.egabi.university.dto.StudentDTO;

import java.util.List;

public interface StudentService {
    
    // ================================================================
    // CRUD Methods
    // ================================================================
    
    /**
     * Get all students available in the system.
     *
     * @return a list of all students as StudentDTO objects
     */
    List<StudentDTO> getAllStudents();
    
    /**
     * Get Student by ID.
     *
     * @param id the ID of the student
     * @return StudentDTO object if exists, null otherwise
     */
    StudentDTO getStudentById(Long id);
    
    /**
     * Create a new student.
     *
     * @param studentDTO the student data transfer object containing student details
     * @return the created StudentDTO object
     */
    StudentDTO createStudent(StudentDTO studentDTO);
    
    /**
     * Update an existing student.
     *
     * @param facultyId  the ID of the faculty to which the student belongs
     * @param studentDTO the student data transfer object containing updated student details
     * @return the updated StudentDTO object
     */
    StudentDTO updateStudent(Long facultyId, StudentDTO studentDTO);
    
    /**
     * Delete a student by its ID.
     *
     * @param id the ID of the student to be deleted
     */
    void deleteStudent(Long id);
    
    // ================================================================
    // Business Logic Methods
    // ================================================================
    
    /**
     * Get all students in a faculty by its ID.
     *
     * @param facultyId the ID of the faculty
     * @return a list of StudentDTO objects associated with the specified faculty
     */
    List<StudentDTO> getStudentsByFacultyId(Long facultyId);
    
    /**
     * Count the number of students in a faculty by its ID.
     *
     * @param facultyId the ID of the faculty
     * @return the count of students in the specified faculty
     */
    Long countStudentsByFacultyId(Long facultyId);
}
