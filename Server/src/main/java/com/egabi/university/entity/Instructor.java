package com.egabi.university.entity;

import com.egabi.university.entity.authentication.User;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Instructor {
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
    
    @ManyToMany
    @JoinTable(
            name = "teaches",
            joinColumns = @JoinColumn(name = "instructor_id"),
            inverseJoinColumns = @JoinColumn(name = "course_code")
    )
    private List<Course> courses = new ArrayList<>();
    
    // Instructor <-> User (one-to-one)
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}