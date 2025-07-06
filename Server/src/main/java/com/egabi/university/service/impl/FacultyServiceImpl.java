package com.egabi.university.service.impl;

import com.egabi.university.dto.FacultyDTO;
import com.egabi.university.entity.Faculty;
import com.egabi.university.exception.ConflictException;
import com.egabi.university.exception.NotFoundException;
import com.egabi.university.mapper.FacultyMapper;
import com.egabi.university.repository.FacultyRepository;
import com.egabi.university.service.FacultyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FacultyServiceImpl implements FacultyService {
    private final FacultyRepository facultyRepository;
    private final FacultyMapper facultyMapper;
    
    @Override
    public List<FacultyDTO> getAllFaculties() {
        List<Faculty> faculties = facultyRepository.findAll();
        return facultyMapper.toDTOs(faculties);
    }
    
    @Override
    public FacultyDTO getFacultyById(Long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Faculty with id " + id + " not found", "FACULTY_NOT_FOUND"));
        return facultyMapper.toDTO(faculty);
    }
    
    @Override
    public FacultyDTO createFaculty(FacultyDTO facultyDTO) {
        if (facultyRepository.existsByNameIgnoreCase((facultyDTO.getName())))
            throw new ConflictException("Faculty with name '" + facultyDTO.getName() + "' already exists", "FACULTY_EXISTS");
        
        // Map the DTO to the entity
        Faculty faculty = facultyMapper.toEntity(facultyDTO);
        
        // Save the faculty entity
        faculty = facultyRepository.save(faculty);
        
        // Return the saved faculty as a DTO
        return facultyMapper.toDTO(faculty);
    }
    
    @Override
    public FacultyDTO updateFaculty(Long id, FacultyDTO facultyDTO) {
        if (!facultyRepository.existsById(id))
            throw new NotFoundException("Faculty with id " + id + " not found", "FACULTY_NOT_FOUND");
        if (facultyRepository.existsByNameIgnoreCase((facultyDTO.getName())))
            throw new ConflictException("Faculty with name '" + facultyDTO.getName() + "' already exists", "FACULTY_EXISTS");
        
        // Map the DTO to the entity
        Faculty updatedFaculty = facultyMapper.toEntity(facultyDTO);
        
        // Save the updated faculty entity
        updatedFaculty = facultyRepository.save(updatedFaculty);
        
        // Return the saved faculty as a DTO
        return facultyMapper.toDTO(updatedFaculty);
    }
    
    @Override
    public void deleteFaculty(Long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Faculty with id " + id + " not found", "FACULTY_NOT_FOUND"));
        
        if (faculty.getDepartments() != null && !faculty.getDepartments().isEmpty())
            throw new ConflictException("Cannot delete faculty with existing departments", "FACULTY_HAS_DEPARTMENTS");
        
        facultyRepository.delete(faculty);
    }
    
    @Override
    public Long countDepartmentsByFacultyId(Long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Faculty with id " + id + " not found", "FACULTY_NOT_FOUND"));
        
        return (long) faculty.getDepartments().size();
    }
    
    @Override
    public Long countStudentsByFacultyId(Long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Faculty with id " + id + " not found", "FACULTY_NOT_FOUND"));
        
        return faculty.getDepartments().stream().mapToLong(department -> department.getStudents().size()).sum();
    }
    
}
