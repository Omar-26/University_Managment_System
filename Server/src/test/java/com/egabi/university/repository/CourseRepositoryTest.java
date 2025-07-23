package com.egabi.university.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class CourseRepositoryTest {
    
    @Autowired
    private CourseRepository courseRepository;
    
    @Test
    public void testFindAllByDepartmentId() {
        // This test will verify that the context loads and the repository is available.
        // You can add more specific tests to check the functionality of the repository methods.
        assert courseRepository != null : "CourseRepository should not be null";
    }
}
