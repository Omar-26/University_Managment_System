package com.egabi.university.dto;

import lombok.Data;

@Data
public class EnrollmentDTO {
    private Long studentId;
    private String courseCode;
    private Double grade;
}
