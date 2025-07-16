package com.egabi.university.mapper;

import com.egabi.university.dto.InstructorDTO;
import com.egabi.university.entity.Course;
import com.egabi.university.entity.Instructor;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InstructorMapper {
    
    /**
     * Converts an Instructor entity to an InstructorDTO.
     *
     * @param instructor the Instructor entity
     * @return the converted InstructorDTO
     */
    @Mappings({
            @Mapping(source = "department.id", target = "departmentId"),
            @Mapping(source = "department.name", target = "departmentName"),
            @Mapping(source = "courses", target = "courseCodes")
    })
    InstructorDTO toDTO(Instructor instructor);
    
    /**
     * Converts an InstructorDTO to an Instructor entity.
     *
     * @param instructorDTO the InstructorDTO
     * @return the converted Instructor entity
     */
    @Mappings({
            @Mapping(source = "departmentId", target = "department.id"),
            @Mapping(source = "courseCodes", target = "courses")
    })
    Instructor toEntity(InstructorDTO instructorDTO);
    
    List<InstructorDTO> toDTOs(List<Instructor> instructors);
    
    /**
     * Updates an existing Instructor entity with values from the DTO.
     * Only non-null fields will overwrite.
     *
     * @param dto        the source DTO
     * @param instructor the target Instructor entity to update
     */
    void updateInstructorFromDto(InstructorDTO dto, @MappingTarget Instructor instructor);
    
    // ================================================================
    // Custom Mappers for Course Codes
    // ================================================================
    
    /**
     * Maps a Course entity to its code.
     *
     * @param course the Course entity
     * @return the course code
     */
    default String map(Course course) {
        return course.getCode();
    }
    
    /**
     * Maps a course code to a Course entity.
     *
     * @param code the course code
     * @return the Course entity with the given code
     */
    default Course map(String code) {
        Course course = new Course();
        course.setCode(code);
        return course;
    }
    
    /**
     * Maps a list of Course entities to a list of course codes.
     *
     * @param courses the list of Course entities
     * @return the list of course codes
     */
    default List<String> mapCoursesToCodes(List<Course> courses) {
        return courses != null ? courses.stream().map(Course::getCode).collect(Collectors.toList()) : null;
    }
    
    /**
     * Maps a list of course codes to a list of Course entities.
     *
     * @param codes the list of course codes
     * @return the list of Course entities with the given codes
     */
    default List<Course> mapCodesToCourses(List<String> codes) {
        return codes != null ? codes.stream().map(code -> {
            Course course = new Course();
            course.setCode(code);
            return course;
        }).collect(Collectors.toList()) : null;
    }
}
