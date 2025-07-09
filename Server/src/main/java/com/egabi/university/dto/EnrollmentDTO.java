package com.egabi.university.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Enrollment Entity.
 * Used to transfer enrollment data between layers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDTO {
    private Long studentId;
    private String courseCode;
    private Double grade;
}
