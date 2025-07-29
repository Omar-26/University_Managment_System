package com.egabi.university.mapper;

import com.egabi.university.dto.LevelDTO;
import com.egabi.university.entity.Level;
import org.mapstruct.*;

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
     * Clones a Level entity.
     * This method creates a new Level instance with the same properties as the source entity.
     *
     * @param source the Level entity to clone
     * @return a new Level instance with the same properties
     */
    Level clone(Level source);
    
    /**
     * Updates an existing Level entity with values from a LevelDTO.
     *
     * @param dto    the LevelDTO containing updated values
     * @param entity the Level entity to be updated
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "faculty", ignore = true),
    })
    void updateEntityFromDTO(LevelDTO dto, @MappingTarget Level entity);
    
    /**
     * Converts a list of Level entities to a list of LevelDTOs.
     *
     * @param level the list of Level entities
     * @return the list of converted LevelDTOs
     */
    List<LevelDTO> toDTOs(List<Level> level);
}