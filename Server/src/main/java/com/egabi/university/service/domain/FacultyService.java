package com.egabi.university.service.domain;

import com.egabi.university.dto.FacultyDTO;

import java.util.List;

public interface FacultyService {
    
    // ================================================================
    // CRUD Methods
    // ================================================================
    
    /**
     * Retrieves all faculties.
     *
     * @return a list of FacultyDTO objects representing all faculties.
     */
    List<FacultyDTO> getAllFaculties();
    
    /**
     * Retrieves a faculty by its ID.
     *
     * @param id the ID of the faculty to retrieve.
     * @return a FacultyDTO object representing the faculty with the specified ID.
     */
    FacultyDTO getFacultyById(Long id);
    
    /**
     * Creates a new faculty.
     *
     * @param facultyDTO the FacultyDTO object containing the details of the faculty to create.
     * @return the created FacultyDTO object.
     */
    FacultyDTO createFaculty(FacultyDTO facultyDTO);
    
    /**
     * Updates an existing faculty.
     *
     * @param id         the ID of the faculty to update.
     * @param facultyDTO the FacultyDTO object containing the updated details of the faculty.
     * @return the updated FacultyDTO object.
     */
    FacultyDTO updateFaculty(Long id, FacultyDTO facultyDTO);
    
    /**
     * Deletes a faculty by its ID.
     *
     * @param id the ID of the faculty to delete.
     */
    void deleteFaculty(Long id);
}
