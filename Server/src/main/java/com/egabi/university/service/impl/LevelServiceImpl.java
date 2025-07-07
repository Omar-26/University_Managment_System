package com.egabi.university.service.impl;

import com.egabi.university.dto.LevelDTO;
import com.egabi.university.entity.Faculty;
import com.egabi.university.entity.Level;
import com.egabi.university.exception.ConflictException;
import com.egabi.university.exception.NotFoundException;
import com.egabi.university.mapper.LevelMapper;
import com.egabi.university.repository.LevelRepository;
import com.egabi.university.service.LevelService;
import com.egabi.university.service.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LevelServiceImpl implements LevelService {
    
    private final LevelRepository levelRepository;
    private final LevelMapper levelMapper;
    private final ValidationService validationService;
    
    
    @Override
    public List<LevelDTO> getAllLevels() {
        List<Level> levels = levelRepository.findAll();
        return levelMapper.toDTOs(levels);
    }
    
    @Override
    public LevelDTO getLevelById(Long id) {
        Level level = levelRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Level with id " + id + " not found", "LEVEL_NOT_FOUND"));
        return levelMapper.toDTO(level);
    }
    
    @Override
    public LevelDTO createLevel(LevelDTO levelDTO) {
        // Check if the name of the level already exists for this specific faculty
        if (levelRepository.existsByNameAndFacultyId(levelDTO.getName(), levelDTO.getFacultyId()))
            throw new ConflictException("Level with name " + levelDTO.getName() + " already exists in this faculty", "LEVEL_ALREADY_EXISTS");
        
        // Map the DTO to the entity
        Level level = levelMapper.toEntity(levelDTO);
        
        // Validate and save the level
        level = validateAndSaveLevel(level);
        
        // Map the saved entity back to DTO
        return levelMapper.toDTO(level);
    }
    
    @Override
    public LevelDTO updateLevel(Long levelId, LevelDTO levelDTO) {
        // Check if the level exists
        if (!levelRepository.existsById(levelId))
            throw new NotFoundException("Level with id " + levelId + " not found", "LEVEL_NOT_FOUND");
        
        // Map the DTO to the entity
        Level updatedLevel = levelMapper.toEntity(levelDTO);
        updatedLevel.setId(levelId);
        
        // Validate and save the updated level
        updatedLevel = validateAndSaveLevel(updatedLevel);
        
        // Map the saved entity back to DTO
        return levelMapper.toDTO(updatedLevel);
    }
    
    @Override
    public void deleteLevel(Long id) {
        // Check if the level exists
        if (!levelRepository.existsById(id))
            throw new NotFoundException("Level with id " + id + " not found", "LEVEL_NOT_FOUND");
        
        //TODO Check if the level is associated with any students or courses
        
        // Delete the level
        levelRepository.deleteById(id);
    }
    
    private Level validateAndSaveLevel(Level level) {
        // Validate faculty is set
        var facultyId = Optional.ofNullable(level.getFaculty()).map(Faculty::getId)
                .orElseThrow(() -> new NotFoundException("Level must be in a faculty", "FACULTY_NOT_FOUND"));
        
        // Validate faculty exists
        var faculty = validationService.validateFacultyExists(facultyId);
        level.setFaculty(faculty);
        
        // Save the level
        return levelRepository.save(level);
    }
}
