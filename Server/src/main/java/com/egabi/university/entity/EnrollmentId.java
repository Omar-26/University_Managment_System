package com.egabi.university.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;


@Embeddable
@Data
public class EnrollmentId implements Serializable {
    private Long studentId;
    private String courseCode;
}
