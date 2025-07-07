package com.egabi.university.service.validation;

import com.egabi.university.entity.*;
import com.egabi.university.exception.ConflictException;
import com.egabi.university.exception.NotFoundException;


/**
 * Centralized validation service for checking entity existence and referential integrity.
 */
public interface ValidationService {
    
    // ============================
    // Faculty
    // ============================
    
    /**
     * Validates that a Faculty exists for the given ID.
     * If not, throws a NotFoundException.
     *
     * @param facultyId the ID to validate
     * @return the Faculty entity if found
     * @throws NotFoundException if the faculty does not exist
     */
    Faculty getFacultyByIdOrThrow(Long facultyId);
    
    /**
     * Asserts the existence of a faculty based on the provided ID.
     * If shouldExist is true and the faculty does not exist, throws a NotFoundException.
     * If shouldExist is false and the faculty exists, throws a ConflictException.
     *
     * @param facultyId   the ID of the faculty to check
     * @param shouldExist whether the faculty should exist or not
     * @throws NotFoundException if the faculty does not exist when it should
     * @throws ConflictException if the faculty exists when it should not
     */
    void assertFacultyExists(Long facultyId, boolean shouldExist);
    
    // ============================
    // Level
    // ============================
    
    /**
     * Validates that a Level exists for the given ID.
     * If not, throws a NotFoundException.
     *
     * @param levelId the ID to validate
     * @return the Level entity if found
     * @throws NotFoundException if the level does not exist
     */
    Level getLevelByIdOrThrow(Long levelId);
    
    /**
     * Asserts the existence of a level based on the provided ID.
     * If shouldExist is true and the level does not exist, throws a NotFoundException.
     * If shouldExist is false and the level exists, throws a ConflictException.
     *
     * @param levelId     the ID of the level to check
     * @param shouldExist whether the level should exist or not
     * @throws NotFoundException if the level does not exist when it should
     * @throws ConflictException if the level exists when it should not
     */
    void assertLevelExists(Long levelId, boolean shouldExist);
    
    // ============================
    // Department
    // ============================
    
    /**
     * Validates that a Department exists for the given ID.
     * If not, throws a NotFoundException.
     *
     * @param departmentId the ID to validate
     * @return the Department entity if found
     * @throws NotFoundException if the department does not exist
     */
    Department getDepartmentByIdOrThrow(Long departmentId);
    
    /**
     * Asserts the existence of a department based on the provided ID.
     * If shouldExist is true and the department does not exist, throws a NotFoundException.
     * If shouldExist is false and the department exists, throws a ConflictException.
     *
     * @param departmentId the ID of the department to check
     * @param shouldExist  whether the department should exist or not
     * @throws NotFoundException if the department does not exist when it should
     * @throws ConflictException if the department exists when it should not
     */
    void assertDepartmentExists(Long departmentId, boolean shouldExist);
    
    // ============================
    // Student
    // ============================
    
    /**
     * Validates that a Student exists for the given ID.
     * If not, throws a NotFoundException.
     *
     * @param studentId the ID to validate
     * @return the Student entity if found
     * @throws NotFoundException if the student does not exist
     */
    Student getStudentByIdOrThrow(Long studentId);
    
    /**
     * Asserts the existence of a student based on the provided ID.
     * If shouldExist is true and the student does not exist, throws a NotFoundException.
     * If shouldExist is false and the student exists, throws a ConflictException.
     *
     * @param studentId   the ID of the student to check
     * @param shouldExist whether the student should exist or not
     * @throws NotFoundException if the student does not exist when it should
     * @throws ConflictException if the student exists when it should not
     */
    void assertStudentExists(Long studentId, boolean shouldExist);
    
    // ============================
    // Course
    // ============================
    
    /**
     * Validates that a Course exists for the given code.
     * If not, throws a NotFoundException.
     *
     * @param courseCode the code to validate
     * @return the Course entity if found
     * @throws NotFoundException if the course does not exist
     */
    Course getCourseByCodeOrThrow(String courseCode);
    
    /**
     * Asserts the existence of a course based on the provided code.
     * If shouldExist is true and the course does not exist, throws a NotFoundException.
     * If shouldExist is false and the course exists, throws a ConflictException.
     *
     * @param courseCode  the code of the course to check
     * @param shouldExist whether the course should exist or not
     * @throws NotFoundException if the course does not exist when it should
     * @throws ConflictException if the course exists when it should not
     */
    void assertCourseExists(String courseCode, boolean shouldExist);
    
    // ============================
    // Instructor
    // ============================
    
    /**
     * Validates that an Instructor exists for the given ID.
     * If not, throws a NotFoundException.
     *
     * @param instructorId the ID to validate
     * @return the Instructor entity if found
     * @throws NotFoundException if the instructor does not exist
     */
    Instructor getInstructorByIdOrThrow(Long instructorId);
    
    /**
     * Asserts the existence of an instructor based on the provided ID.
     * If shouldExist is true and the instructor does not exist, throws a NotFoundException.
     * If shouldExist is false and the instructor exists, throws a ConflictException.
     *
     * @param instructorId the ID of the instructor to check
     * @param shouldExist  whether the instructor should exist or not
     * @throws NotFoundException if the instructor does not exist when it should
     * @throws ConflictException if the instructor exists when it should not
     */
    void assertInstructorExists(Long instructorId, boolean shouldExist);
    
    // ============================
    // Enrollment
    // ============================
    
    /**
     * Validates that an Enrollment exists for the given composite ID.
     * If not, throws a NotFoundException.
     *
     * @param enrollmentId the composite key to validate
     * @return the Enrollment entity if found
     * @throws NotFoundException if the enrollment does not exist
     */
    Enrollment getEnrollmentByIdOrThrow(EnrollmentId enrollmentId);
    
    /**
     * Asserts the existence of an enrollment based on the provided EnrollmentId.
     * If shouldExist is true and the enrollment does not exist, throws a NotFoundException.
     * If shouldExist is false and the enrollment exists, throws a ConflictException.
     *
     * @param enrollmentId the composite key of the enrollment to check
     * @param shouldExist  whether the enrollment should exist or not
     * @throws NotFoundException if the enrollment does not exist when it should
     * @throws ConflictException if the enrollment exists when it should not
     */
    void assertEnrollmentExists(EnrollmentId enrollmentId, boolean shouldExist);
    
    // ============================
    // MISC
    // ============================
    
    /**
     * Validates that a grade is within the acceptable range (0-100).
     * If not, throws a NotFoundException.
     *
     * @param grade the grade to validate
     * @throws NotFoundException if the grade is not valid
     */
    void validateGradeInRange(Double grade);
}

