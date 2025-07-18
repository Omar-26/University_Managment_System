package com.egabi.university.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Faculty Entity.
 * Used to transfer faculty data between layers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacultyDTO {
    private Long id;
    private String name;
}
