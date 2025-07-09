package com.egabi.university.mapper;

import com.egabi.university.dto.EnrollmentDTO;
import com.egabi.university.entity.Enrollment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * Mapper for converting between Enrollment entity and EnrollmentDTO.
 * Uses MapStruct for automatic mapping.
 */
@Mapper(componentModel = "spring")
public interface EnrollmentMapper {
    
    /**
     * Converts an Enrollment entity to an EnrollmentDTO.
     *
     * @param enrollment the Enrollment entity
     * @return the converted EnrollmentDTO
     */
    @Mappings({
            @Mapping(source = "id.studentId", target = "studentId"),
            @Mapping(source = "id.courseCode", target = "courseCode"),
    })
    EnrollmentDTO toDTO(Enrollment enrollment);
    
    /**
     * Converts an EnrollmentDTO to an Enrollment entity.
     *
     * @param dto the EnrollmentDTO
     * @return the converted Enrollment entity
     */
    @Mappings({
            @Mapping(source = "studentId", target = "id.studentId"),
            @Mapping(source = "courseCode", target = "id.courseCode"),
    })
    Enrollment toEntity(EnrollmentDTO dto);
    
    /**
     * Converts a list of Enrollment entities to a list of EnrollmentDTOs.
     *
     * @param enrollments the list of Enrollment entities
     * @return the list of converted EnrollmentDTOs
     */
    List<EnrollmentDTO> toDTOs(List<Enrollment> enrollments);
    
}