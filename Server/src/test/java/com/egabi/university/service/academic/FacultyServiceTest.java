package com.egabi.university.service.academic;

import com.egabi.university.dto.FacultyDTO;
import com.egabi.university.entity.Department;
import com.egabi.university.entity.Faculty;
import com.egabi.university.exception.ConflictException;
import com.egabi.university.exception.NotFoundException;
import com.egabi.university.mapper.FacultyMapper;
import com.egabi.university.repository.FacultyRepository;
import com.egabi.university.service.academic.impl.FacultyServiceImpl;
import com.egabi.university.service.validation.ValidationService;
import com.egabi.university.util.TestAssertionUtils;
import com.egabi.university.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Comprehensive unit tests for {@link FacultyServiceImpl} using JUnit 5 and Mockito.
 * <p>
 * These tests verify the service logic in isolation from external dependencies:
 * <ul>
 *   <li>{@link FacultyRepository} for database operations</li>
 *   <li>{@link ValidationService} for business validation logic</li>
 * </ul>
 * <p>
 * <b>Focus areas:</b>
 * <ul>
 *   <li>Repository interaction correctness</li>
 *   <li>Validation logic invocation</li>
 *   <li>Proper exception handling and scenario coverage</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class FacultyServiceTest {
    
    // ================================================================
    // Mocks and Dependencies
    // ================================================================
    
    @Mock
    private FacultyRepository facultyRepository;
    
    @Mock
    private ValidationService validationService;
    
    private FacultyServiceImpl facultyService;
    
    private Faculty faculty;
    private FacultyDTO facultyDTO;
    
    // ================================================================
    // Setup : Arrange common test fixtures
    // ================================================================
    
    @BeforeEach
    void setUp() {
        // Initialize the mapper using MapStruct factory
        FacultyMapper facultyMapper = Mappers.getMapper(FacultyMapper.class);
        
        // Set the validation service in the TestAssertionUtils
        TestAssertionUtils.setValidationService(validationService);
        
        // Create the service under test with mocked dependencies
        facultyService = new FacultyServiceImpl(facultyRepository, facultyMapper, validationService);
        
        // Prepare a Faculty entity with [id = 1 and name = "Engineering"]
        faculty = TestDataFactory.buildFaculty();
        // Prepare a simple DTO from the entity for input/output tests
        facultyDTO = TestDataFactory.buildFacultyDTO();
    }
    
    // ================================================================
    // Positive Test Cases
    // ================================================================
    
    // Get ============================================================
    
    /**
     * Unit test for {@link FacultyService#getAllFaculties()}.
     * <p>
     * <b>Scenario:</b> When faculties exist in the database, the service should:
     * <ul>
     *   <li>Fetch all faculty entities via {@link FacultyRepository#findAll()}</li>
     *   <li>Map the entities to {@link FacultyDTO} instances correctly</li>
     *   <li>Return a list containing all mapped DTOs</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The returned list has the expected size and content</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> The list contains all faculties mapped to DTOs.
     */
    @Test
    @DisplayName("Should return all faculties when faculties exist")
    void shouldReturnAllFaculties_whenFacultiesExist() {
        // Arrange: Prepare mocks and inputs
        when(facultyRepository.findAll()).thenReturn(List.of(faculty));
        
        // Act: Call the method under test
        List<FacultyDTO> result = facultyService.getAllFaculties();
        
        // Assert: Verify output and interactions
        assertThat(result)
                .as("Returned list should contain exactly one faculty")
                .hasSize(1);
        assertThat(result.getFirst())
                .as("Returned faculty should match the mock entity")
                .extracting(FacultyDTO::getId, FacultyDTO::getName)
                .containsExactly(faculty.getId(), faculty.getName());
        
        verify(facultyRepository).findAll();
        verifyNoMoreInteractions(validationService, facultyRepository);
    }
    
    /**
     * Unit test for {@link FacultyService#getFacultyById(Long)}.
     * <p>
     * <b>Scenario:</b> When a faculty with the specified ID exists, the service should:
     * <ul>
     *   <li>Fetch the faculty entity via {@link ValidationService#getFacultyByIdOrThrow(Long)}</li>
     *   <li>Map the entity to a {@link FacultyDTO} instance</li>
     *   <li>Return the mapped DTO</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The returned DTO has the expected ID and name</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> The DTO contains the correct faculty data.
     */
    @Test
    @DisplayName("Should return faculty by ID when it exists")
    void shouldReturnFacultyById_whenFacultyExists() {
        // Arrange: Prepare mocks and inputs
        when(validationService.getFacultyByIdOrThrow(faculty.getId())).thenReturn(faculty);
        
        // Act: Call the method under test
        FacultyDTO result = facultyService.getFacultyById(faculty.getId());
        
        // Assert: Verify output and interactions
        assertThat(result)
                .as("Returned faculty should match the mock entity")
                .extracting(FacultyDTO::getId, FacultyDTO::getName)
                .containsExactly(faculty.getId(), faculty.getName());
        
        verify(validationService).getFacultyByIdOrThrow(faculty.getId());
        verifyNoMoreInteractions(validationService);
    }
    
    // Create ============================================================
    
    /**
     * Unit test for {@link FacultyService#createFaculty(FacultyDTO)}.
     * <p>
     * <b>Scenario:</b> When creating a new faculty with a unique name, the service should:
     * <ul>
     *   <li>Validate that the faculty name is unique via {@link ValidationService#assertFacultyNameUnique(String)}</li>
     *   <li>Map the DTO to a {@link Faculty} entity</li>
     *   <li>Save the entity via {@link FacultyRepository#save(Object)}</li>
     *   <li>Return the saved entity as a DTO</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The returned DTO has the expected ID and name</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> The DTO contains the correct faculty data.
     */
    @Test
    @DisplayName("Should create a new faculty with unique name")
    void shouldCreateFaculty_whenNameIsUnique() {
        // Arrange: Prepare mocks and inputs
        doNothing().when(validationService).assertFacultyNameUnique(anyString());
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        
        // Act: Call the method under test
        FacultyDTO result = facultyService.createFaculty(facultyDTO);
        
        // Assert: Verify output and interactions
        assertThat(result)
                .as("Returned faculty should match the mock entity")
                .extracting(FacultyDTO::getId, FacultyDTO::getName)
                .containsExactly(faculty.getId(), faculty.getName());
        
        verify(validationService).assertFacultyNameUnique(facultyDTO.getName());
        verify(facultyRepository).save(any(Faculty.class));
        verifyNoMoreInteractions(validationService);
    }
    
    // Update ============================================================
    
    /**
     * Unit test for {@link FacultyService#updateFaculty(Long, FacultyDTO)}.
     * <p>
     * <b>Scenario:</b> When updating an existing faculty with the same name, the service should:
     * <ul>
     *   <li>Validate that the faculty exists via {@link ValidationService#getFacultyByIdOrThrow(Long)}</li>
     *   <li>Save the entity without changing the name</li>
     *   <li>Return the updated entity as a DTO</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The returned DTO has the same ID and name as before</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> The DTO contains the same faculty data.
     */
    @Test
    @DisplayName("Should return unchanged faculty when updating with same name")
    void shouldReturnUnchangedFaculty_whenExistsAndUpdatingWithSameName() {
        // Arrange: Prepare mocks and inputs
        when(validationService.getFacultyByIdOrThrow(faculty.getId())).thenReturn(faculty);
        
        // Act: Call the method under test
        FacultyDTO result = facultyService.updateFaculty(faculty.getId(), facultyDTO);
        
        // Assert: Verify output and interactions
        assertThat(result)
                .as("Returned faculty should match the original entity")
                .extracting(FacultyDTO::getId, FacultyDTO::getName)
                .containsExactly(faculty.getId(), faculty.getName());
        
        verify(validationService).getFacultyByIdOrThrow(faculty.getId());
        verify(validationService, never()).assertFacultyNameUnique(anyString());
        verify(facultyRepository, never()).save(any(Faculty.class));
        verifyNoMoreInteractions(validationService);
    }
    
    /**
     * Unit test for {@link FacultyService#updateFaculty(Long, FacultyDTO)}.
     * <p>
     * <b>Scenario:</b> When updating an existing faculty with a new unique name, the service should:
     * <ul>
     *   <li>Validate that the faculty exists via {@link ValidationService#getFacultyByIdOrThrow(Long)}</li>
     *   <li>Validate that the new name is unique via {@link ValidationService#assertFacultyNameUnique(String)}</li>
     *   <li>Map the DTO to a {@link Faculty} entity</li>
     *   <li>Save the updated entity via {@link FacultyRepository#save(Object)}</li>
     *   <li>Return the updated entity as a DTO</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The returned DTO has the expected ID and new name</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> The DTO contains the updated faculty data.
     */
    @Test
    @DisplayName("Should update faculty with new unique name")
    void shouldUpdateFaculty_whenIdExistsAndNameIsUnique() {
        // Arrange: Prepare mocks and inputs
        FacultyDTO updatedDTO = TestDataFactory.buildFacultyDTO(faculty.getId(), "New Engineering");
        
        when(validationService.getFacultyByIdOrThrow(faculty.getId())).thenReturn(faculty);
        doNothing().when(validationService).assertFacultyNameUnique(updatedDTO.getName());
        when(facultyRepository.save(any(Faculty.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act: Call the method under test
        FacultyDTO result = facultyService.updateFaculty(faculty.getId(), updatedDTO);
        
        // Assert: Verify output and interactions
        assertThat(result)
                .as("Returned faculty should match the mock updated entity")
                .extracting(FacultyDTO::getId, FacultyDTO::getName)
                .containsExactly(faculty.getId(), updatedDTO.getName());
        
        verify(validationService).getFacultyByIdOrThrow(faculty.getId());
        verify(validationService).assertFacultyNameUnique(updatedDTO.getName());
        verify(facultyRepository).save(any(Faculty.class));
        verifyNoMoreInteractions(validationService, facultyRepository);
    }
    
    // Delete ============================================================
    
    /**
     * Unit test for {@link FacultyService#deleteFaculty(Long)}.
     * <p>
     * <b>Scenario:</b> When deleting a faculty by ID that exists and has no associations,
     * the service should:
     * <ul>
     *   <li>Validate that the faculty exists via {@link ValidationService#getFacultyByIdOrThrow(Long)}</li>
     *   <li>Check for associations (departments) and ensure there are none</li></li>
     *   <li>Delete the faculty entity via {@link FacultyRepository#delete(Object)}</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>No exceptions are thrown</li>
     *   <li>The repository delete method is called with the correct entity</li>
     *   <li>No unexpected interactions occur with the validation service</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> The faculty is deleted successfully.
     */
    @Test
    @DisplayName("Should delete faculty by ID")
    void shouldDeleteFacultyById_whenFacultyExistsAndHasNoAssociations() {
        // Arrange: Prepare mocks and inputs
        when(validationService.getFacultyByIdOrThrow(faculty.getId())).thenReturn(faculty);
        
        // Act: Call the method under test
        facultyService.deleteFaculty(faculty.getId());
        
        // Assert: Verify interactions
        verify(validationService).getFacultyByIdOrThrow(faculty.getId());
        verify(facultyRepository).delete(faculty);
        verifyNoMoreInteractions(validationService);
    }
    
    // ================================================================
    // Negative Test Cases
    // ================================================================
    
    // Get ============================================================
    
    /**
     * Unit test for {@link FacultyService#getFacultyById(Long)}.
     * <p>
     * <b>Scenario:</b> When attempting to retrieve a faculty by an ID that does not exist,
     * the service should:
     * <ul>
     *   <li>Call {@link ValidationService#getFacultyByIdOrThrow(Long)} to check existence</li>
     *   <li>Throw a {@link NotFoundException} if the faculty is not found</li>
     *   <li>Never call {@link FacultyRepository} since the faculty does not exist</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception type is thrown</li>
     *   <li>The validation service is called with the expected ID</li>
     *   <li>The repository is not interacted with</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> {@code NotFoundException} is thrown and no repository interaction occurs.
     */
    @Test
    @DisplayName("Should throw NotFoundException when faculty ID does not exist")
    void shouldThrowNotFoundException_whenFacultyIdDoesNotExist() {
        // Arrange, Act & Assert: Use helper to assert not found behavior
        TestAssertionUtils.assertFacultyNotFound(() -> facultyService.getFacultyById(TestDataFactory.NON_EXISTENT_ID));
    }
    
    // Create ============================================================
    
    /**
     * Unit test for {@link FacultyService#createFaculty(FacultyDTO)}.
     * <p>
     * <b>Scenario:</b> When attempting to create a faculty with a name that already exists,
     * the service should:
     * <ul>
     *   <li>Call {@link ValidationService#assertFacultyNameUnique(String)} to check uniqueness</li>
     *   <li>Throw a {@link ConflictException} if the name is already taken</li>
     *   <li>Never call {@link FacultyRepository} to persist the duplicate faculty</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception type is thrown</li>
     *   <li>The validation service is called with the expected name</li>
     *   <li>The repository is not interacted with</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> {@code ConflictException} is thrown and no repository interaction occurs.
     */
    @Test
    @DisplayName("Should throw ConflictException when creating faculty with non-unique name")
    void shouldThrowConflictException_whenCreatingFacultyWithNonUniqueName() {
        // Arrange: Prepare mocks and inputs
        doThrow(new ConflictException("Faculty with name '" + faculty.getName() + "' already exists",
                "FACULTY_ALREADY_EXISTS")
        ).when(validationService).assertFacultyNameUnique(faculty.getName());
        
        // Act: Call the service method to check for exception
        assertThatThrownBy(() -> facultyService.createFaculty(facultyDTO))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Faculty with name '" + faculty.getName() + "' already exists");
        
        // Assert: Verify interactions
        verify(validationService).assertFacultyNameUnique(facultyDTO.getName());
        verifyNoInteractions(facultyRepository);
        verifyNoMoreInteractions(validationService);
    }
    
    // Update ============================================================
    
    /**
     * Unit test for {@link FacultyService#updateFaculty(Long, FacultyDTO)}.
     * <p>
     * <b>Scenario:</b> When updating a faculty with an ID that does not exist,
     * the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException}</li>
     *   <li>Not interact with the repository or validation service beyond the check</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> {@code NotFoundException} is thrown.
     */
    @Test
    @DisplayName("Should throw NotFoundException when updating faculty with non-existing ID")
    void shouldThrowNotFoundException_whenUpdatingNonExistingFaculty() {
        // Arrange, Act & Assert: Use helper to assert not found behavior
        TestAssertionUtils.assertFacultyNotFound(() -> facultyService.updateFaculty(TestDataFactory.NON_EXISTENT_ID, facultyDTO));
    }
    
    /**
     * Unit test for {@link FacultyService#updateFaculty(Long, FacultyDTO)}.
     * <p>
     * <b>Scenario:</b> When updating a faculty with a new name that is not unique,
     * the service should:
     * <ul>
     *   <li>Validate that the faculty exists via {@link ValidationService#getFacultyByIdOrThrow(Long)}</li>
     *   <li>Check that the new name is unique via {@link ValidationService#assertFacultyNameUnique(String)}</li>
     *   <li>Throw a {@link ConflictException} if the name is already taken</li>
     *   <li>Not call {@link FacultyRepository} to persist the update</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception type is thrown</li>
     *   <li>The validation service is called with the expected name</li>
     *   <li>The repository is not interacted with</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> {@code ConflictException} is thrown and no repository interaction occurs.
     */
    @Test
    @DisplayName("Should throw ConflictException when updating faculty with non-unique name")
    void shouldThrowConflictException_whenUpdatingFacultyWithNewNonUniqueName() {
        // Arrange: Prepare mocks and inputs
        FacultyDTO updatedDTO = new FacultyDTO(1L, "New Engineering");
        when(validationService.getFacultyByIdOrThrow(faculty.getId())).thenReturn(faculty);
        doThrow(new ConflictException("Faculty with name '" + updatedDTO.getName() + "' already exists",
                "FACULTY_ALREADY_EXISTS")
        ).when(validationService).assertFacultyNameUnique(updatedDTO.getName());
        
        // Act & Assert: Execute the service call and expect an exception
        assertThatThrownBy(() -> facultyService.updateFaculty(faculty.getId(), updatedDTO))
                .as("Service should throw ConflictException when Faculty name is not unique")
                .isInstanceOf(ConflictException.class).
                hasMessageContaining("Faculty with name '" + updatedDTO.getName() + "' already exists");
        
        // Assert: Verify interactions
        verify(validationService).getFacultyByIdOrThrow(faculty.getId());
        verify(validationService).assertFacultyNameUnique(updatedDTO.getName());
        verifyNoInteractions(facultyRepository);
        verifyNoMoreInteractions(validationService);
    }
    
    // Delete ============================================================
    
    /**
     * Unit test for {@link FacultyService#deleteFaculty(Long)}.
     * <p>
     * <b>Scenario:</b> When attempting to delete a faculty with an ID that does not exist,
     * the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException}</li>
     *   <li>Not interact with the repository or validation service beyond the check</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> {@code NotFoundException} is thrown.
     */
    @Test
    @DisplayName("Should throw NotFoundException when deleting faculty with non-existing ID")
    void shouldThrowNotFoundException_whenDeletingNonExistingFaculty() {
        // Arrange, Act & Assert: Use helper to assert not found behavior
        TestAssertionUtils.assertFacultyNotFound(() -> facultyService.deleteFaculty(TestDataFactory.NON_EXISTENT_ID));
    }
    
    /**
     * Unit test for {@link FacultyService#deleteFaculty(Long)}.
     * <p>
     * <b>Scenario:</b> When deleting a level that has associations with departments, the service should:
     * <ul>
     *   <li>Fetch the level entity via {@link ValidationService#getFacultyByIdOrThrow(Long)}</li>
     *   <li>Check for associations (departments) and throw a {@link ConflictException} if any exist</li>
     *   <li>Never call {@link FacultyRepository#delete(Object)} since deletion is not allowed</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception type is thrown</li>
     *   <li>The validation service is called with the expected ID</li>
     *   <li>The repository delete method is not called</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> {@code ConflictException} is thrown and no repository interaction occurs.
     */
    @Test
    @DisplayName("Should throw ConflictException when deleting faculty with associated departments")
    void shouldThrowConflictException_whenDeletingFacultyWithDepartments() {
        // Arrange: Prepare mocks and inputs
        when(validationService.getFacultyByIdOrThrow(faculty.getId())).thenReturn(faculty);
        Department department = TestDataFactory.buildDepartment(faculty);
        faculty.setDepartments(List.of(department));
        
        // Act: Call the service method to check for exception
        assertThatThrownBy(() -> facultyService.deleteFaculty(faculty.getId()))
                .as("Service should throw ConflictException when Faculty has associations with Departments")
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Cannot delete faculty with id " + faculty.getId() +
                        " because it has associated departments");
        
        // Assert: Verify interactions
        verify(validationService).getFacultyByIdOrThrow(faculty.getId());
        verifyNoInteractions(facultyRepository);
        verifyNoMoreInteractions(validationService);
    }
}
