package com.egabi.university.repository;

import com.egabi.university.entity.Enrollment;
import com.egabi.university.entity.EnrollmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, EnrollmentId> {
    Optional<List<Enrollment>> findByStudentId(Long studentId);
    
    Optional<List<Enrollment>> findByCourseCode(String CourseCode);
}