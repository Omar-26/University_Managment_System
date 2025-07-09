package com.egabi.university.mapper;

import com.egabi.university.dto.DepartmentDTO;
import com.egabi.university.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper for converting between Department entity and DepartmentDTO.
 * Uses MapStruct for automatic mapping.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DepartmentMapper {
    
    /**
     * Converts a Department entity to a DepartmentDTO.
     *
     * @param department the Department entity
     * @return the converted DepartmentDTO
     */
    @Mapping(source = "faculty.id", target = "facultyId")
    DepartmentDTO toDTO(Department department);
    
    /**
     * Converts a DepartmentDTO to a Department entity.
     *
     * @param dto the DepartmentDTO
     * @return the converted Department entity
     */
    @Mapping(source = "facultyId", target = "faculty.id")
    Department toEntity(DepartmentDTO dto);
    
    /**
     * Converts a list of Department entities to a list of DepartmentDTOs.
     *
     * @param department the list of Department entities
     * @return the list of converted DepartmentDTOs
     */
    List<DepartmentDTO> toDTOs(List<Department> department);
}