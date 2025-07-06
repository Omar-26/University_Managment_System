package com.egabi.university.mapper;

import com.egabi.university.dto.DepartmentDTO;
import com.egabi.university.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DepartmentMapper {
    
    @Mapping(source = "faculty.id", target = "facultyId")
    DepartmentDTO toDTO(Department department);
    
    
    @Mapping(source = "facultyId", target = "faculty.id")
    Department toEntity(DepartmentDTO dto);
    
    List<DepartmentDTO> toDTOs(List<Department> department);
    
}