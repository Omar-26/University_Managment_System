package com.egabi.university.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Course {
    @Id
    @Column(unique = true, nullable = false)
    private String code;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private Integer credits;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "level_id", nullable = false)
    private Level level;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    
    @OneToMany(mappedBy = "course")
    private List<Enrollment> enrollments = new ArrayList<>();
    
    @ManyToMany(mappedBy = "courses")
    private List<Instructor> instructors = new ArrayList<>();
}