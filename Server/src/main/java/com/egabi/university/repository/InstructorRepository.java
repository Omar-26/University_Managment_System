package com.egabi.university.repository;

import com.egabi.university.entity.Instructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Instructor entity.
 * Provides CRUD operations and custom queries for Instructor.
 */
@Repository
public interface InstructorRepository extends JpaRepository<Instructor, Long> {
}