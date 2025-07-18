package com.egabi.university.controller;

import com.egabi.university.dto.LevelDTO;
import com.egabi.university.service.academic.LevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.egabi.university.util.ApiPaths.LEVELS;

/**
 * REST Controller for managing levels.
 * Handles HTTP requests related to Level CRUD actions.
 * Provides endpoints to create, read, update, and delete levels.
 */
@RestController
@RequestMapping(LEVELS)
@RequiredArgsConstructor
public class LevelController {
    
    private final LevelService levelService;
    
    /**
     * Retrieves all levels.
     *
     * @return List of LevelDTO
     */
    @GetMapping
    public ResponseEntity<List<LevelDTO>> getAllLevels() {
        return ResponseEntity.ok(levelService.getAllLevels());
    }
    
    /**
     * Retrieves a level by its ID.
     *
     * @param levelId the ID of the level
     * @return LevelDTO
     */
    @GetMapping("/{levelId}")
    public ResponseEntity<LevelDTO> getLevelById(@PathVariable Long levelId) {
        return ResponseEntity.ok(levelService.getLevelById(levelId));
    }
    
    /**
     * Creates a new level.
     *
     * @param levelDTO the LevelDTO containing the details of the level to create
     * @return ResponseEntity with the created LevelDTO and location URI
     */
    @PostMapping
    public ResponseEntity<LevelDTO> createLevel(@RequestBody LevelDTO levelDTO) {
        LevelDTO createdLevel = levelService.createLevel(levelDTO);
        URI location = URI.create(LEVELS + "/" + createdLevel.getId());
        return ResponseEntity.created(location).body(createdLevel);
    }
    
    /**
     * Updates an existing level.
     *
     * @param levelId  the ID of the level to update
     * @param levelDTO the LevelDTO containing the updated details
     * @return ResponseEntity with the updated LevelDTO
     */
    @PutMapping("/{levelId}")
    public ResponseEntity<LevelDTO> updateLevel(@PathVariable Long levelId, @RequestBody LevelDTO levelDTO) {
        LevelDTO updatedLevel = levelService.updateLevel(levelId, levelDTO);
        return ResponseEntity.ok(updatedLevel);
    }
    
    /**
     * Deletes a level by its ID.
     *
     * @param levelId the ID of the level to delete
     * @return ResponseEntity with no content status
     */
    @DeleteMapping("/{levelId}")
    public ResponseEntity<Void> deleteLevel(@PathVariable Long levelId) {
        levelService.deleteLevel(levelId);
        return ResponseEntity.noContent().build();
    }
}
