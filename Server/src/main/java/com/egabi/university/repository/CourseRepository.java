package com.egabi.university.repository;

import com.egabi.university.entity.Course;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {
    @EntityGraph(attributePaths = {"department", "level"})
//    @SuppressWarnings("override")
    List<Course> findAll();
    
    boolean existsByCodeIgnoreCase(String code);
    
    Optional<Course> findByCodeIgnoreCase(String code);
}