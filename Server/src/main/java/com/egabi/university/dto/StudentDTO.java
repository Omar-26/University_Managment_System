package com.egabi.university.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private Long departmentId;
    private Long levelId;
}
