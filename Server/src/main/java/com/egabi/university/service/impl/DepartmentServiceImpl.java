package com.egabi.university.service.impl;

import com.egabi.university.dto.DepartmentDTO;
import com.egabi.university.entity.Department;
import com.egabi.university.entity.Faculty;
import com.egabi.university.exception.NotFoundException;
import com.egabi.university.mapper.DepartmentMapper;
import com.egabi.university.repository.DepartmentRepository;
import com.egabi.university.service.DepartmentService;
import com.egabi.university.service.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {
    
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final ValidationService validationService;
    
    @Override
    public List<DepartmentDTO> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return departmentMapper.toDTOs(departments);
    }
    
    @Override
    public DepartmentDTO getDepartmentById(Long id) {
        Department department = departmentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Department with id " + id + " not found", "DEPARTMENT_NOT_FOUND"));
        return departmentMapper.toDTO(department);
    }
    
    @Override
    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        // Check if the department already exists
        if (departmentRepository.existsByNameIgnoreCase(((departmentDTO.getName()))))
            throw new NotFoundException("Department with name " + departmentDTO.getName() + " already exists", "DEPARTMENT_EXISTS");
        
        // Map the DTO to the entity
        Department department = departmentMapper.toEntity(departmentDTO);
        
        // Validate faculty and save the department
        department = validateAndSaveDepartment(department);
        
        return departmentMapper.toDTO(department);
    }
    
    @Override
    public DepartmentDTO updateDepartment(Long departmentId, DepartmentDTO departmentDTO) {
        // Check if the department exists
        if (!departmentRepository.existsById(departmentId))
            throw new NotFoundException("Department with id " + departmentId + " not found", "DEPARTMENT_NOT_FOUND");
        
        // Map the DTO to the entity
        Department updatedDepartment = departmentMapper.toEntity(departmentDTO);
        updatedDepartment.setId(departmentId);
        
        // Validate faculty and save the updated department
        updatedDepartment = validateAndSaveDepartment(updatedDepartment);
        
        return departmentMapper.toDTO(updatedDepartment);
    }
    
    @Override
    public void deleteDepartment(Long id) {
        // Check if the department exists
        if (!departmentRepository.existsById(id))
            throw new NotFoundException("Department with id " + id + " not found", "DEPARTMENT_NOT_FOUND");
        
        //TODO Check that the department is not linked to any students or courses
        
        // Delete the department
        departmentRepository.deleteById(id);
    }
    
    private Department validateAndSaveDepartment(Department department) {
        // Validate faculty is set
        var facultyId = Optional.ofNullable(department.getFaculty()).map(Faculty::getId)
                .orElseThrow(() -> new NotFoundException("Department must be in a faculty", "FACULTY_NOT_FOUND"));
        
        // Validate faculty exists
        var faculty = validationService.validateFacultyExists(facultyId);
        department.setFaculty(faculty);
        
        // Save the department
        return departmentRepository.save(department);
    }
}
