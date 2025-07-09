package com.egabi.university.repository;

import com.egabi.university.entity.Level;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Level entity.
 * Provides CRUD operations and custom queries for Level.
 */
@Repository
public interface LevelRepository extends JpaRepository<Level, Long> {
    
    /**
     * Checks if a level exists with the given name and faculty ID.
     *
     * @param name      Level name
     * @param facultyId ID of the faculty
     * @return true if exists, false otherwise
     */
    boolean existsByNameAndFacultyId(String name, Long facultyId);
}