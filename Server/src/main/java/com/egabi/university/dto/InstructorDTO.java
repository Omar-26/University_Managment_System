package com.egabi.university.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Data Transfer Object for Instructor Entity.
 * Used to transfer instructor data between layers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor

public class InstructorDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private Long departmentId;
    private String departmentName;
    private List<String> courseCodes;
}
