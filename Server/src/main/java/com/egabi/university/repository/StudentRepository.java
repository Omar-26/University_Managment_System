package com.egabi.university.repository;

import com.egabi.university.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Student entity.
 * Provides CRUD operations and custom queries for Student.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    /**
     * Gets all students in a faculty by its ID.
     *
     * @param facultyId the ID of the faculty
     * @return a list of students associated with the specified faculty
     */
    @Query("select s from Student s where s.department.faculty.id = :facultyId")
    List<Student> findAllByFacultyId(@Param("facultyId") Long facultyId);
    
    /**
     * Counts the number of students in a faculty by its ID.
     *
     * @param facultyId the ID of the faculty
     * @return the count of students in the specified faculty
     */
    @Query("select count(s) from Student s where s.department.faculty.id = :facultyId")
    Long countByFacultyId(@Param("facultyId") Long facultyId);
}