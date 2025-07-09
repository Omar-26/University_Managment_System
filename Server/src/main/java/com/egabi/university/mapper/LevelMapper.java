package com.egabi.university.mapper;

import com.egabi.university.dto.LevelDTO;
import com.egabi.university.entity.Level;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * Mapper for converting between Level entity and LevelDTO.
 * Uses MapStruct for automatic mapping.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LevelMapper {
    
    /**
     * Converts a Level entity to a LevelDTO.
     *
     * @param level the Level entity
     * @return the converted LevelDTO
     */
    @Mapping(source = "faculty.id", target = "facultyId")
    LevelDTO toDTO(Level level);
    
    /**
     * Converts a LevelDTO to a Level entity.
     *
     * @param dto the LevelDTO
     * @return the converted Level entity
     */
    @Mapping(source = "facultyId", target = "faculty.id")
    Level toEntity(LevelDTO dto);
    
    /**
     * Converts a list of Level entities to a list of LevelDTOs.
     *
     * @param level the list of Level entities
     * @return the list of converted LevelDTOs
     */
    List<LevelDTO> toDTOs(List<Level> level);
}