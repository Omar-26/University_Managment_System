package com.egabi.university.repository;

import com.egabi.university.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Department entity.
 * Provides CRUD operations and custom queries for Department.
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    
    /**
     * Checks if a department exists with the given name (case-insensitive).
     *
     * @param name Department name
     * @return true if exists
     */
    boolean existsByNameIgnoreCase(String name);
    
    /**
     * Gets all departments in a faculty by its ID.
     *
     * @param facultyId ID of the faculty
     * @return List of departments associated with the specified faculty
     */
    List<Department> findAllByFacultyId(Long facultyId);
    
    /**
     * Counts the number of departments associated with a specific faculty.
     *
     * @param facultyId ID of the faculty
     * @return Count of departments
     */
    Long countByFacultyId(Long facultyId);
}