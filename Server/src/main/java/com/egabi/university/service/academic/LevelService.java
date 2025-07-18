package com.egabi.university.service.academic;

import com.egabi.university.dto.LevelDTO;

import java.util.List;

public interface LevelService {
    /**
     * Get all levels available in the system.
     *
     * @return a list of all levels as LevelDTO objects
     */
    List<LevelDTO> getAllLevels();
    
    /**
     * Get Level by ID.
     *
     * @param levelId the ID of the level
     * @return LevelDTO object if exists, null otherwise
     */
    LevelDTO getLevelById(Long levelId);
    
    /**
     * Create a new level.
     *
     * @param levelDTO the level data transfer object containing level details
     * @return the created LevelDTO object
     */
    LevelDTO createLevel(LevelDTO levelDTO);
    
    /**
     * Update an existing level.
     *
     * @param levelId  the ID of the level to be updated
     * @param levelDTO the level data transfer object containing updated level details
     * @return the updated LevelDTO object
     */
    LevelDTO updateLevel(Long levelId, LevelDTO levelDTO);
    
    /**
     * Delete a level by its ID.
     *
     * @param levelId the ID of the level to be deleted
     */
    void deleteLevel(Long levelId);
}
