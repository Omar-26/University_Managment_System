package com.egabi.university.mapper;

import com.egabi.university.dto.LevelDTO;
import com.egabi.university.entity.Level;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LevelMapper {
    
    @Mapping(source = "faculty.id", target = "facultyId")
    LevelDTO toDTO(Level level);
    
    @Mapping(source = "facultyId", target = "faculty.id")
    Level toEntity(LevelDTO dto);
    
    List<LevelDTO> toDTOs(List<Level> level);
    
}