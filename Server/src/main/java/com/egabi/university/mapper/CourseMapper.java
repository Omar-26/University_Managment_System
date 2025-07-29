package com.egabi.university.mapper;

import com.egabi.university.dto.CourseDTO;
import com.egabi.university.entity.Course;
import org.mapstruct.*;

import java.util.List;

/**
 * Mapper for converting between Course entity and CourseDTO.
 * Uses MapStruct for automatic mapping.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseMapper {
    
    /**
     * Converts a Course entity to a CourseDTO.
     *
     * @param course the Course entity
     * @return the converted CourseDTO
     */
    @Mappings({
            @Mapping(source = "department.id", target = "departmentId"),
            @Mapping(source = "level.id", target = "levelId")
    })
    CourseDTO toDTO(Course course);
    
    /**
     * Converts a CourseDTO to a Course entity.
     *
     * @param dto the CourseDTO
     * @return the converted Course entity
     */
    @Mappings({
            @Mapping(source = "departmentId", target = "department.id"),
            @Mapping(source = "levelId", target = "level.id")
    })
    Course toEntity(CourseDTO dto);
    
    /**
     * Clones a Course entity.
     *
     * @param source the Course entity to clone
     * @return a new Course entity that is a clone of the source
     */
    Course clone(Course source);
    
    /**
     * Updates an existing Course entity from a CourseDTO.
     *
     * @param dto    the CourseDTO containing updated data
     * @param entity the Course entity to update
     */
    @Mappings({
            @Mapping(target = "code", ignore = true),
            @Mapping(target = "department.id", ignore = true),
            @Mapping(target = "enrollments", ignore = true),
            @Mapping(target = "instructors", ignore = true),
            @Mapping(source = "levelId", target = "level.id")
    })
    void updateEntityFromDTO(CourseDTO dto, @MappingTarget Course entity);
    
    /**
     * Converts a list of Course entities to a list of CourseDTOs.
     *
     * @param courses the list of Course entities
     * @return the list of converted CourseDTOs
     */
    List<CourseDTO> toDTOs(List<Course> courses);
    
}