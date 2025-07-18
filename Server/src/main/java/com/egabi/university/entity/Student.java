package com.egabi.university.entity;

import com.egabi.university.entity.authentication.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a student in the university system.
 * Each student has a unique ID, personal details, and is associated with a department and level.
 * They can enroll in multiple courses and have a one-to-one relationship with a user account.
 */
@Entity
@Data
@NoArgsConstructor
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
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    
    @ManyToOne
    @JoinColumn(name = "level_id", nullable = false)
    private Level level;
    
    // Student <-> Enrollment (one-to-many)
    @OneToMany(mappedBy = "student")
    private List<Enrollment> enrollments = new ArrayList<>();
    
    // Student <-> User (one-to-one)
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}