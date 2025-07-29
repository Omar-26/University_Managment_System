package com.egabi.university.service.academic;

import com.egabi.university.dto.LevelDTO;
import com.egabi.university.entity.Course;
import com.egabi.university.entity.Faculty;
import com.egabi.university.entity.Level;
import com.egabi.university.entity.Student;
import com.egabi.university.exception.BadRequestException;
import com.egabi.university.exception.ConflictException;
import com.egabi.university.exception.NotFoundException;
import com.egabi.university.mapper.LevelMapper;
import com.egabi.university.repository.LevelRepository;
import com.egabi.university.service.academic.impl.LevelServiceImpl;
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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

/**
 * Comprehensive unit tests for {@link LevelServiceImpl} using JUnit 5 and Mockito.
 * <p>
 * These tests verify the service logic in isolation from external dependencies:
 * <ul>
 *   <li>{@link LevelRepository} for database operations</li>
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
public class LevelServiceTest {
    
    // ================================================================
    // Mocks and Dependencies
    // ================================================================
    
    @Mock
    private LevelRepository levelRepository;
    
    @Mock
    private ValidationService validationService;
    
    private LevelServiceImpl levelService;
    
    private Level level;
    private LevelDTO levelDTO;
    private Faculty faculty;
    
    // ================================================================
    // Setup : Arrange common test fixtures
    // ================================================================
    
    @BeforeEach
    void setUp() {
        // Initialize the mapper using MapStruct factory
        LevelMapper levelMapper = Mappers.getMapper(LevelMapper.class);
        
        // Set the validation service in the TestAssertionUtils
        TestAssertionUtils.setValidationService(validationService);
        
        // Create the service under test with mocked dependencies
        levelService = new LevelServiceImpl(levelRepository, levelMapper, validationService);
        
        // Prepare a Faculty entity with [id = 1 and name = "Engineering"] to be used in tests
        faculty = TestDataFactory.buildFaculty();
        
        // Prepare a Department entity with [id = 1, name = "Freshman", faculty]
        level = TestDataFactory.buildLevel(faculty);
        // Prepare a simple DTO  from the entity for input/output tests
        levelDTO = TestDataFactory.buildLevelDTO();
    }
    
    // ================================================================
    // Positive Test Cases
    // ================================================================
    
    // Get ============================================================
    
    /**
     * Unit test for {@link LevelService#getAllLevels()}.
     * <p>
     * <b>Scenario:</b> When levels exist, the service should:
     * <ul>
     *   <li>Fetch all levels from the repository</li>
     *   <li>Map them to a list of {@link LevelDTO}</li>
     *   <li>Return the list of DTOs</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The returned list contains the expected level data</li>
     *   <li>No unexpected interactions occur with the repository</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> The returned list contains exactly one level with correct data.
     */
    @Test
    @DisplayName("Should return all levels when levels exist")
    void shouldReturnAllLevels_whenLevelsExist() {
        // Arrange: prepare mocks and inputs
        when(levelRepository.findAll()).thenReturn(List.of(level));
        
        // Act: Call the method under test
        List<LevelDTO> result = levelService.getAllLevels();
        
        // Assert: Verify the output and interactions
        assertThat(result).as("Returned list should contain exactly one level").hasSize(1);
        assertThat(result.getFirst())
                .as("Returned level should match the mock entity")
                .extracting(LevelDTO::getId, LevelDTO::getName)
                .containsExactly(level.getId(), level.getName());
        
        verify(levelRepository).findAll();
        verifyNoMoreInteractions(levelRepository);
    }
    
    /**
     * Unit test for {@link LevelService#getLevelById(Long)}.
     * <p>
     * <b>Scenario:</b> When a level with the given ID exists, the service should:
     * <ul>
     *   <li>Fetch the department entity via {@link ValidationService#getLevelByIdOrThrow(Long)}</li>
     *   <li>Map the entity to a {@link LevelDTO} instance</li>
     *   <li>Return the mapped DTO</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The returned DTO matches the expected values</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> The returned DTO contains the correct department data.
     */
    @Test
    @DisplayName("Should return level by ID when it exists")
    void shouldReturnLevelById_whenLevelExists() {
        // Arrange: prepare mocks and inputs
        when(validationService.getLevelByIdOrThrow(level.getId())).thenReturn(level);
        
        // Act: Call the method under test
        LevelDTO result = levelService.getLevelById(level.getId());
        
        // Assert: Verify the output and interactions
        assertThat(result)
                .as("Returned level should match the mock entity")
                .extracting(LevelDTO::getId, LevelDTO::getName)
                .containsExactly(level.getId(), level.getName());
        
        verify(validationService).getLevelByIdOrThrow(level.getId());
        verifyNoMoreInteractions(levelRepository);
    }
    
    // Create =========================================================
    
    /**
     * Unit test for {@link LevelService#createLevel(LevelDTO)}.
     * <p>
     * <b>Scenario:</b> When creating a new level with existing faculty and a unique name, the service should:
     * <ul>
     *   <li>Validate the faculty is set in the DTO</li>
     *   <li>Validate the faculty exists via {@link ValidationService#getFacultyByIdOrThrow(Long)}</li>
     *   <li>Check if the level name is unique per faculty via {@link ValidationService#assertLevelNameUniquePerFaculty(String, Long)}</li>
     *   <li>Map the DTO to a new {@link Level} entity</li>
     *   <li>Save the entity via {@link LevelRepository#save(Object)}</li>
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
    @DisplayName("Should create a new level that has existing faculty and unique name per faculty")
    void shouldCreateLevel_whenExistingFacultyIsSetAndNameIsUniquePerFaculty() {
        // Arrange: prepare mocks and inputs
        when(validationService.getFacultyByIdOrThrow(faculty.getId())).thenReturn(faculty);
        doNothing().when(validationService).assertLevelNameUniquePerFaculty(levelDTO.getName(), faculty.getId());
        when(levelRepository.save(any(Level.class))).thenReturn(level);
        
        // Act: Call the method under test
        LevelDTO result = levelService.createLevel(levelDTO);
        
        // Assert: Verify the output and interactions
        assertThat(result)
                .as("Returned level should match the mock entity")
                .extracting(LevelDTO::getId, LevelDTO::getName)
                .containsExactly(level.getId(), level.getName());
        
        verify(validationService).assertLevelNameUniquePerFaculty(levelDTO.getName(), faculty.getId());
        verify(validationService).getFacultyByIdOrThrow(faculty.getId());
        verify(levelRepository).save(any(Level.class));
        verifyNoMoreInteractions(levelRepository, validationService);
    }
    
    // Update =========================================================
    
    /**
     * Unit test for {@link LevelService#updateLevel(Long, LevelDTO)}.
     * <p>
     * <b>Scenario:</b> When updating an existing level with the same name, the service should:
     * <ul>
     *   <li>Fetch the existing level entity via {@link ValidationService#getLevelByIdOrThrow(Long)}</li>
     *   <li>Return the existing entity as a DTO without changes</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The returned DTO matches the original entity</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> The returned DTO contains the unchanged level data.
     */
    @Test
    @DisplayName("Should return unchanged level when updating with same name")
    void shouldReturnUnchangedLevel_whenExistsAndUpdatingWithSameName() {
        // Arrange: prepare mocks and inputs
        when(validationService.getLevelByIdOrThrow(level.getId())).thenReturn(level);
        
        // Act: Call the method under test
        LevelDTO result = levelService.updateLevel(level.getId(), levelDTO);
        
        // Assert: Verify the output and interactions
        assertThat(result)
                .as("Returned level should match the original entity")
                .extracting(LevelDTO::getId, LevelDTO::getName, LevelDTO::getFacultyId)
                .containsExactly(level.getId(), levelDTO.getName(), faculty.getId());
        
        verify(validationService).getLevelByIdOrThrow(level.getId());
        verify(validationService, never()).assertLevelNameUniquePerFaculty(anyString(), anyLong());
        verify(levelRepository, never()).save(any(Level.class));
        verifyNoMoreInteractions(validationService);
    }
    
    /**
     * Unit test for {@link LevelService#updateLevel(Long, LevelDTO)}.
     * <p>
     * <b>Scenario:</b> When updating an existing level with a unique name per faculty, the service should:
     * <ul>
     *   <li>Fetch the existing level entity via {@link ValidationService#getLevelByIdOrThrow(Long)}</li>
     *   <li>Validate the new name is unique per faculty via {@link ValidationService#assertLevelNameUniquePerFaculty(String, Long)}</li>
     *   <li>Save the updated entity via {@link LevelRepository#save(Object)}</li>
     *   <li>Return the updated entity as a DTO</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The returned DTO has the expected ID and new name</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> The DTO contains the updated level data.
     */
    @Test
    @DisplayName("Should update an existing level with unique name per faculty")
    void shouldUpdateLevel_whenIdExistsAndNameIsUniquePerFaculty() {
        // Arrange: prepare mocks and inputs
        LevelDTO updatedDTO = TestDataFactory.buildLevelDTO(level.getId(), "New Freshman", faculty.getId());
        
        when(validationService.getLevelByIdOrThrow(level.getId())).thenReturn(level);
        doNothing().when(validationService).assertLevelNameUniquePerFaculty(updatedDTO.getName(), faculty.getId());
        when(levelRepository.save(any(Level.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act: Call the method under test
        LevelDTO result = levelService.updateLevel(level.getId(), updatedDTO);
        
        // Assert: Verify the output and interactions
        assertThat(result)
                .as("Returned level should match the mock updated entity")
                .extracting(LevelDTO::getId, LevelDTO::getName)
                .containsExactly(level.getId(), updatedDTO.getName());
        
        verify(validationService).getLevelByIdOrThrow(level.getId());
        verify(validationService).assertLevelNameUniquePerFaculty(updatedDTO.getName(), faculty.getId());
        verify(levelRepository).save(any(Level.class));
        verifyNoMoreInteractions(levelRepository, validationService);
    }
    
    // Delete =========================================================
    
    /**
     * Unit test for {@link LevelService#deleteLevel(Long)}.
     * <p>
     * <b>Scenario:</b> When deleting an existing level that has no associations, the service should:
     * <ul>
     *   <li>Fetch the level entity via {@link ValidationService#getLevelByIdOrThrow(Long)}</li>
     *   <li>Check for associations (students or courses) and ensure there are none</li>
     *   <li>Delete the entity via {@link LevelRepository#delete(Object)}</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The level is deleted without any exceptions</li>
     *   <li>The repository delete method is called with the correct entity</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> The level is successfully deleted.
     */
    @Test
    @DisplayName("Should delete an existing level that has no associations")
    void shouldDeleteLevelById_whenLevelExistsAndHasNoAssociations() {
        // Arrange: prepare mocks and inputs
        when(validationService.getLevelByIdOrThrow(level.getId())).thenReturn(level);
        
        // Act: Call the method under test
        levelService.deleteLevel(level.getId());
        
        // Assert: Verify the interactions
        verify(validationService).getLevelByIdOrThrow(level.getId());
        verify(levelRepository).delete(level);
        verifyNoMoreInteractions(levelRepository, validationService);
    }
    
    // ================================================================
    // Negative Test Cases
    // ================================================================
    
    // Get ============================================================
    
    /**
     * Unit test for {@link LevelService#getLevelById(Long)}.
     * <p>
     * <b>Scenario:</b> When attempting to retrieve a faculty by an ID that does not exist,
     * the service should:
     * <ul>
     *   <li>Call {@link ValidationService#getLevelByIdOrThrow(Long)} to check existence</li>
     *   <li>Throw a {@link NotFoundException} if the level is not found</li>
     *   <li>Never call {@link LevelRepository} since the level does not exist</li>
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
    @DisplayName("Should throw NotFoundException when level ID does not exist")
    void shouldThrowNotFoundException_whenLevelIdDoesNotExist() {
        // Arrange, Act & Assert: Use helper to assert not found behavior
        TestAssertionUtils.assertLevelNotFound(() -> levelService.getLevelById(TestDataFactory.NON_EXISTENT_ID));
    }
    
    // Create =========================================================
    
    /**
     * Unit test for {@link LevelService#createLevel(LevelDTO)}.
     * <p>
     * <b>Scenario:</b> When creating a level with no faculty set, the service should:
     * <ul>
     *   <li>Throw a {@link BadRequestException}</li>
     *   <li>Never call {@link LevelRepository} since the level cannot be created</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception type is thrown</li>
     *   <li>No interactions with the repository occur</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> {@code BadRequestException} is thrown indicating the faculty must be set,
     */
    @Test
    @DisplayName("Should throw BadRequestException when creating level without setting faculty")
    void shouldThrowBadRequestException_whenCreatingLevelWithoutSettingFaculty() {
        // Arrange: prepare inputs
        levelDTO.setFacultyId(null);
        
        // Act & Assert: Execute the service call and expect an exception
        assertThatThrownBy(() -> levelService.createLevel(levelDTO))
                .as("Service should throw Bad Request Exception when Faculty ID is not set")
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Level must be in a faculty");
        
        // Assert: Verify interactions
        verifyNoInteractions(levelRepository);
    }
    
    /**
     * Unit test for {@link LevelService#createLevel(LevelDTO)}.
     * <p>
     * <b>Scenario:</b> When creating a level with a non-existent faculty, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException}</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception type is thrown</li>
     *   <li>No interactions with the repository occur</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> A {@code NotFoundException} is thrown indicating the faculty was not found.
     */
    @Test
    @DisplayName("Should throw NotFoundException when creating level with non-existent faculty")
    void shouldThrowNotFoundException_whenCreatingLevelWithNonExistentFaculty() {
        // Arrange: prepare mocks and inputs
        levelDTO.setFacultyId(TestDataFactory.NON_EXISTENT_ID);
        doThrow(new NotFoundException("Faculty with id " + TestDataFactory.NON_EXISTENT_ID + " not found"
                , "FACULTY_NOT_FOUND")
        ).when(validationService).getFacultyByIdOrThrow(TestDataFactory.NON_EXISTENT_ID);
        
        // Act & Assert: Execute the service call and expect an exception
        assertThatThrownBy(() -> levelService.createLevel(levelDTO))
                .as("Service should throw Not Found Exception when Faculty ID does not exist")
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Faculty with id " + TestDataFactory.NON_EXISTENT_ID + " not found");
        
        // Assert: Verify interactions
        verify(validationService).getFacultyByIdOrThrow(TestDataFactory.NON_EXISTENT_ID);
        verifyNoInteractions(levelRepository);
    }
    
    /**
     * Unit test for {@link LevelService#createLevel(LevelDTO)}.
     * <p>
     * <b>Scenario:</b> When attempting to create a level with a name that already exists,
     * the service should:
     * <ul>
     *   <li>Call {@link ValidationService#assertLevelNameUniquePerFaculty(String, Long)} to check uniqueness</li>
     *   <li>Throw a {@link ConflictException} if the name is already taken</li>
     *   <li>Never call {@link LevelRepository} to persist the duplicate level</li>
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
    @DisplayName("Should throw ConflictException when creating level with non-unique name per faculty")
    void shouldThrowConflictException_whenCreatingLevelWithNonUniqueName() {
        // Arrange: prepare mocks and inputs
        doThrow(new ConflictException("Level with name '" + levelDTO.getName() + "' already exists",
                "LEVEL_ALREADY_EXISTS")
        ).when(validationService).assertLevelNameUniquePerFaculty(levelDTO.getName(), faculty.getId());
        
        // Act & Assert: Execute the service call and expect an exception
        assertThatThrownBy(() -> levelService.createLevel(levelDTO))
                .as("Service should throw ConflictException when Level name is not unique")
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Level with name '" + levelDTO.getName() + "' already exists");
        
        // Assert: Verify interactions
        verify(validationService).assertLevelNameUniquePerFaculty(levelDTO.getName(), faculty.getId());
        verifyNoInteractions(levelRepository);
        verifyNoMoreInteractions(validationService);
    }
    
    // Update =========================================================
    
    /**
     * Unit test for {@link LevelService#updateLevel(Long, LevelDTO)}.
     * <p>
     * <b>Scenario:</b> When updating a level with a non-existent ID, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException}</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> A {@code NotFoundException} is thrown indicating the level was not found.
     */
    @Test
    @DisplayName("Should throw NotFoundException when updating level with non-existent ID")
    void shouldThrowNotFoundException_whenUpdatingNonExistingLevel() {
        // Arrange, Act & Assert: Use helper to assert not found behavior
        TestAssertionUtils.assertLevelNotFound(() -> levelService.updateLevel(TestDataFactory.NON_EXISTENT_ID, levelDTO));
    }
    
    /**
     * Unit test for {@link LevelService#updateLevel(Long, LevelDTO)}.
     * <p>
     * <b>Scenario:</b> When updating a level with a non-unique name, the service should:
     * <ul>
     *   <li>Throw a {@link ConflictException}</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> A {@code ConflictException} is thrown indicating the level name is not unique.
     */
    @Test
    @DisplayName("Should throw ConflictException when updating level with non-unique name")
    void shouldThrowConflictException_whenUpdatingLevelWithNonUniqueName() {
        // Arrange: prepare mocks and inputs
        LevelDTO updatedDTO = TestDataFactory.buildLevelDTO(level.getId(), "New Computer Engineering", faculty.getId());
        when(validationService.getLevelByIdOrThrow(level.getId())).thenReturn(level);
        doThrow(new ConflictException("Level with name '" + updatedDTO.getName() + "' already exists", "LEVEL_ALREADY_EXISTS"))
                .when(validationService).assertLevelNameUniquePerFaculty(updatedDTO.getName(), faculty.getId());
        
        // Act & Assert: Execute the service call and expect an exception
        assertThatThrownBy(() -> levelService.updateLevel(level.getId(), updatedDTO))
                .as("Service should throw ConflictException when Level name is not unique")
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Level with name '" + updatedDTO.getName() + "' already exists");
        
        // Assert: Verify interactions
        verify(validationService).getLevelByIdOrThrow(level.getId());
        verify(validationService).assertLevelNameUniquePerFaculty(updatedDTO.getName(), faculty.getId());
        verifyNoInteractions(levelRepository);
        verifyNoMoreInteractions(validationService);
    }
    
    // Delete =========================================================
    
    /**
     * Unit test for {@link LevelService#deleteLevel(Long)}.
     * <p>
     * <b>Scenario:</b> When deleting a level with a non-existent ID, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException}</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception type is thrown</li>
     *   <li>No interactions with the repository occur</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> A {@code NotFoundException} is thrown indicating the level was not found.
     */
    @Test
    @DisplayName("Should throw NotFoundException when deleting level with non-existent ID")
    void shouldThrowNotFoundException_whenDeletingNonExistingLevel() {
        // Arrange, Act & Assert: Use helper to assert not found behavior
        TestAssertionUtils.assertLevelNotFound(() -> levelService.deleteLevel(TestDataFactory.NON_EXISTENT_ID));
    }
    
    
    /**
     * Unit test for {@link LevelService#deleteLevel(Long)}.
     * <p>
     * <b>Scenario:</b> When deleting a level that has associations with students or courses, the service should:
     * <ul>
     *   <li>Fetch the level entity via {@link ValidationService#getLevelByIdOrThrow(Long)}</li>
     *   <li>Check for associations (students or courses) and throw a {@link ConflictException} if any exist</li>
     *   <li>Never call {@link LevelRepository#delete(Object)} since deletion is not allowed</li>
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
    @DisplayName("Should throw ConflictException when deleting level with associations")
    void shouldThrowConflictException_whenDeletingLevelWithAssociations() {
        // Arrange: prepare mocks and inputs
        when(validationService.getLevelByIdOrThrow(level.getId())).thenReturn(level);
        // TODO change this to use a real Test Data Factory
        Student student = new Student();
        Course course = new Course();
        level.setStudents(List.of(student));
        level.setCourses(List.of(course));
        
        // Act & Assert: Execute the service call and expect an exception
        assertThatThrownBy(() -> levelService.deleteLevel(level.getId()))
                .as("Service should throw ConflictException when Level has associations with students or courses")
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Cannot delete level with id " + level.getId() + " because it has associated students or courses");
        
        // Assert: Verify interactions
        verify(validationService).getLevelByIdOrThrow(level.getId());
        verifyNoInteractions(levelRepository);
        verifyNoMoreInteractions(validationService);
    }
    
}
