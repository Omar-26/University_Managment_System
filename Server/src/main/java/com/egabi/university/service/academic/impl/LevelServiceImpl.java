package com.egabi.university.service.academic.impl;

import com.egabi.university.dto.LevelDTO;
import com.egabi.university.entity.Faculty;
import com.egabi.university.entity.Level;
import com.egabi.university.exception.BadRequestException;
import com.egabi.university.exception.ConflictException;
import com.egabi.university.exception.NotFoundException;
import com.egabi.university.mapper.LevelMapper;
import com.egabi.university.repository.LevelRepository;
import com.egabi.university.service.academic.LevelService;
import com.egabi.university.service.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Default implementation of {@link LevelService}.
 * Provides CRUD operations for managing levels.
 */
@Service
@RequiredArgsConstructor
public class LevelServiceImpl implements LevelService {
    
    private final LevelRepository levelRepository;
    private final LevelMapper levelMapper;
    private final ValidationService validationService;
    
    // ================================================================
    // CRUD Methods
    // ================================================================
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<LevelDTO> getAllLevels() {
        List<Level> levels = levelRepository.findAll();
        return levelMapper.toDTOs(levels);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public LevelDTO getLevelById(Long levelId) {
        Level level = validationService.getLevelByIdOrThrow(levelId);
        return levelMapper.toDTO(level);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public LevelDTO createLevel(LevelDTO levelDTO) {
        // Check if the name of the level already exists for this specific faculty
        validationService.assertLevelNameUniquePerFaculty(levelDTO.getName(), levelDTO.getFacultyId());
        
        // Map the DTO to the entity
        Level level = levelMapper.toEntity(levelDTO);
        
        // Validate and save the level
        level = validateAndSaveLevel(level);
        
        // Map the saved entity back to DTO
        return levelMapper.toDTO(level);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public LevelDTO updateLevel(Long levelId, LevelDTO levelDTO) {
        // Validate that the level exists
        Level existingLevel = validationService.getLevelByIdOrThrow(levelId);
        
        // Check if the sender changed any of the fields
        Level newLevel = levelMapper.clone(existingLevel);
        levelMapper.updateEntityFromDTO(levelDTO, newLevel);
        boolean changed = !newLevel.equals(existingLevel);
        
        if (changed) {
            // Validate that the level name is unique for the faculty
            validationService.assertLevelNameUniquePerFaculty(levelDTO.getName(), levelDTO.getFacultyId());
            
            // Update the existing level entity the name from the DTO [id and faculty are ignored]
            levelMapper.updateEntityFromDTO(levelDTO, existingLevel);
            
            // save the updated level
            levelRepository.save(existingLevel);
        }
        
        // Map the updated entity back to DTO
        return levelMapper.toDTO(existingLevel);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteLevel(Long levelId) {
        // Check if the level exists
        Level level = validationService.getLevelByIdOrThrow(levelId);
        
        //Check if the level is associated with any students or courses
        if (!level.getStudents().isEmpty() || !level.getCourses().isEmpty())
            throw new ConflictException(
                    "Cannot delete level with id " + levelId + " because it has associated students or courses",
                    "LEVEL_DELETE_CONFLICT");
        
        // Delete the level
        levelRepository.delete(level);
    }
    
    // ================================================================
    // Helper methods
    // ================================================================
    
    /**
     * Validates the level's faculty and saves the level.
     *
     * @param level The level to validate and save.
     * @return The saved level.
     * @throws NotFoundException if the faculty is not found.
     */
    private Level validateAndSaveLevel(Level level) {
        // Validate faculty is set
        Long facultyId = Optional.ofNullable(level.getFaculty()).map(Faculty::getId)
                .orElseThrow(() -> new BadRequestException("Level must be in a faculty", "FACULTY_NOT_PROVIDED"));
        
        // Validate faculty exists
        Faculty faculty = validationService.getFacultyByIdOrThrow(facultyId);
        level.setFaculty(faculty);
        
        // Save the level
        return levelRepository.save(level);
    }
}
