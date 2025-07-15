package com.egabi.university.repository;

import com.egabi.university.entity.Course;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Course entity.
 * Provides CRUD operations and custom queries for Course.
 */
@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    
    /**
     * Finds all courses with their associated department and level.
     * Uses EntityGraph to optimize fetching related entities.
     *
     * @return List of all courses with department and level
     */
    @EntityGraph(attributePaths = {"department", "level"})
    List<Course> findAll();
    
    /**
     * Finds all courses associated with a specific department ID.
     *
     * @param departmentId the ID of the department
     * @return List of courses associated with the specified department
     */
    List<Course> findAllByDepartmentId(Long departmentId);
    
    Long countAllByDepartmentId(Long departmentId);
}