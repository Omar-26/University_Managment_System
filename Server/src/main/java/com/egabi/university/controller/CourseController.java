package com.egabi.university.controller;

import com.egabi.university.dto.CourseDTO;
import com.egabi.university.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.egabi.university.util.ApiPaths.COURSES;

/**
 * REST Controller for managing courses.
 * Handles HTTP requests related to Course CRUD actions.
 * Provides endpoints to create, read, update, and delete courses.
 */
@RestController
@RequestMapping(COURSES)
@RequiredArgsConstructor
public class CourseController {
    
    private final CourseService courseService;
    
    /**
     * Retrieves all courses.
     *
     * @return List of CourseDTO
     */
    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }
    
    /**
     * Retrieves a course by its code.
     *
     * @param code the code of the course
     * @return CourseDTO
     */
    @GetMapping("/{code}")
    public ResponseEntity<CourseDTO> getCourseByCode(@PathVariable String code) {
        return ResponseEntity.ok(courseService.getCourseByCode(code));
    }
    
    /**
     * Creates a new course.
     *
     * @param courseDTO the CourseDTO containing the details of the course to create
     * @return ResponseEntity with the created CourseDTO and location URI
     */
    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO courseDTO) {
        CourseDTO createdCourse = courseService.createCourse(courseDTO);
        URI location = URI.create(COURSES + "/" + createdCourse.getCode());
        return ResponseEntity.created(location).body(createdCourse);
    }
    
    /**
     * Updates an existing course.
     *
     * @param code      the code of the course to update
     * @param courseDTO the CourseDTO containing the updated details of the course
     * @return ResponseEntity with the updated CourseDTO
     */
    @PutMapping("/{code}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable String code, @RequestBody CourseDTO courseDTO) {
        CourseDTO updatedCourse = courseService.updateCourse(code, courseDTO);
        return ResponseEntity.ok(updatedCourse);
    }
    
    /**
     * Deletes a course by its code.
     *
     * @param code the code of the course to delete
     * @return ResponseEntity with no content
     */
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String code) {
        courseService.deleteCourse(code);
        return ResponseEntity.noContent().build();
    }
}
