package com.egabi.university.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for Level Entity.
 * Used to transfer level data between layers.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor

public class LevelDTO {
    private Long id;
    private String name;
    private Long facultyId;
}
