package com.egabi.university.service.academic;

import com.egabi.university.dto.InstructorDTO;
import com.egabi.university.entity.Course;
import com.egabi.university.entity.Department;
import com.egabi.university.entity.Faculty;
import com.egabi.university.entity.Instructor;
import com.egabi.university.entity.authentication.Role;
import com.egabi.university.entity.authentication.User;
import com.egabi.university.exception.BadRequestException;
import com.egabi.university.exception.NotFoundException;
import com.egabi.university.mapper.InstructorMapper;
import com.egabi.university.repository.InstructorRepository;
import com.egabi.university.service.academic.impl.InstructorServiceImpl;
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

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Comprehensive unit tests for {@link InstructorServiceImpl} using JUnit 5 and Mockito.
 * <p>
 * These tests verify the service logic in isolation from external dependencies:
 * <ul>
 *   <li>{@link InstructorRepository} for database operations</li>
 *   <li>{@link ValidationService} for business validation logic</li>
 * </ul>
 * </p>
 * <p>
 * <b>Focus areas:</b>
 * <ul>
 *   <li>Repository interaction correctness</li>
 *   <li>Validation logic invocation</li>
 *   <li>Proper exception handling and scenario coverage</li>
 * </ul>
 * </p>
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class InstructorServiceTest {
    
    // ================================================================
    // Mocks and Dependencies
    // ================================================================
    
    @Mock
    private InstructorRepository instructorRepository;
    
    @Mock
    private ValidationService validationService;
    
    private InstructorServiceImpl instructorService;
    
    private Instructor instructor;
    private InstructorDTO instructorDTO;
    private Department department;
    private Faculty faculty;
    private User user;
    
    // ================================================================
    // Setup : Arrange common test fixtures
    // ================================================================
    
    @BeforeEach
    void setUp() {
        // Initialize the mapper using MapStruct factory
        InstructorMapper instructorMapper = Mappers.getMapper(InstructorMapper.class);
        
        // Set up the validation service for TestAssertionUtils
        TestAssertionUtils.setValidationService(validationService);
        
        // Create the service under test with mocked dependencies
        instructorService = new InstructorServiceImpl(instructorRepository, instructorMapper, validationService);
        
        // Prepare test data
        faculty = TestDataFactory.buildFaculty();
        department = TestDataFactory.buildDepartment(faculty);
        user = TestDataFactory.buildUser(Role.INSTRUCTOR);
        instructor = TestDataFactory.buildInstructor(user, department);
        instructorDTO = TestDataFactory.buildInstructorDTO();
    }
    
    // ================================================================
    // Positive Test Cases
    // ================================================================
    
    // Get ============================================================
    
    /**
     * Unit test for {@link InstructorService#getAllInstructors()}.
     * <p>
     * <b>Scenario:</b> When instructors exist in the database, the service should:
     * <ul>
     *   <li>Fetch all instructor entities via {@link InstructorRepository#findAll()}</li>
     *   <li>Map the entities to {@link InstructorDTO} instances correctly</li>
     *   <li>Return a list containing all mapped DTOs</li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The returned list has the expected size and content</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> The list contains all instructors mapped to DTOs.
     */
    @Test
    @DisplayName("Should return all instructors when instructors exist")
    void shouldReturnAllInstructors_whenInstructorsExist() {
        // Arrange: Prepare mocks and inputs
        when(instructorRepository.findAll()).thenReturn(List.of(instructor));
        
        // Act: Call the method under test
        List<InstructorDTO> result = instructorService.getAllInstructors();
        
        // Assert: Verify output and interactions
        assertThat(result)
                .as("Returned list should contain exactly one instructor")
                .hasSize(1);
        assertThat(result.getFirst())
                .as("Returned instructor should match the mock entity")
                .extracting(InstructorDTO::getId, InstructorDTO::getUserId,
                        InstructorDTO::getFirstName, InstructorDTO::getLastName,
                        InstructorDTO::getPhoneNumber, InstructorDTO::getDateOfBirth, InstructorDTO::getGender,
                        InstructorDTO::getFacultyId, InstructorDTO::getFacultyName,
                        InstructorDTO::getDepartmentId, InstructorDTO::getDepartmentName)
                .containsExactly(instructor.getId(), user.getId(),
                        instructor.getFirstName(), instructor.getLastName(),
                        instructor.getPhoneNumber(), instructor.getDateOfBirth(), instructor.getGender(),
                        faculty.getId(), faculty.getName(),
                        department.getId(), department.getName());
        
        verify(instructorRepository).findAll();
        verifyNoMoreInteractions(validationService, instructorRepository);
    }
    
    /**
     * Unit test for {@link InstructorService#getInstructorById(Long)}.
     * <p>
     * <b>Scenario:</b> When an instructor with the specified ID exists, the service should:
     * <ul>
     *   <li>Fetch the instructor entity via {@link ValidationService#getInstructorByIdOrThrow(Long)}</li>
     *   <li>Map the entity to an {@link InstructorDTO} instance</li>
     *   <li>Return the mapped DTO</li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The returned DTO has the expected ID, firstName, and lastName</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> The DTO contains the correct instructor data.
     */
    @Test
    @DisplayName("Should return instructor by ID when it exists")
    void shouldReturnInstructorById_whenInstructorExists() {
        // Arrange: Prepare mocks and inputs
        when(validationService.getInstructorByIdOrThrow(instructor.getId())).thenReturn(instructor);
        
        // Act: Call the method under test
        InstructorDTO result = instructorService.getInstructorById(instructor.getId());
        
        // Assert: Verify output and interactions
        assertThat(result)
                .as("Returned instructor should match the mock entity")
                .extracting(InstructorDTO::getId, InstructorDTO::getUserId,
                        InstructorDTO::getFirstName, InstructorDTO::getLastName,
                        InstructorDTO::getPhoneNumber, InstructorDTO::getDateOfBirth, InstructorDTO::getGender,
                        InstructorDTO::getFacultyId, InstructorDTO::getFacultyName,
                        InstructorDTO::getDepartmentId, InstructorDTO::getDepartmentName)
                .containsExactly(instructor.getId(), user.getId(),
                        instructor.getFirstName(), instructor.getLastName(),
                        instructor.getPhoneNumber(), instructor.getDateOfBirth(), instructor.getGender(),
                        faculty.getId(), faculty.getName(),
                        department.getId(), department.getName());
        
        verify(validationService).getInstructorByIdOrThrow(instructor.getId());
        verifyNoMoreInteractions(validationService);
    }
    
    // Create ============================================================
    
    /**
     * Unit test for {@link InstructorService#createInstructor(InstructorDTO)}.
     * <p>
     * <b>Scenario:</b> When creating a new instructor, the service should:
     * <ul>
     *   <li>Map the DTO to an {@link Instructor} entity</li>
     *   <li>Validate the department and courses</li>
     *   <li>Save the entity via {@link InstructorRepository#save(Object)}</li>
     *   <li>Return the saved entity as a DTO</li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The returned DTO has the expected values</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> The DTO contains the correct instructor data.
     */
    @Test
    @DisplayName("Should create a new instructor when all required fields are set")
    void shouldCreateInstructor_whenAllRequiredFieldsExistAndAreSet() {
        // Arrange: Prepare mocks and inputs
        instructor.setUser(null);
        when(validationService.getDepartmentByIdOrThrow(department.getId())).thenReturn(department);
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);
        
        // Act: Call the method under test
        InstructorDTO result = instructorService.createInstructor(instructorDTO);
        
        // Assert: Verify output and interactions
        assertThat(result)
                .as("Returned instructor should match the mock entity")
                .extracting(InstructorDTO::getId, InstructorDTO::getFirstName, InstructorDTO::getLastName,
                        InstructorDTO::getPhoneNumber, InstructorDTO::getDateOfBirth, InstructorDTO::getGender,
                        InstructorDTO::getFacultyId, InstructorDTO::getFacultyName,
                        InstructorDTO::getDepartmentId, InstructorDTO::getDepartmentName)
                .containsExactly(instructor.getId(), instructor.getFirstName(), instructor.getLastName(),
                        instructor.getPhoneNumber(), instructor.getDateOfBirth(), instructor.getGender(),
                        faculty.getId(), faculty.getName(),
                        department.getId(), department.getName());
        
        verify(validationService).getDepartmentByIdOrThrow(department.getId());
        verify(instructorRepository).save(any(Instructor.class));
        verifyNoMoreInteractions(validationService, instructorRepository);
    }
    
    /**
     * Unit test for {@link InstructorService#createInstructor(InstructorDTO, User)}.
     * <p>
     * <b>Scenario:</b> When creating a new instructor with associated user, the service should:
     * <ul>
     *   <li>Map the DTO to an {@link Instructor} entity</li>
     *   <li>Associate the user with the instructor</li>
     *   <li>Validate the department and courses</li>
     *   <li>Save the entity via {@link InstructorRepository#save(Object)}</li>
     *   <li>Return the saved entity as a DTO</li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The returned DTO has the expected values</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> The DTO contains the correct instructor data with user association.
     */
    @Test
    @DisplayName("Should create a new instructor with associated user")
    void shouldCreateInstructorWithUser_whenAllRequiredFieldsExistAndAreSet() {
        // Arrange: Prepare mocks and inputs
        when(validationService.getDepartmentByIdOrThrow(department.getId())).thenReturn(department);
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);
        
        // Act: Call the method under test
        InstructorDTO result = instructorService.createInstructor(instructorDTO, user);
        
        // Assert: Verify output and interactions
        assertThat(result)
                .as("Returned instructor should match the mock entity")
                .extracting(InstructorDTO::getId, InstructorDTO::getUserId,
                        InstructorDTO::getFirstName, InstructorDTO::getLastName,
                        InstructorDTO::getPhoneNumber, InstructorDTO::getDateOfBirth, InstructorDTO::getGender,
                        InstructorDTO::getFacultyId, InstructorDTO::getFacultyName,
                        InstructorDTO::getDepartmentId, InstructorDTO::getDepartmentName)
                .containsExactly(instructor.getId(), user.getId(),
                        instructor.getFirstName(), instructor.getLastName(),
                        instructor.getPhoneNumber(), instructor.getDateOfBirth(), instructor.getGender(),
                        faculty.getId(), faculty.getName(),
                        department.getId(), department.getName());
        
        verify(validationService).getDepartmentByIdOrThrow(department.getId());
        verify(instructorRepository).save(any(Instructor.class));
        verifyNoMoreInteractions(instructorRepository);
    }
    
    // Update ============================================================
    
    // TODO add test case for updating an instructor with same data
    
    // TODO Check the update logic with the user
    
    /**
     * Unit test for {@link InstructorService#updateInstructor(Long, InstructorDTO)}.
     * <p>
     * <b>Scenario:</b> When updating an existing instructor, the service should:
     * <ul>
     *   <li>Fetch the existing instructor via {@link ValidationService#getInstructorByIdOrThrow(Long)}</li>
     *   <li>Update the entity with new data</li>
     *   <li>Validate the department and courses</li>
     *   <li>Save the updated entity via {@link InstructorRepository#save(Object)}</li>
     *   <li>Return the updated entity as a DTO</li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The returned DTO has the updated values</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> The DTO contains the updated instructor data.
     */
    @Test
    @DisplayName("Should update instructor when it exists")
    void shouldUpdateInstructor_whenInstructorExistsAndAllRequiredFieldsExistAndAreSet() {
        // Arrange: Prepare mocks and inputs
        when(validationService.getInstructorByIdOrThrow(instructor.getId())).thenReturn(instructor);
        when(validationService.getDepartmentByIdOrThrow(department.getId())).thenReturn(department);
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);
        
        // Act: Call the method under test
        InstructorDTO result = instructorService.updateInstructor(instructor.getId(), instructorDTO);
        
        // Assert: Verify output and interactions
        assertThat(result)
                .as("Returned instructor should match the mock entity")
                .extracting(InstructorDTO::getId, InstructorDTO::getUserId,
                        InstructorDTO::getFirstName, InstructorDTO::getLastName,
                        InstructorDTO::getPhoneNumber, InstructorDTO::getDateOfBirth, InstructorDTO::getGender,
                        InstructorDTO::getFacultyId, InstructorDTO::getFacultyName,
                        InstructorDTO::getDepartmentId, InstructorDTO::getDepartmentName)
                .containsExactly(instructor.getId(), user.getId(),
                        instructor.getFirstName(), instructor.getLastName(),
                        instructor.getPhoneNumber(), instructor.getDateOfBirth(), instructor.getGender(),
                        faculty.getId(), faculty.getName(),
                        department.getId(), department.getName());
        
        verify(validationService).getInstructorByIdOrThrow(instructor.getId());
        verify(validationService).getDepartmentByIdOrThrow(department.getId());
        verify(instructorRepository).save(any(Instructor.class));
        verifyNoMoreInteractions(instructorRepository);
    }
    
    // Delete ============================================================
    
    /**
     * Unit test for {@link InstructorService#deleteInstructor(Long)}.
     * <p>
     * <b>Scenario:</b> When deleting an instructor that exists and has no associations, the service should:
     * <ul>
     *   <li>Fetch the instructor via {@link ValidationService#getInstructorByIdOrThrow(Long)}</li>
     *   <li>Delete the instructor via {@link InstructorRepository#delete(Object)}</li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The instructor is deleted successfully</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> The instructor is deleted without exceptions.
     */
    @Test
    @DisplayName("Should delete instructor by ID")
    void shouldDeleteInstructorById_whenInstructorExistsAndHasNoCourses() {
        // Arrange: Prepare mocks and inputs
        when(validationService.getInstructorByIdOrThrow(instructor.getId())).thenReturn(instructor);
        doNothing().when(instructorRepository).delete(instructor);
        
        // Act: Call the method under test
        instructorService.deleteInstructor(instructor.getId());
        
        // Assert: Verify interactions
        verify(validationService).getInstructorByIdOrThrow(instructor.getId());
        verify(instructorRepository).delete(instructor);
        verifyNoMoreInteractions(validationService, instructorRepository);
    }
    
    // ================================================================
    // Negative Test Cases
    // ================================================================
    
    // Get ============================================================
    
    /**
     * Unit test for {@link InstructorService#getInstructorById(Long)}.
     * <p>
     * <b>Scenario:</b> When an instructor with the specified ID does not exist, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#getInstructorByIdOrThrow(Long)}</li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception is thrown with the expected message</li>
     *   <li>No unexpected interactions occur with the repository</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> NotFoundException is thrown.
     */
    @Test
    @DisplayName("Should throw NotFoundException when instructor ID does not exist")
    void shouldThrowNotFoundException_whenInstructorIdDoesNotExist() {
        // Arrange, Act & Assert: Use helper to assert not found behavior
        TestAssertionUtils.assertInstructorNotFound(() -> instructorService.getInstructorById(TestDataFactory.NON_EXISTENT_ID));
    }
    
    // Create ============================================================
    
    /**
     * Unit test for {@link InstructorService#createInstructor(InstructorDTO)}.
     * <p>
     * <b>Scenario:</b> When creating an instructor with department not set, the service should:
     * <ul>
     *   <li>Throw a {@link BadRequestException}</li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception is thrown with the expected message</li>
     *   <li>No unexpected interactions occur with the repository</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> BadRequestException is thrown.
     */
    @Test
    @DisplayName("Should throw BadRequestException when creating instructor with department not set")
    void shouldThrowBadRequestException_whenCreatingInstructorWithDepartmentNotSet() {
        // Arrange: Prepare mocks and inputs
        instructorDTO.setDepartmentId(null);
        
        // Act & Assert: Call the method and verify exception
        assertThatThrownBy(() -> instructorService.createInstructor(instructorDTO))
                .as("Should throw BadRequestException when department is not set")
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Instructor must be in a department");
    }
    
    /**
     * Unit test for {@link InstructorService#createInstructor(InstructorDTO)}.
     * <p>
     * <b>Scenario:</b> When creating an instructor with a non-existing department, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#getDepartmentByIdOrThrow(Long)}</li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception is thrown with the expected message</li>
     *   <li>No unexpected interactions occur with the repository</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> NotFoundException is thrown.
     */
    @Test
    @DisplayName("Should throw NotFoundException when creating instructor with non-existing department")
    void shouldThrowNotFoundException_whenCreatingInstructorWithNonExistingDepartment() {
        // Arrange: Prepare mocks and inputs
        instructorDTO.setDepartmentId(TestDataFactory.NON_EXISTENT_ID);
        
        // Act & Assert: Use helper to assert not found behavior
        TestAssertionUtils.assertDepartmentNotFound(() -> instructorService.createInstructor(instructorDTO));
    }
    
    // Update ============================================================
    
    /**
     * Unit test for {@link InstructorService#updateInstructor(Long, InstructorDTO)}.
     * <p>
     * <b>Scenario:</b> When updating an instructor with a non-existing ID, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#getInstructorByIdOrThrow(Long)}</li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception is thrown with the expected message</li>
     *   <li>No unexpected interactions occur with the repository</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> NotFoundException is thrown.
     */
    @Test
    @DisplayName("Should throw NotFoundException when updating instructor with non-existing ID")
    void shouldThrowNotFoundException_whenUpdatingNonExistingInstructor() {
        // Arrange, Act & Assert: Use helper to assert not found behavior
        TestAssertionUtils.assertInstructorNotFound(() -> instructorService.updateInstructor(TestDataFactory.NON_EXISTENT_ID, instructorDTO));
    }
    
    /**
     * Unit test for {@link InstructorService#updateInstructor(Long, InstructorDTO)}.
     * <p>
     * <b>Scenario:</b> When updating an instructor with department not set, the service should:
     * <ul>
     *   <li>Throw a {@link BadRequestException}</li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception is thrown with the expected message</li>
     *   <li>No unexpected interactions occur with the repository</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> BadRequestException is thrown.
     */
    @Test
    @DisplayName("Should throw BadRequestException when updating instructor with department not set")
    void shouldThrowBadRequestException_whenUpdatingInstructorWithDepartmentNotSet() {
        // Arrange: Prepare mocks and inputs
        instructorDTO.setDepartmentId(null);
        when(validationService.getInstructorByIdOrThrow(instructor.getId())).thenReturn(instructor);
        
        // Act & Assert: Call the method and verify exception
        assertThatThrownBy(() -> instructorService.updateInstructor(instructor.getId(), instructorDTO))
                .as("Should throw BadRequestException when department is not set")
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Instructor must be in a department");
        
        // Assert: Verify interactions
        verify(validationService).getInstructorByIdOrThrow(instructor.getId());
    }
    
    /**
     * Unit test for {@link InstructorService#updateInstructor(Long, InstructorDTO)}.
     * <p>
     * <b>Scenario:</b> When updating an instructor with a non-existing department, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#getDepartmentByIdOrThrow(Long)}</li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception is thrown with the expected message</li>
     *   <li>No unexpected interactions occur with the repository</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> NotFoundException is thrown.
     */
    @Test
    @DisplayName("Should throw NotFoundException when updating instructor with non-existing department")
    void shouldThrowNotFoundException_whenUpdatingInstructorWithNonExistingDepartment() {
        // Arrange : Prepare mocks and inputs
        instructorDTO.setDepartmentId(TestDataFactory.NON_EXISTENT_ID);
        
        // Act & Assert: Use helper to assert not found behavior
        when(validationService.getInstructorByIdOrThrow(instructor.getId())).thenReturn(instructor);
        TestAssertionUtils.assertDepartmentNotFound(() ->
                instructorService.updateInstructor(instructor.getId(), instructorDTO));
        
        // Assert: Verify interactions
        verify(validationService).getInstructorByIdOrThrow(instructor.getId());
    }
    
    // Delete ============================================================
    
    /**
     * Unit test for {@link InstructorService#deleteInstructor(Long)}.
     * <p>
     * <b>Scenario:</b> When deleting an instructor with a non-existing ID, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#getInstructorByIdOrThrow(Long)}</li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception is thrown with the expected message</li>
     *   <li>No unexpected interactions occur with the repository</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> NotFoundException is thrown.
     */
    @Test
    @DisplayName("Should throw NotFoundException when deleting instructor with non-existing ID")
    void shouldThrowNotFoundException_whenDeletingNonExistingInstructor() {
        // Arrange, Act & Assert: Use helper to assert not found behavior
        TestAssertionUtils.assertInstructorNotFound(() -> instructorService.deleteInstructor(TestDataFactory.NON_EXISTENT_ID));
    }
    
    /**
     * Unit test for {@link InstructorService#deleteInstructor(Long)}.
     * <p>
     * <b>Scenario:</b> When deleting an instructor with associated courses, the service should:
     * <ul>
     *   <li>Fetch the instructor via {@link ValidationService#getInstructorByIdOrThrow(Long)}</li>
     *   <li>Remove the instructor from all associated courses</li>
     *   <li>Delete the instructor</li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The instructor is deleted successfully</li>
     *   <li>No unexpected interactions occur with the repository</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> The instructor is deleted without exceptions.
     */
    @Test
    @DisplayName("Should delete instructor with associated courses")
    void shouldDeleteInstructorWithAssociatedCourses() {
        // Arrange: Prepare mocks and inputs
        Course course = TestDataFactory.buildCourse(department, null);
        instructor.setCourses(new ArrayList<>(List.of(course)));
        course.setInstructors(new ArrayList<>(List.of(instructor)));
        
        when(validationService.getInstructorByIdOrThrow(instructor.getId())).thenReturn(instructor);
        doNothing().when(instructorRepository).delete(instructor);
        
        // Act: Call the method under test
        instructorService.deleteInstructor(instructor.getId());
        
        // Assert: Verify interactions
        verify(validationService).getInstructorByIdOrThrow(instructor.getId());
        verify(instructorRepository).delete(instructor);
        verifyNoMoreInteractions(validationService, instructorRepository);
    }
}
