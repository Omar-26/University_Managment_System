package com.egabi.university.service.domain;

import com.egabi.university.dto.DepartmentDTO;

import java.util.List;

public interface DepartmentService {
    
    // ================================================================
    // CRUD Methods
    // ================================================================
    
    /**
     * Get all departments available in the system.
     *
     * @return a list of all departments as DepartmentsDTO objects
     */
    List<DepartmentDTO> getAllDepartments();
    
    /**
     * Get Department by ID.
     *
     * @param departmentId the ID of the department
     * @return Department object if exists, null otherwise
     */
    DepartmentDTO getDepartmentById(Long departmentId);
    
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
     * @param departmentId  the ID of the department to be updated
     * @param departmentDTO the department data transfer object containing updated department details
     * @return the updated DepartmentDTO object
     */
    DepartmentDTO updateDepartment(Long departmentId, DepartmentDTO departmentDTO);
    
    /**
     * Delete a department by its ID.
     *
     * @param departmentId the ID of the department to be deleted
     */
    void deleteDepartment(Long departmentId);
    
    // ================================================================
    // Business Logic Methods
    // ================================================================
    
    /**
     * Get all departments by faculty ID.
     *
     * @param facultyId the ID of the faculty
     * @return a list of DepartmentDTO objects associated with the specified faculty
     */
    List<DepartmentDTO> getDepartmentsByFacultyId(Long facultyId);
    
    /**
     * Count departments for a specific faculty.
     *
     * @param facultyId the ID of the faculty.
     * @return the count of departments associated with the faculty.
     */
    Long countDepartmentsByFacultyId(Long facultyId);
}
