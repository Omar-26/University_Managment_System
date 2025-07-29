package com.egabi.university.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a level in the university system.
 * Each level is associated with a faculty and can have multiple students and courses.
 */
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Level {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    @EqualsAndHashCode.Include
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "faculty_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Faculty faculty;
    
    @OneToMany(mappedBy = "level")
    private List<Student> students = new ArrayList<>();
    
    @OneToMany(mappedBy = "level")
    private List<Course> courses = new ArrayList<>();
    
}
