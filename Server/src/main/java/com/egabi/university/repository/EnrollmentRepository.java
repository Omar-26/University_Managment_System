package com.egabi.university.repository;

import com.egabi.university.entity.Enrollment;
import com.egabi.university.entity.EnrollmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository for Enrollment entity.
 * Provides CRUD operations and custom queries for Enrollment.
 */
@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, EnrollmentId> {
    
    /**
     * Finds all enrollments for a specific student.
     *
     * @param studentId ID of the student
     * @return List of enrollments for the student
     */
    Optional<List<Enrollment>> findByStudentId(Long studentId);
    
    /**
     * Finds all enrollments for a specific course.
     *
     * @param CourseCode Code of the course
     * @return List of enrollments for the course
     */
    Optional<List<Enrollment>> findByCourseCode(String CourseCode);
}