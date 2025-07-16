package com.egabi.university.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Data Transfer Object for Student Entity.
 * Used to transfer student data between layers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor

public class StudentDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private Long departmentId;
    private String departmentName;
    private Long levelId;
    private String levelName;
    private Long facultyId;
    private String facultyName;
}
