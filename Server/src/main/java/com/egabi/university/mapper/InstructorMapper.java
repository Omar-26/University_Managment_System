package com.egabi.university.mapper;

import com.egabi.university.dto.InstructorDTO;
import com.egabi.university.entity.Course;
import com.egabi.university.entity.Instructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InstructorMapper {
    
    @Mappings({
            @Mapping(source = "department.id", target = "departmentId"),
            @Mapping(source = "courses", target = "courseCodes")
    })
    InstructorDTO toDTO(Instructor instructor);
    
    @Mappings({
            @Mapping(source = "departmentId", target = "department.id"),
            @Mapping(source = "courseCodes", target = "courses")
    })
    Instructor toEntity(InstructorDTO instructorDTO);
    
    List<InstructorDTO> toDTOs(List<Instructor> instructors);
    
    // === Custom mappers for course codes ===
    default String map(Course course) {
        return course.getCode();
    }
    
    default Course map(String code) {
        Course course = new Course();
        course.setCode(code);
        return course;
    }
    
    default List<String> mapCoursesToCodes(List<Course> courses) {
        return courses != null ? courses.stream().map(Course::getCode).collect(Collectors.toList()) : null;
    }
    
    default List<Course> mapCodesToCourses(List<String> codes) {
        return codes != null ? codes.stream().map(code -> {
            Course course = new Course();
            course.setCode(code);
            return course;
        }).collect(Collectors.toList()) : null;
    }
}
