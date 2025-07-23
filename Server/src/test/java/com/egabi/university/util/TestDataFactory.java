package com.egabi.university.util;

import com.egabi.university.dto.DepartmentDTO;
import com.egabi.university.dto.FacultyDTO;
import com.egabi.university.entity.Course;
import com.egabi.university.entity.Department;
import com.egabi.university.entity.Faculty;
import com.egabi.university.entity.Student;

import java.util.List;

/**
 * <h2>TestDataFactory</h2>
 * <p>
 * Central utility for creating reusable test Entities and DTOs.
 * </p>
 * Keeps test data consistent and easy to maintain across unit tests.
 * </p>
 *
 * <h3>Usage:</h3>
 * <pre>{@code
 * Faculty faculty = TestDataFactory.buildFaculty();
 * Department department = TestDataFactory.buildDepartment(faculty);
 * DepartmentDTO departmentDTO = TestDataFactory.buildDepartmentDTO();
 * }</pre>
 *
 * <p>
 * <b>Note:</b> This class should only be used in test sources.
 * </p>
 */
public final class TestDataFactory {
    
    /**
     * Prevent instantiation.
     */
    private TestDataFactory() {
    }
    
    /**
     * Shared ID to represent an entity that does NOT exist.
     * Use for negative test scenarios.
     */
    public static final long NON_EXISTENT_ID = Long.MAX_VALUE;
    
    // ================================================================
    // Faculty
    // ================================================================
    
    /**
     * Builds a default Faculty entity with typical test values.
     *
     * @return Faculty entity.
     */
    public static Faculty buildFaculty() {
        return Faculty.builder()
                .id(1L)
                .name("Engineering")
                .departments(List.of())
                .build();
    }
    
    /**
     * Builds a Faculty entity with a custom ID and name.
     *
     * @param id   ID to use.
     * @param name Name to use.
     * @return Faculty entity.
     */
    public static Faculty buildFaculty(Long id, String name) {
        return Faculty.builder()
                .id(id)
                .name(name)
                .departments(List.of())
                .build();
    }
    
    /**
     * Builds a default FacultyDTO.
     *
     * @return FacultyDTO.
     */
    public static FacultyDTO buildFacultyDTO() {
        return new FacultyDTO(1L, "Engineering");
    }
    
    /**
     * Builds a FacultyDTO with custom values.
     *
     * @param id   ID to use.
     * @param name Name to use.
     * @return FacultyDTO.
     */
    public static FacultyDTO buildFacultyDTO(Long id, String name) {
        return new FacultyDTO(id, name);
    }
    
    // ================================================================
    // Department
    // ================================================================
    
    /**
     * Builds a default Department entity with given Faculty.
     *
     * @param faculty Faculty to associate.
     * @return Department entity.
     */
    public static Department buildDepartment(Faculty faculty) {
        return Department.builder()
                .id(1L)
                .name("Computer Engineering")
                .faculty(faculty)
                .students(List.of())
                .courses(List.of())
                .build();
    }
    
    /**
     * Builds a Department entity with custom ID and name.
     *
     * @param id      ID to use.
     * @param name    Name to use.
     * @param faculty Faculty to associate.
     * @return Department entity.
     */
    public static Department buildDepartment(Long id, String name, Faculty faculty) {
        return Department.builder()
                .id(id)
                .name(name)
                .faculty(faculty)
                .students(List.of())
                .courses(List.of())
                .build();
    }
    
    /**
     * Builds a default DepartmentDTO.
     *
     * @return DepartmentDTO.
     */
    public static DepartmentDTO buildDepartmentDTO() {
        return new DepartmentDTO(1L, "Computer Engineering", 1L);
    }
    
    /**
     * Builds a DepartmentDTO with custom values.
     *
     * @param id        ID to use.
     * @param name      Name to use.
     * @param facultyId Faculty ID to associate.
     * @return DepartmentDTO.
     */
    public static DepartmentDTO buildDepartmentDTO(Long id, String name, Long facultyId) {
        return new DepartmentDTO(id, name, facultyId);
    }
    
    // ================================================================
    // Student
    // ================================================================
    
    /**
     * Builds a default Student entity for a given Department.
     *
     * @param department Department to associate.
     * @return Student entity.
     */
    public static Student buildStudent(Department department) {
        return Student.builder()
                .id(1L)
                .firstName("Alice")
                .lastName("Smith")
                .department(department)
                .build();
    }
    
    /**
     * Builds a Student entity with custom ID and name.
     *
     * @param id         ID to use.
     * @param firstName  First Name to use.
     * @param lastName   Last Name to use.
     * @param department Department to associate.
     * @return Student entity.
     */
    public static Student buildStudent(Long id, String firstName, String lastName, Department department) {
        return Student.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .department(department)
                .build();
    }
    
    // ================================================================
    // Course
    // ================================================================
    
    /**
     * Builds a default Course entity for a given Department.
     *
     * @param department Department to associate.
     * @return Course entity.
     */
    public static Course buildCourse(Department department) {
        return Course.builder()
                .code("CS101")
                .name("Algorithms")
                .department(department)
                .build();
    }
    
    /**
     * Builds a Course entity with custom code and name.
     *
     * @param code       Code to use.
     * @param name       Name to use.
     * @param department Department to associate.
     * @return Course entity.
     */
    public static Course buildCourse(String code, String name, Department department) {
        return Course.builder()
                .code(code)
                .name(name)
                .department(department)
                .build();
    }
    
    // ================================================================
    // Add more builders below as your domain grows
    // ================================================================
}
