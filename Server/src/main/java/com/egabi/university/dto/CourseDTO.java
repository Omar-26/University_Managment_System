package com.egabi.university.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Course Entity.
 * Used to transfer course data between layers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {
    private String code;
    private String name;
    private Integer credits;
    private Long departmentId;
    private Long levelId;
}
