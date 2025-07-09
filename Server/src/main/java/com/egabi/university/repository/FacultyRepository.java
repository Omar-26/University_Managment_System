package com.egabi.university.repository;

import com.egabi.university.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Faculty entity.
 * Provides CRUD operations and custom queries for Faculty.
 */
@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    
    /**
     * Checks if a faculty exists with the given name (case-insensitive).
     *
     * @param name Faculty name
     * @return true if exists
     */
    boolean existsByNameIgnoreCase(String name);
}