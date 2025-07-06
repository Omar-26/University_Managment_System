package com.egabi.university.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Enrollment {
    @EmbeddedId
    private EnrollmentId id = new EnrollmentId();
    
    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
    
    @ManyToOne
    @MapsId("courseCode")
    @JoinColumn(name = "course_code", nullable = false)
    private Course course;
    
    private Double grade;
}