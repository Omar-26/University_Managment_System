package com.egabi.university.controller;

import com.egabi.university.dto.CourseDTO;
import com.egabi.university.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.egabi.university.util.ApiPaths.COURSES;

@RestController
@RequestMapping(COURSES)
@RequiredArgsConstructor
public class CourseController {
    // This class will handle HTTP requests related to courses such as:
    // - Get all courses
    // - Get course by code
    // - Create a new course
    // - Update an existing course
    // - Delete a course
    
    private final CourseService courseService;
    
    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }
    
    @GetMapping("/{code}")
    public ResponseEntity<CourseDTO> getCourseByCode(@PathVariable String code) {
        return ResponseEntity.ok(courseService.getCourseByCode(code));
    }
    
    
    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO courseDTO) {
        CourseDTO createdCourse = courseService.createCourse(courseDTO);
        URI location = URI.create(COURSES + "/" + createdCourse.getCode());
        return ResponseEntity.created(location).body(createdCourse);
    }
    
    @PutMapping("/{code}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable String code, @RequestBody CourseDTO courseDTO) {
        courseDTO.setCode(code);
        CourseDTO updatedCourse = courseService.updateCourse(courseDTO);
        return ResponseEntity.ok(updatedCourse);
    }
    
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteCourse(@PathVariable String code) {
        courseService.deleteCourse(code);
        return ResponseEntity.noContent().build();
    }
}
