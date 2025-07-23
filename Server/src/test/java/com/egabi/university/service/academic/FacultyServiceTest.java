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
import com.egabi.university.service.validation.ValidationServiceImpl;
import com.egabi.university.util.TestDataFactory;
import org.jetbrains.annotations.NotNull;
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
 * Unit tests for {@link FacultyServiceImpl}.
 * <p>
 * This test class verifies the correct behavior of the service by isolating it from
 * external dependencies:
 * <ul>
 *   <li>{@link FacultyRepository} for database operations</li>
 *   <li>{@link ValidationService} for business validation logic</li>
 * </ul>
 * Focus areas:
 * <ul>
 *   <li>Correct repository interactions</li>
 *   <li>Correct validation logic calls</li>
 *   <li>Proper handling of expected scenarios</li>
 * </ul>
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
class FacultyServiceTest {
    
    // ================================================================
    // Mocks and Dependencies
    // ================================================================
    
    @Mock
    private FacultyRepository facultyRepository;
    
    @Mock
    private ValidationServiceImpl validationService;
    
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
        
        // Create the service under test with mocked dependencies
        facultyService = new FacultyServiceImpl(facultyRepository, facultyMapper, validationService);
        
        // Prepare an entity with [id = 1 and name = "Engineering"]
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
     * Scenario: When faculties exist in the database, the service should:
     * <ul>
     *   <li>Fetch all faculty entities via {@link FacultyRepository#findAll()}</li>
     *   <li>Map the entities to {@link FacultyDTO} instances correctly</li>
     *   <li>Return a list containing all mapped DTOs</li>
     * </ul>
     * <p>
     * Verifies:
     * <ul>
     *   <li>The returned list has the expected size and content</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * <p>
     * Expected result: The list contains all faculties mapped to DTOs.
     */
    @Test
    @DisplayName("Should return all faculties")
    void shouldGetAllFaculties_whenTheyExist() {
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
        verifyNoMoreInteractions(validationService);
    }
    
    /**
     * Unit test for {@link FacultyService#getFacultyById(Long)}.
     * <p>
     * Scenario: When a faculty with the specified ID exists, the service should:
     * <ul>
     *   <li>Fetch the faculty entity via {@link ValidationService#getFacultyByIdOrThrow(Long)}</li>
     *   <li>Map the entity to a {@link FacultyDTO} instance</li>
     *   <li>Return the mapped DTO</li>
     * </ul>
     * <p>
     * Verifies:
     * <ul>
     *   <li>The returned DTO has the expected ID and name</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * <p>
     * Expected result: The DTO contains the correct faculty data.
     */
    @Test
    @DisplayName("Should return faculty by ID when it exists")
    void shouldGetFaculty_whenIdExists() {
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
     * Scenario: When creating a new faculty with a unique name, the service should:
     * <ul>
     *   <li>Validate that the faculty name is unique via {@link ValidationService#assertFacultyNameUnique(String)}</li>
     *   <li>Map the DTO to a {@link Faculty} entity</li>
     *   <li>Save the entity via {@link FacultyRepository#save(Object)}</li>
     *   <li>Return the saved entity as a DTO</li>
     * </ul>
     * <p>
     * Verifies:
     * <ul>
     *   <li>The returned DTO has the expected ID and name</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * <p>
     * Expected result: The DTO contains the correct faculty data.
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
     * Scenario: When updating an existing faculty with the same name, the service should:
     * <ul>
     *   <li>Validate that the faculty exists via {@link ValidationService#getFacultyByIdOrThrow(Long)}</li>
     *   <li>Save the entity without changing the name</li>
     *   <li>Return the updated entity as a DTO</li>
     * </ul>
     * <p>
     * Verifies:
     * <ul>
     *   <li>The returned DTO has the same ID and name as before</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * <p>
     * Expected result: The DTO contains the same faculty data.
     */
    @Test
    @DisplayName("Should update faculty with same name without throwing conflict")
    void shouldUpdateFaculty_whenUpdatingWithSameName() {
        // Arrange: Prepare mocks and inputs
        FacultyDTO sameNameDTO = new FacultyDTO(faculty.getId(), faculty.getName());
        
        when(validationService.getFacultyByIdOrThrow(faculty.getId())).thenReturn(faculty);
        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        
        // Act: Call the method under test
        FacultyDTO result = facultyService.updateFaculty(faculty.getId(), sameNameDTO);
        
        // Assert: Verify output and interactions
        assertThat(result.getName()).isEqualTo(faculty.getName());
        
        verify(validationService).getFacultyByIdOrThrow(faculty.getId());
        verifyNoMoreInteractions(validationService);
    }
    
    /**
     * Unit test for {@link FacultyService#updateFaculty(Long, FacultyDTO)}.
     * <p>
     * Scenario: When updating an existing faculty with a new unique name, the service should:
     * <ul>
     *   <li>Validate that the faculty exists via {@link ValidationService#getFacultyByIdOrThrow(Long)}</li>
     *   <li>Validate that the new name is unique via {@link ValidationService#assertFacultyNameUnique(String)}</li>
     *   <li>Map the DTO to a {@link Faculty} entity</li>
     *   <li>Save the updated entity via {@link FacultyRepository#save(Object)}</li>
     *   <li>Return the updated entity as a DTO</li>
     * </ul>
     * <p>
     * Verifies:
     * <ul>
     *   <li>The returned DTO has the expected ID and new name</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * <p>
     * Expected result: The DTO contains the updated faculty data.
     */
    @Test
    @DisplayName("Should update faculty with new unique name")
    void shouldUpdateFacultyWithNewUniqueName_whenIdExists() {
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
     * Scenario: When deleting a faculty by ID, the service should:
     * <ul>
     *   <li>Validate that the faculty exists via {@link ValidationService#getFacultyByIdOrThrow(Long)}</li>
     *   <li>Delete the faculty entity via {@link FacultyRepository#delete(Object)}</li>
     * </ul>
     * <p>
     * Verifies:
     * <ul>
     *   <li>No exceptions are thrown</li>
     *   <li>The repository delete method is called with the correct entity</li>
     *   <li>No unexpected interactions occur with the validation service</li>
     * </ul>
     * <p>
     * Expected result: The faculty is deleted successfully.
     */
    @Test
    @DisplayName("Should delete faculty by ID")
    void shouldDeleteFacultyWithoutDepartments_whenIdExists() {
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
     * Scenario: When requesting a faculty by an ID that does not exist,
     * the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException}</li>
     *   <li>Not interact with unrelated services or repositories beyond the check</li>
     * </ul>
     * <p>
     * Expected result: {@code NotFoundException} is thrown indicating the faculty was not found.
     */
    @Test
    @DisplayName("Should throw Not Found Exception when faculty ID does not exist")
    void shouldThrowNotFoundException_whenGettingNonExistingFaculty() {
        // Arrange, Act & Assert: Use helper to assert not found behavior
        assertFacultyNotFound(() -> facultyService.getFacultyById(TestDataFactory.NON_EXISTENT_ID));
    }
    
    // Create ============================================================
    
    /**
     * Unit test for {@link FacultyService#createFaculty(FacultyDTO)}.
     * <p>
     * Scenario: When attempting to create a faculty with a name that already exists,
     * the service should:
     * <ul>
     *   <li>Call {@link ValidationService#assertFacultyNameUnique(String)} to check uniqueness</li>
     *   <li>Throw a {@link ConflictException} if the name is already taken</li>
     *   <li>Never call {@link FacultyRepository} to persist the duplicate faculty</li>
     * </ul>
     * <p>
     * Verifies:
     * <ul>
     *   <li>The correct exception type is thrown</li>
     *   <li>The validation service is called with the expected name</li>
     *   <li>The repository is not interacted with</li>
     * </ul>
     * <p>
     * Expected result: {@code ConflictException} is thrown and no repository interaction occurs.
     */
    @Test
    @DisplayName("Should throw Conflict Exception if faculty name already exists")
    void shouldThrowConflictException_whenNameAlreadyExists() {
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
     * Scenario: When updating a faculty with an ID that does not exist,
     * the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException}</li>
     *   <li>Not interact with the repository or validation service beyond the check</li>
     * </ul>
     * <p>
     * Expected result: {@code NotFoundException} is thrown.
     */
    @Test
    @DisplayName("Should throw Not Found Exception when updating faculty with non-existing ID")
    void shouldThrowNotFoundException_whenUpdatingNonExistingFaculty() {
        // Arrange, Act & Assert: Use helper to assert not found behavior
        assertFacultyNotFound(() -> facultyService.updateFaculty(TestDataFactory.NON_EXISTENT_ID, facultyDTO));
    }
    
    /**
     * Unit test for {@link FacultyService#updateFaculty(Long, FacultyDTO)}.
     * <p>
     * Scenario: When updating a faculty with a new name that is not unique,
     * the service should:
     * <ul>
     *   <li>Validate that the faculty exists via {@link ValidationService#getFacultyByIdOrThrow(Long)}</li>
     *   <li>Check that the new name is unique via {@link ValidationService#assertFacultyNameUnique(String)}</li>
     *   <li>Throw a {@link ConflictException} if the name is already taken</li>
     *   <li>Not call {@link FacultyRepository} to persist the update</li>
     * </ul>
     * <p>
     * Verifies:
     * <ul>
     *   <li>The correct exception type is thrown</li>
     *   <li>The validation service is called with the expected name</li>
     *   <li>The repository is not interacted with</li>
     * </ul>
     * <p>
     * Expected result: {@code ConflictException} is thrown and no repository interaction occurs.
     */
    @Test
    @DisplayName("Should throw Conflict Exception when updating faculty with non-unique name")
    void shouldThrowConflictException_whenUpdatingFacultyWithNewNonUniqueName() {
        // Arrange: Prepare mocks and inputs
        FacultyDTO updatedDTO = new FacultyDTO(1L, "New Engineering");
        when(validationService.getFacultyByIdOrThrow(faculty.getId())).thenReturn(faculty);
        doThrow(new ConflictException("Faculty with name '" + updatedDTO.getName() + "' already exists",
                "FACULTY_ALREADY_EXISTS")
        ).when(validationService).assertFacultyNameUnique(updatedDTO.getName());
        
        // Act: Call the service method to check for exception
        assertThatThrownBy(() -> facultyService.updateFaculty(faculty.getId(), updatedDTO))
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
     * Scenario: When attempting to delete a faculty with an ID that does not exist,
     * the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException}</li>
     *   <li>Not interact with the repository or validation service beyond the check</li>
     * </ul>
     * <p>
     * Expected result: {@code NotFoundException} is thrown.
     */
    @Test
    @DisplayName("Should throw Not Found Exception when deleting faculty with non-existing ID")
    void shouldThrowNotFoundException_whenDeletingNonExistingFaculty() {
        // Arrange, Act & Assert: Use helper to assert not found behavior
        assertFacultyNotFound(() -> facultyService.deleteFaculty(TestDataFactory.NON_EXISTENT_ID));
    }
    
    /**
     * Unit test for {@link FacultyService#deleteFaculty(Long)}.
     * <p>
     * Scenario: When attempting to delete a faculty that has associated departments,
     * the service should:
     * <ul>
     *   <li>Validate that the faculty exists via {@link ValidationService#getFacultyByIdOrThrow(Long)}</li>
     *   <li>Check if the faculty has any associated departments</li>
     *   <li>Throw a {@link ConflictException} if departments are found</li>
     *   <li>Not call {@link FacultyRepository} to delete the faculty</li>
     * </ul>
     * <p>
     * Verifies:
     * <ul>
     *   <li>The correct exception type is thrown</li>
     *   <li>The validation service is called with the expected ID</li>
     *   <li>The repository is not interacted with</li>
     * </ul>
     * <p>
     * Expected result: {@code ConflictException} is thrown and no repository interaction occurs.
     */
    @Test
    @DisplayName("Should throw Conflict Exception when deleting faculty with associated departments")
    void shouldThrowConflictException_whenDeletingFacultyWithDepartments() {
        // Arrange: Prepare mocks and inputs
        Department department = new Department();
        faculty.setDepartments(List.of(department));
        
        when(validationService.getFacultyByIdOrThrow(faculty.getId())).thenReturn(faculty);
        
        // Act: Call the service method to check for exception
        assertThatThrownBy(() -> facultyService.deleteFaculty(faculty.getId()))
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Cannot delete faculty with id " + faculty.getId() +
                        " because it has associated departments");
        
        // Assert: Verify interactions
        verify(validationService).getFacultyByIdOrThrow(faculty.getId());
        verifyNoInteractions(facultyRepository);
        verifyNoMoreInteractions(validationService);
    }
    
    // ================================================================
    // Utility Methods
    // ================================================================
    
    /**
     * Utility to verify that a service call throws {@link NotFoundException} for a missing faculty.
     *
     * @param executable The service call expected to throw.
     */
    private void assertFacultyNotFound(@NotNull Runnable executable) {
        // Arrange: Stub validation service to throw NotFoundException
        doThrow(new NotFoundException(
                "Faculty with id " + TestDataFactory.NON_EXISTENT_ID + " not found"
                , "FACULTY_NOT_FOUND")
        ).when(validationService).getFacultyByIdOrThrow(TestDataFactory.NON_EXISTENT_ID);
        
        // Act : Execute the service call and expect an exception
        assertThatThrownBy(executable::run)
                .as("Service should throw Not Found Exception when faculty ID does not exist")
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Faculty with id " + TestDataFactory.NON_EXISTENT_ID + " not found");
        
        // Assert: Verify interactions
        verify(validationService).getFacultyByIdOrThrow(TestDataFactory.NON_EXISTENT_ID);
        verifyNoInteractions(facultyRepository);
        verifyNoMoreInteractions(validationService);
    }
}