package com.egabi.university.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

/**
 * Represents a department in the university system.
 * Each department has a unique ID, a name, and is associated with a faculty.
 * It can have multiple students and courses.
 */
@Entity
@Data
@NoArgsConstructor
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "faculty_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Faculty faculty;
    
    @OneToMany(mappedBy = "department")
    private List<Student> students;
    
    @OneToMany(mappedBy = "department")
    private List<Course> courses;
    
}
