package com.egabi.university.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a course in the university system.
 * Each course has a unique code, a name, credits, and is associated with a level and department.
 * It can have multiple enrollments and instructors.
 */
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Course {
    @Id
    @Column(unique = true, nullable = false)
    private String code;
    
    @Column(nullable = false)
    @EqualsAndHashCode.Include
    private String name;
    
    @Column(nullable = false)
    @EqualsAndHashCode.Include
    private Integer credits;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id", nullable = false)
    @EqualsAndHashCode.Include
    private Level level;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    
    @OneToMany(mappedBy = "course")
    private List<Enrollment> enrollments = new ArrayList<>();
    
    @ManyToMany(mappedBy = "courses")
    private List<Instructor> instructors = new ArrayList<>();
}