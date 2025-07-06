package com.egabi.university.repository;

import com.egabi.university.entity.Faculty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyRepository extends JpaRepository<Faculty, Long> {
    
    boolean existsByName(String name);
    
    boolean existsByNameIgnoreCase(String name);
}