package com.egabi.university.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Student {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false)
    private String phoneNumber;
    
    @Column(nullable = false)
    private LocalDate dateOfBirth;
    
    private String gender;
    
    @ManyToOne
    @JoinColumn(name = "department_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Department department;
    
    @ManyToOne
    @JoinColumn(name = "level_id", nullable = false)
    private Level level;
    
    // Student <-> Enrollment (one-to-many)
    @OneToMany(mappedBy = "student")
    private List<Enrollment> enrollments = new ArrayList<>();
}