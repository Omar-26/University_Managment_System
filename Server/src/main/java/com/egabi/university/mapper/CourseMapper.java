package com.egabi.university.mapper;

import com.egabi.university.dto.CourseDTO;
import com.egabi.university.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

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
     * Converts a list of Course entities to a list of CourseDTOs.
     *
     * @param courses the list of Course entities
     * @return the list of converted CourseDTOs
     */
    List<CourseDTO> toDTOs(List<Course> courses);
    
}