package com.egabi.university.mapper;

import com.egabi.university.dto.StudentDTO;
import com.egabi.university.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper for converting between Student entity and StudentDTO.
 * Uses MapStruct for automatic mapping.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentMapper {
    
    /**
     * Converts a Student entity to a StudentDTO.
     *
     * @param student the Student entity
     * @return the converted StudentDTO
     */
    @Mappings({
            @Mapping(source = "department.id", target = "departmentId"),
            @Mapping(source = "level.id", target = "levelId")
    })
    StudentDTO toDTO(Student student);
    
    /**
     * Converts a StudentDTO to a Student entity.
     *
     * @param studentDTO the StudentDTO
     * @return the converted Student entity
     */
    @Mappings({
            @Mapping(source = "departmentId", target = "department.id"),
            @Mapping(source = "levelId", target = "level.id")
    })
    Student toEntity(StudentDTO studentDTO);
    
    /**
     * Converts a list of Student entities to a list of StudentDTOs.
     *
     * @param students the list of Student entities
     * @return the list of converted StudentDTOs
     */
    List<StudentDTO> toDTOs(List<Student> students);
}
