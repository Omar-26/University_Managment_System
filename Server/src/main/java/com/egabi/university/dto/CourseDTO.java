package com.egabi.university.dto;

import lombok.Data;

@Data
public class CourseDTO {
    private String code;
    private String name;
    private Integer credits;
    private Long departmentId;
    private Long levelId;
}
