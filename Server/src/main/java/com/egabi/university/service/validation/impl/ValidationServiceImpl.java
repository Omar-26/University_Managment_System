package com.egabi.university.service.validation.impl;

import com.egabi.university.entity.*;
import com.egabi.university.entity.authentication.Role;
import com.egabi.university.entity.authentication.User;
import com.egabi.university.exception.BadRequestException;
import com.egabi.university.exception.ConflictException;
import com.egabi.university.exception.NotFoundException;
import com.egabi.university.repository.*;
import com.egabi.university.service.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Default implementation of {@link ValidationService}.
 * Provides centralized validation logic for checking entity existence and referential integrity.
 */
@Service
@RequiredArgsConstructor
public class ValidationServiceImpl implements ValidationService {
    
    private final FacultyRepository facultyRepository;
    private final LevelRepository levelRepository;
    private final DepartmentRepository departmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final InstructorRepository instructorRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    
    // ============================
    // Faculty
    // ============================
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Faculty getFacultyByIdOrThrow(Long facultyId) {
        return facultyRepository.findById(facultyId)
                .orElseThrow(() -> new NotFoundException("Faculty with id " + facultyId + " not found", "FACULTY_NOT_FOUND"));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void assertFacultyExists(Long facultyId) {
        boolean exists = facultyRepository.existsById(facultyId);
        if (!exists)
            throw new NotFoundException("Faculty with id " + facultyId + " not found", "FACULTY_NOT_FOUND");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void assertFacultyNameUnique(String facultyName) {
        boolean nameExists = facultyRepository.existsByNameIgnoreCase(facultyName);
        if (nameExists) {
            throw new ConflictException(
                    "Faculty with name '" + facultyName + "' already exists",
                    "FACULTY_ALREADY_EXISTS");
        }
    }
    
    // ============================
    // Level
    // ============================
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Level getLevelByIdOrThrow(Long levelId) {
        return levelRepository.findById(levelId)
                .orElseThrow(() -> new NotFoundException(
                        "Level with id " + levelId + " not found", "LEVEL_NOT_FOUND"));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void assertLevelExists(Long levelId) {
        boolean exists = levelRepository.existsById(levelId);
        if (!exists)
            throw new NotFoundException("Level with id " + levelId + " not found", "LEVEL_NOT_FOUND");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void assertLevelNameUniquePerFaculty(String levelName, Long facultyId) {
        boolean nameExists = levelRepository.existsByNameAndFacultyId(levelName, facultyId);
        if (nameExists)
            throw new ConflictException(
                    "Level with name " + levelName + " already exists in this faculty",
                    "LEVEL_ALREADY_EXISTS");
    }
    
    // ============================
    // Department
    // ============================
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Department getDepartmentByIdOrThrow(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new NotFoundException(
                        "Department with id " + departmentId + " not found", "DEPARTMENT_NOT_FOUND"));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void assertDepartmentExists(Long departmentId) {
        boolean exists = departmentRepository.existsById(departmentId);
        if (!exists)
            throw new NotFoundException("Department with id " + departmentId + " not found", "DEPARTMENT_NOT_FOUND");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void assertDepartmentNameUnique(String departmentName) {
        boolean nameExists = departmentRepository.existsByNameIgnoreCase(departmentName);
        if (nameExists)
            throw new ConflictException(
                    "Department with name '" + departmentName + "' already exists",
                    "DEPARTMENT_ALREADY_EXISTS");
    }
    
    // ============================
    // Student
    // ============================
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Student getStudentByIdOrThrow(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new NotFoundException(
                        "Student with id " + studentId + " not found", "STUDENT_NOT_FOUND"));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void assertStudentExists(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if (!exists)
            throw new NotFoundException("Student with id " + studentId + " not found", "STUDENT_NOT_FOUND");
    }
    
    // ============================
    // Course
    // ============================
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Course getCourseByCodeOrThrow(String courseCode) {
        return courseRepository.findById(courseCode)
                .orElseThrow(() -> new NotFoundException(
                        "Course with code " + courseCode + " not found", "COURSE_NOT_FOUND"));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void assertCourseExists(String courseCode, boolean shouldExist) {
        boolean exists = courseRepository.existsById(courseCode);
        if (shouldExist && !exists) {
            throw new NotFoundException("Course with code " + courseCode + " not found", "COURSE_NOT_FOUND");
        }
        if (!shouldExist && exists) {
            throw new ConflictException("Course with code " + courseCode + " already exists", "COURSE_ALREADY_EXISTS");
        }
    }
    
    // ============================
    // Instructor
    // ============================
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Instructor getInstructorByIdOrThrow(Long instructorId) {
        return instructorRepository.findById(instructorId)
                .orElseThrow(() -> new NotFoundException(
                        "Instructor with id " + instructorId + " not found", "INSTRUCTOR_NOT_FOUND"));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void assertInstructorExists(Long instructorId) {
        boolean exists = instructorRepository.existsById(instructorId);
        if (!exists)
            throw new NotFoundException("Instructor with id " + instructorId + " not found", "INSTRUCTOR_NOT_FOUND");
    }
    
    // ============================
    // Enrollment
    // ============================
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Enrollment getEnrollmentByIdOrThrow(EnrollmentId enrollmentId) {
        return enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new NotFoundException(
                        "Enrollment for student_id " + enrollmentId.getStudentId() +
                                " and course_code " + enrollmentId.getCourseCode() + " not found",
                        "ENROLLMENT_NOT_FOUND"));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void assertEnrollmentExists(EnrollmentId enrollmentId, boolean shouldExist) {
        boolean exists = enrollmentRepository.existsById(enrollmentId);
        if (shouldExist && !exists) {
            throw new NotFoundException(
                    "Enrollment not found for student_id " + enrollmentId.getStudentId()
                            + " and course_code " + enrollmentId.getCourseCode(),
                    "ENROLLMENT_NOT_FOUND");
        }
        if (!shouldExist && exists) {
            throw new ConflictException(
                    "Enrollment already exists for student_id " + enrollmentId.getStudentId()
                            + " and course_code " + enrollmentId.getCourseCode(),
                    "ENROLLMENT_ALREADY_EXISTS");
        }
    }
    
    // ============================
    // User
    // ============================
    
    /**
     * {@inheritDoc}
     */
    @Override
    public User getUserByIdOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(
                        "User with id " + userId + " not found", "USER_NOT_FOUND"));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void assertUserExists(Long userId) {
        boolean exists = userRepository.existsById(userId);
        if (!exists)
            throw new NotFoundException("User with id " + userId + " not found", "USER_NOT_FOUND");
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void assertUserEmailUnique(String email) {
        boolean emailExists = userRepository.existsByEmailIgnoreCase(email);
        if (emailExists) {
            throw new ConflictException(
                    "User with email '" + email + "' already exists",
                    "USER_EMAIL_ALREADY_EXISTS");
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void assertUserPasswordValid(String password) {
        if (password == null || password.length() < 8) {
            throw new ConflictException(
                    "Password must be at least 8 characters long",
                    "INVALID_PASSWORD");
        }
        //TODO Additional password validation logic can be added here
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void assertUserRoleValid(String role) {
        if (role == null || role.isEmpty()) {
            throw new BadRequestException("Role must not be null or empty", "INVALID_ROLE");
        }
        try {
            Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid role: " + role, "INVALID_ROLE");
        }
    }
    
    // ============================
    // MISC
    // ============================
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void validateGradeInRange(Double grade) {
        if (grade < 0.00 || grade > 100.00)
            throw new BadRequestException(
                    "Grade " + grade + " is Invalid, grade must be between 0.00 and 100.00",
                    "INVALID_GRADE");
    }
}
