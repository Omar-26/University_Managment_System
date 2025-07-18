package com.egabi.university.service.academic.impl;

import com.egabi.university.dto.StudentDTO;
import com.egabi.university.entity.Department;
import com.egabi.university.entity.Level;
import com.egabi.university.entity.Student;
import com.egabi.university.entity.authentication.User;
import com.egabi.university.exception.ConflictException;
import com.egabi.university.exception.NotFoundException;
import com.egabi.university.mapper.StudentMapper;
import com.egabi.university.repository.StudentRepository;
import com.egabi.university.service.academic.StudentService;
import com.egabi.university.service.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Default implementation of {@link StudentService}.
 * Provides CRUD operations for managing students.
 */
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final ValidationService validationService;
    
    // ================================================================
    // CRUD Methods
    // ================================================================
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<StudentDTO> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return studentMapper.toDTOs(students);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public StudentDTO getStudentById(Long studentId) {
        Student student = validationService.getStudentByIdOrThrow(studentId);
        return studentMapper.toDTO(student);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public StudentDTO createStudent(StudentDTO studentDTO) {
        // Map the DTO to the entity
        Student student = studentMapper.toEntity(studentDTO);
        
        // Check the student is associated with a user if yes set the user, otherwise leave it null
        if (studentDTO.getUserId() != null) {
            User user = validationService.getUserByIdOrThrow(studentDTO.getUserId());
            student.setUser(user);
        }
        
        // Validate department - level and save student
        student = vaildateAndSaveStudent(student);
        
        // Return the saved student as a DTO
        return studentMapper.toDTO(student);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public StudentDTO updateStudent(Long studentId, StudentDTO studentDTO) {
        // Check if the student exists
        validationService.assertStudentExists(studentId);
        
        // Map the DTO to the entity
        Student updatedStudent = studentMapper.toEntity(studentDTO);
        updatedStudent.setId(studentId);
        
        // Validate department - level and update student
        updatedStudent = vaildateAndSaveStudent(updatedStudent);
        
        // Return the saved student as a DTO
        return studentMapper.toDTO(updatedStudent);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteStudent(Long studentId) {
        // Check if the student exists
        Student student = validationService.getStudentByIdOrThrow(studentId);
        
        // Ensure the student is not enrolled in any courses and prevent deletion
        if (!student.getEnrollments().isEmpty())
            throw new ConflictException("Cannot delete student with id " + studentId +
                    " because they are enrolled in courses", "STUDENT_HAS_ENROLLMENTS");
        
        // Delete the student
        studentRepository.deleteById(studentId);
    }
    
    // ================================================================
    // Business Logic Methods
    // ================================================================
    
    // User-related methods
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public StudentDTO createStudent(StudentDTO studentDTO, User user) {
        // Map the DTO to the entity
        Student student = studentMapper.toEntity(studentDTO);
        
        // Associate the student with the user
        student.setUser(user);
        
        // Validate department - level and save student
        student = vaildateAndSaveStudent(student);
        
        // Return the saved student as a DTO
        return studentMapper.toDTO(student);
    }
    
    // Faculty-related methods
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<StudentDTO> getStudentsByFacultyId(Long facultyId) {
        // Validate faculty existence
        validationService.assertFacultyExists(facultyId);
        
        // Get all students in the faculty
        List<Student> students = studentRepository.findAllByFacultyId(facultyId);
        return studentMapper.toDTOs(students);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Long countStudentsByFacultyId(Long facultyId) {
        // Validate faculty existence
        validationService.assertFacultyExists(facultyId);
        
        // Count students in the faculty
        Long count = studentRepository.countByFacultyId(facultyId);
        
        return count != null ? count : 0L;
    }
    
    // ================================================================
    // Helper methods
    // ================================================================
    
    /**
     * Validates the student entity and saves it to the database.
     *
     * @param student The student entity to validate and save.
     * @return The saved student entity.
     */
    private Student vaildateAndSaveStudent(Student student) {
        // Validate level
        //1. ensure that the level is set
        Long levelId = Optional.ofNullable(student.getLevel()).map(Level::getId)
                .orElseThrow(() -> new NotFoundException("Student must have a level", "LEVEL_NOT_FOUND"));
        
        // 2. ensure that the level exists
        Level level = validationService.getLevelByIdOrThrow(levelId);
        student.setLevel(level);
        
        // Validate department
        //1. ensure that the department is set
        Long departmentId = Optional.ofNullable(student.getDepartment()).map(Department::getId)
                .orElseThrow(() -> new NotFoundException("Student must be in a department", "DEPARTMENT_NOT_FOUND"));
        
        // 2. ensure that the department exists
        Department department = validationService.getDepartmentByIdOrThrow(departmentId);
        student.setDepartment(department);
        
        //TODO 3. ensure that the department is in the same faculty as the level
//        if (!department.getFaculty().getId().equals(level.getFaculty().getId())) {
//            throw new ConflictException("Department and level must be in the same faculty",
//                    "DEPARTMENT_LEVEL_FACULTY_MISMATCH");
//        }
        
        // Save the student
        return studentRepository.save(student);
    }
}
