package com.egabi.university.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Represents the composite key for the Enrollment entity.
 * It consists of a student ID and a course code.
 */
@Embeddable
@Data
@NoArgsConstructor
public class EnrollmentId implements Serializable {
    private Long studentId;
    private String courseCode;
    
    public EnrollmentId(Long studentId, String courseCode) {
        this.studentId = studentId;
        this.courseCode = courseCode;
    }
}
