package com.egabi.university.service.academic.impl;

import com.egabi.university.dto.DepartmentDTO;
import com.egabi.university.entity.Department;
import com.egabi.university.entity.Faculty;
import com.egabi.university.exception.BadRequestException;
import com.egabi.university.exception.ConflictException;
import com.egabi.university.exception.NotFoundException;
import com.egabi.university.mapper.DepartmentMapper;
import com.egabi.university.repository.DepartmentRepository;
import com.egabi.university.service.academic.DepartmentService;
import com.egabi.university.service.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Default implementation of {@link DepartmentService}.
 * Provides CRUD operations for managing departments.
 */
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final ValidationService validationService;
    
    // ================================================================
    // CRUD Methods
    // ================================================================
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<DepartmentDTO> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return departmentMapper.toDTOs(departments);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public DepartmentDTO getDepartmentById(Long departmentId) {
        Department department = validationService.getDepartmentByIdOrThrow(departmentId);
        return departmentMapper.toDTO(department);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        // Check if the department already exists
        validationService.assertDepartmentNameUnique(departmentDTO.getName());
        
        // Map the DTO to the entity
        Department department = departmentMapper.toEntity(departmentDTO);
        
        // Validate faculty and save the department
        department = validateAndSaveDepartment(department);
        
        // Return the created department DTO
        return departmentMapper.toDTO(department);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public DepartmentDTO updateDepartment(Long departmentId, DepartmentDTO departmentDTO) {
        // Validate that the department exists
        Department existingDepartment = validationService.getDepartmentByIdOrThrow(departmentId);
        
        // Check if the sender changed any of the fields
        Department newDepartment = departmentMapper.clone(existingDepartment);
        departmentMapper.updateEntityFromDTO(departmentDTO, newDepartment);
        boolean changed = !newDepartment.equals(existingDepartment);
        
        if (changed) {
            // Validate that the department name is unique
            validationService.assertDepartmentNameUnique(departmentDTO.getName());
            
            // Update the existing department entity the name from the DTO [id and faculty are ignored]
            departmentMapper.updateEntityFromDTO(departmentDTO, existingDepartment);
            
            // save the updated department
            departmentRepository.save(existingDepartment);
        }
        
        // Map the updated entity back to DTO
        return departmentMapper.toDTO(existingDepartment);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteDepartment(Long departmentId) {
        // Check if the department exists
        Department department = validationService.getDepartmentByIdOrThrow(departmentId);
        
        // Check if the department has any associated courses or students
        if (!department.getStudents().isEmpty() || !department.getCourses().isEmpty())
            throw new ConflictException(
                    "Cannot delete department with id " + departmentId + " because it has associated students or courses",
                    "DEPARTMENT_HAS_ASSOCIATIONS");
        
        // Delete the department
        departmentRepository.delete(department);
    }
    
    // ================================================================
    // Business Logic Methods
    // ================================================================
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public List<DepartmentDTO> getDepartmentsByFacultyId(Long facultyId) {
        // Validate faculty existence
        validationService.assertFacultyExists(facultyId);
        
        // Fetch departments by faculty ID
        List<Department> departments = departmentRepository.findAllByFacultyId(facultyId);
        return departmentMapper.toDTOs(departments);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Long countDepartmentsByFacultyId(Long facultyId) {
        // Validate faculty existence
        validationService.assertFacultyExists(facultyId);
        
        Long count = departmentRepository.countByFacultyId(facultyId);
        return count != null ? count : 0L;
    }
    
    // ================================================================
    // Helper methods
    // ================================================================
    
    /**
     * Validates the department's faculty and saves the department.
     * <p>
     * //     * @param department The department to validate and save.
     *
     * @return The saved department.
     * @throws NotFoundException if the faculty is not found.
     */
    private Department validateAndSaveDepartment(Department department) {
        // Validate faculty
        // 1. Check if the faculty is set
        Long facultyId = Optional.ofNullable(department.getFaculty()).map(Faculty::getId)
                .orElseThrow(() -> new BadRequestException("Department must be in a faculty", "FACULTY_NOT_PROVIDED"));
        
        // 2. Check if the faculty exists
        Faculty faculty = validationService.getFacultyByIdOrThrow(facultyId);
        department.setFaculty(faculty);
        
        // Save the department
        return departmentRepository.save(department);
    }
}
