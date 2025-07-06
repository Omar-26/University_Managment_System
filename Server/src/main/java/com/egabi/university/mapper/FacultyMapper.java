package com.egabi.university.mapper;

import com.egabi.university.dto.FacultyDTO;
import com.egabi.university.entity.Faculty;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FacultyMapper {
    
    FacultyDTO toDTO(Faculty faculty);
    
    Faculty toEntity(FacultyDTO dto);
    
    List<FacultyDTO> toDTOs(List<Faculty> faculties);
    
}
