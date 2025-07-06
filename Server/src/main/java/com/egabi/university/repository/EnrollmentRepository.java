package com.egabi.university.repository;

import com.egabi.university.entity.Enrollment;
import com.egabi.university.entity.EnrollmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, EnrollmentId> {
}