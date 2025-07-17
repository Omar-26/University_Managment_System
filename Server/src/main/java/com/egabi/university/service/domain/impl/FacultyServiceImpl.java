package com.egabi.university.service.domain.impl;

import com.egabi.university.dto.FacultyDTO;
import com.egabi.university.entity.Faculty;
import com.egabi.university.exception.ConflictException;
import com.egabi.university.mapper.FacultyMapper;
import com.egabi.university.repository.FacultyRepository;
import com.egabi.university.service.domain.FacultyService;
import com.egabi.university.service.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Default implementation of {@link FacultyService}.
 * Provides CRUD operations for managing faculties.
 */
@Service
@RequiredArgsConstructor
public class FacultyServiceImpl implements FacultyService {
    
    private final FacultyRepository facultyRepository;
    private final FacultyMapper facultyMapper;
    private final ValidationService validationService;
    
    // ================================================================
    // CRUD Methods
    // ================================================================
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<FacultyDTO> getAllFaculties() {
        List<Faculty> faculties = facultyRepository.findAll();
        return facultyMapper.toDTOs(faculties);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public FacultyDTO getFacultyById(Long facultyId) {
        Faculty faculty = validationService.getFacultyByIdOrThrow(facultyId);
        return facultyMapper.toDTO(faculty);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public FacultyDTO createFaculty(FacultyDTO facultyDTO) {
        // Validate that the faculty name is unique
        validationService.assertFacultyNameUnique(facultyDTO.getName());
        
        // Map the DTO to the entity
        Faculty faculty = facultyMapper.toEntity(facultyDTO);
        
        // Save the faculty entity
        faculty = facultyRepository.save(faculty);
        
        // Return the saved faculty as a DTO
        return facultyMapper.toDTO(faculty);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public FacultyDTO updateFaculty(Long facultyId, FacultyDTO facultyDTO) {
        // Validate that the faculty exists
        Faculty existingFaculty = validationService.getFacultyByIdOrThrow(facultyId);
        // Check if the faculty name is unique
        if (!existingFaculty.getName().equals(facultyDTO.getName()))
            validationService.assertFacultyNameUnique(facultyDTO.getName());
        
        // Map the DTO to the entity
        Faculty updatedFaculty = facultyMapper.toEntity(facultyDTO);
        updatedFaculty.setId(facultyId);
        
        // Save the updated faculty entity
        updatedFaculty = facultyRepository.save(updatedFaculty);
        
        // Return the saved faculty as a DTO
        return facultyMapper.toDTO(updatedFaculty);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteFaculty(Long facultyId) {
        // Validate that the faculty exists
        Faculty faculty = validationService.getFacultyByIdOrThrow(facultyId);
        
        // Check if the faculty has any associated departments
        if (faculty.getDepartments() != null && !faculty.getDepartments().isEmpty())
            throw new ConflictException("Cannot delete faculty with id " + facultyId +
                    " because it has associated departments", "FACULTY_HAS_DEPARTMENTS");
        
        // Delete the faculty
        facultyRepository.delete(faculty);
    }
}
