package com.egabi.university.mapper;

import com.egabi.university.dto.FacultyDTO;
import com.egabi.university.entity.Faculty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * Mapper for converting between Faculty entity and FacultyDTO.
 * Uses MapStruct for automatic mapping.
 */
@Mapper(componentModel = "spring")
public interface FacultyMapper {
    
    /**
     * Converts a Faculty entity to a FacultyDTO.
     *
     * @param faculty the Faculty entity
     * @return the converted FacultyDTO
     */
    FacultyDTO toDTO(Faculty faculty);
    
    /**
     * Converts a FacultyDTO to a Faculty entity.
     *
     * @param dto the FacultyDTO
     * @return the converted Faculty entity
     */
    Faculty toEntity(FacultyDTO dto);
    
    /**
     * Clones a Faculty entity.
     * This method creates a new Faculty instance with the same properties as the source entity.
     *
     * @param source the Faculty entity to clone
     * @return a new Faculty instance with the same properties
     */
    Faculty clone(Faculty source);
    
    /**
     * Updates an existing Faculty entity with values from a FacultyDTO.
     *
     * @param dto     the FacultyDTO containing updated values
     * @param faculty the Faculty entity to update
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "departments", ignore = true)
    })
    void updateEntityFromDTO(FacultyDTO dto, @MappingTarget Faculty faculty);
    
    /**
     * Converts a list of Faculty entities to a list of FacultyDTOs.
     *
     * @param faculties the list of Faculty entities
     * @return the list of converted FacultyDTOs
     */
    List<FacultyDTO> toDTOs(List<Faculty> faculties);
}

//TODO make all the updates ignore the id field
