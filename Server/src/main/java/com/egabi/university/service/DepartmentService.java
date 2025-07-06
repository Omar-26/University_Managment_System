package com.egabi.university.service;

import com.egabi.university.dto.DepartmentDTO;

import java.util.List;

public interface DepartmentService {
    /**
     * Get all departments available in the system.
     *
     * @return a list of all departments as DepartmentsDTO objects
     */
    List<DepartmentDTO> getAllDepartments();
    
    /**
     * Get Department by ID.
     *
     * @param id the ID of the department
     * @return Department object if exists, null otherwise
     */
    DepartmentDTO getDepartmentById(Long id);
    
    /**
     * Create a new department.
     *
     * @param departmentDTO the department data transfer object containing department details
     * @return the created DepartmentDTO object
     */
    DepartmentDTO createDepartment(DepartmentDTO departmentDTO);
    
    /**
     * Update an existing department.
     *
     * @param departmentDTO the department data transfer object containing updated department details
     * @return the updated DepartmentDTO object
     */
    DepartmentDTO updateDepartment(DepartmentDTO departmentDTO);
    
    /**
     * Delete a department by its ID.
     *
     * @param id the ID of the department to be deleted
     */
    void deleteDepartment(Long id);
}
