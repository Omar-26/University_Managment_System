package com.egabi.university.mapper;

import com.egabi.university.dto.StudentDTO;
import com.egabi.university.entity.Student;
import org.mapstruct.*;

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
            @Mapping(source = "user.id", target = "userId"),
            @Mapping(source = "department.id", target = "departmentId"),
            @Mapping(source = "department.name", target = "departmentName"),
            @Mapping(source = "level.id", target = "levelId"),
            @Mapping(source = "level.name", target = "levelName"),
            @Mapping(source = "department.faculty.id", target = "facultyId"),
            @Mapping(source = "department.faculty.name", target = "facultyName"),
    })
    StudentDTO toDTO(Student student);
    
    /**
     * Converts a StudentDTO to a Student entity.
     *
     * @param studentDTO the StudentDTO
     * @return the converted Student entity
     */
    @Mappings({
            @Mapping(target = "user", ignore = true),
            @Mapping(source = "departmentId", target = "department.id"),
            @Mapping(source = "levelId", target = "level.id"),
    })
    Student toEntity(StudentDTO studentDTO);
    
    /**
     * Clones a Student entity.
     * This method creates a new Student instance with the same properties as the source entity.
     *
     * @param source the Student entity to clone
     * @return a new Student instance with the same properties
     */
    Student clone(Student source);
    
    /**
     * Updates an existing Student entity with values from a StudentDTO.
     *
     * @param dto     the StudentDTO containing updated values
     * @param student the Student entity to update
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(source = "departmentId", target = "department.id"),
            @Mapping(source = "levelId", target = "level.id"),
            @Mapping(target = "enrollments", ignore = true)
    })
    void updateEntityFromDTO(StudentDTO dto, @MappingTarget Student student);
    
    /**
     * Converts a list of Student entities to a list of StudentDTOs.
     *
     * @param students the list of Student entities
     * @return the list of converted StudentDTOs
     */
    List<StudentDTO> toDTOs(List<Student> students);
}
