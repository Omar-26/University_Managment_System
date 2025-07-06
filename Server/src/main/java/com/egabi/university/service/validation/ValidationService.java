package com.egabi.university.service.validation;

import com.egabi.university.entity.Department;
import com.egabi.university.entity.Faculty;
import com.egabi.university.entity.Level;
import com.egabi.university.exception.NotFoundException;

/**
 * A service for validating and resolving entity relationships.
 * This helps enforce referential integrity rules across services.
 */
public interface ValidationService {
    
    /**
     * Validates that a Faculty exists for the given ID.
     * If not, throws a NotFoundException.
     *
     * @param facultyId the ID to validate
     * @return the Faculty entity if found
     * @throws NotFoundException if the faculty does not exist
     */
    Faculty validateFaculty(Long facultyId);
    
    /**
     * Validates that a Department exists for the given ID.
     * If not, throws a NotFoundException.
     *
     * @param departmentId the ID to validate
     * @return the Department entity if found
     * @throws NotFoundException if the department does not exist
     */
    Department validateDepartment(Long departmentId);
    
    /**
     * Validates that a Level exists for the given ID.
     * If not, throws a NotFoundException.
     *
     * @param levelId the ID to validate
     * @return the Level entity if found
     * @throws NotFoundException if the level does not exist
     */
    Level validateLevel(Long levelId);
}
