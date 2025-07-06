package com.egabi.university.controller;

import com.egabi.university.dto.LevelDTO;
import com.egabi.university.service.LevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static com.egabi.university.util.ApiPaths.LEVELS;

@RestController
@RequestMapping(LEVELS)
@RequiredArgsConstructor
public class LevelController {
    // This class will handle HTTP requests related to levels such as:
    // - Get all levels
    // - Get level by ID
    // - Create a new level
    // - Update an existing level
    // - Delete a level
    
    private final LevelService levelService;
    
    @GetMapping
    public ResponseEntity<List<LevelDTO>> getAllLevels() {
        return ResponseEntity.ok(levelService.getAllLevels());
    }
    
    @GetMapping("/{levelId}")
    public ResponseEntity<LevelDTO> getLevelById(@PathVariable Long levelId) {
        return ResponseEntity.ok(levelService.getLevelById(levelId));
    }
    
    @PostMapping
    public ResponseEntity<LevelDTO> createLevel(@RequestBody LevelDTO levelDTO) {
        LevelDTO createdLevel = levelService.createLevel(levelDTO);
        URI location = URI.create(LEVELS + "/" + createdLevel.getId());
        return ResponseEntity.created(location).body(createdLevel);
    }
    
    @PutMapping("/{levelId}")
    public ResponseEntity<LevelDTO> updateLevel(@PathVariable Long levelId, @RequestBody LevelDTO levelDTO) {
        LevelDTO updatedLevel = levelService.updateLevel(levelId, levelDTO);
        return ResponseEntity.ok(updatedLevel);
    }
    
    @DeleteMapping("/{levelId}")
    public ResponseEntity<Void> deleteLevel(@PathVariable Long levelId) {
        levelService.deleteLevel(levelId);
        return ResponseEntity.noContent().build();
    }
    
}
