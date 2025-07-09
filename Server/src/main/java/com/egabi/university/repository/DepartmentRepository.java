package com.egabi.university.repository;

import com.egabi.university.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
}