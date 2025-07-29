package com.egabi.university.service.academic;

import com.egabi.university.dto.DepartmentDTO;
import com.egabi.university.entity.Course;
import com.egabi.university.entity.Department;
import com.egabi.university.entity.Faculty;
import com.egabi.university.entity.Student;
import com.egabi.university.exception.BadRequestException;
import com.egabi.university.exception.ConflictException;
import com.egabi.university.exception.NotFoundException;
import com.egabi.university.mapper.DepartmentMapper;
import com.egabi.university.repository.DepartmentRepository;
import com.egabi.university.service.academic.impl.DepartmentServiceImpl;
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
 * Comprehensive unit tests for {@link DepartmentServiceImpl} using JUnit 5 and Mockito.
 * <p>
 * These tests verify the service logic in isolation from external dependencies:
 * <ul>
 *   <li>{@link DepartmentRepository} for database operations</li>
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
public class DepartmentServiceTest {
    
    // ================================================================
    // Mocks and Dependencies
    // ================================================================
    
    @Mock
    private DepartmentRepository departmentRepository;
    
    @Mock
    private ValidationService validationService;
    
    private DepartmentServiceImpl departmentService;
    
    private Department department;
    private DepartmentDTO departmentDTO;
    private Faculty faculty;
    
    // ================================================================
    // Setup : Arrange common test fixtures
    // ================================================================
    
    @BeforeEach
    void setUp() {
        // Initialize the mapper using MapStruct factory
        DepartmentMapper departmentMapper = Mappers.getMapper(DepartmentMapper.class);
        
        // Set the validation service in the TestAssertionUtils
        TestAssertionUtils.setValidationService(validationService);
        
        
        // Create the service under test with mocked dependencies
        departmentService = new DepartmentServiceImpl(departmentRepository, departmentMapper, validationService);
        
        // Prepare a Faculty entity with [id = 1 and name = "Engineering"]
        faculty = TestDataFactory.buildFaculty();
        
        // Prepare a Department entity with [id = 1, name = "Computer Engineering", faculty]
        department = TestDataFactory.buildDepartment(faculty);
        // Prepare a simple DTO  from the entity for input/output tests
        departmentDTO = TestDataFactory.buildDepartmentDTO();
    }
    
    // ================================================================
    // Positive Test Cases
    // ================================================================
    
    // Get ============================================================
    
    /**
     * Unit test for {@link DepartmentService#getAllDepartments()}.
     * <p>
     * <b>Scenario:</b> When departments exist in the database, the service should:
     * <ul>
     *   <li>Fetch all department entities via {@link DepartmentRepository#findAll()}</li>
     *   <li>Map the entities to {@link DepartmentDTO} instances correctly</li>
     *   <li>Return a list containing all mapped DTOs</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The returned list has the expected size and content</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> The list contains all departments mapped to DTOs.
     */
    @Test
    @DisplayName("Should return all departments when departments exist")
    void shouldReturnAllDepartments_whenDepartmentsExist() {
        // Arrange: prepare mocks and inputs
        when(departmentRepository.findAll()).thenReturn(List.of(department));
        
        // Act: Call the method under test
        List<DepartmentDTO> result = departmentService.getAllDepartments();
        
        // Assert: Verify the output and interactions
        assertThat(result).as("Returned list should contain exactly one department").hasSize(1);
        assertThat(result.getFirst())
                .as("Returned department should match the mock entity")
                .extracting(DepartmentDTO::getId, DepartmentDTO::getName, DepartmentDTO::getFacultyId)
                .containsExactly(department.getId(), department.getName(), department.getFaculty().getId());
        
        verify(departmentRepository).findAll();
        verifyNoMoreInteractions(departmentRepository);
    }
    
    /**
     * Unit test for {@link DepartmentService#getDepartmentById(Long)}.
     * <p>
     * <b>Scenario:</b> When a department with the given ID exists, the service should:
     * <ul>
     *   <li>Fetch the department entity via {@link ValidationService#getDepartmentByIdOrThrow(Long)}</li>
     *   <li>Map the entity to a {@link DepartmentDTO} instance</li>
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
    @DisplayName("Should return department by ID when it exists")
    void shouldReturnDepartmentById_whenDepartmentExists() {
        // Arrange: prepare mocks and inputs
        when(validationService.getDepartmentByIdOrThrow(department.getId())).thenReturn(department);
        
        // Act: Call the method under test
        DepartmentDTO result = departmentService.getDepartmentById(department.getId());
        
        // Assert: Verify the output and interactions
        assertThat(result)
                .as("Returned department should match the mock entity")
                .extracting(DepartmentDTO::getId, DepartmentDTO::getName, DepartmentDTO::getFacultyId)
                .containsExactly(department.getId(), department.getName(), department.getFaculty().getId());
        
        verify(validationService).getDepartmentByIdOrThrow(department.getId());
        verifyNoMoreInteractions(departmentRepository);
    }
    
    // Create =========================================================
    
    @Test
    @DisplayName("Should create a new department that has existing faculty and unique name")
    void shouldCreateDepartment_whenExistingFacultyIsSetAndNameIsUnique() {
        // Arrange: prepare mocks and inputs
        when(validationService.getFacultyByIdOrThrow(faculty.getId())).thenReturn(faculty);
        doNothing().when(validationService).assertDepartmentNameUnique(departmentDTO.getName());
        when(departmentRepository.save(any(Department.class))).thenReturn(department);
        
        // Act: Call the method under test
        DepartmentDTO result = departmentService.createDepartment(departmentDTO);
        
        // Assert: Verify the output and interactions
        assertThat(result)
                .as("Returned department should match the mock entity")
                .extracting(DepartmentDTO::getId, DepartmentDTO::getName, DepartmentDTO::getFacultyId)
                .containsExactly(department.getId(), department.getName(), faculty.getId());
        
        verify(validationService).assertDepartmentNameUnique(departmentDTO.getName());
        verify(validationService).getFacultyByIdOrThrow(faculty.getId());
        verify(departmentRepository).save(any(Department.class));
        verifyNoMoreInteractions(departmentRepository, validationService);
    }
    
    // Update =========================================================
    
    
    /**
     * Unit test for {@link DepartmentService#updateDepartment(Long, DepartmentDTO)}.
     * <p>
     * <b>Scenario:</b> When updating an existing department with the same name, the service should:
     * <ul>
     *   <li>Fetch the existing department entity via {@link ValidationService#getDepartmentByIdOrThrow(Long)}</li>
     *   <li>Return the existing entity as a DTO without changes</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The returned DTO matches the original entity</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> The returned DTO contains the unchanged department data.
     */
    @Test
    @DisplayName("Should return unchanged department when updating with same name")
    void shouldReturnUnchangedDepartment_whenExistsAndUpdatingWithSameName() {
        // Arrange: prepare mocks and inputs
        when(validationService.getDepartmentByIdOrThrow(department.getId())).thenReturn(department);
        
        // Act: Call the method under test
        DepartmentDTO result = departmentService.updateDepartment(department.getId(), departmentDTO);
        
        // Assert: Verify the output and interactions
        assertThat(result)
                .as("Returned department should match the original entity")
                .extracting(DepartmentDTO::getId, DepartmentDTO::getName, DepartmentDTO::getFacultyId)
                .containsExactly(department.getId(), department.getName(), faculty.getId());
        
        verify(validationService).getDepartmentByIdOrThrow(department.getId());
        verify(validationService, never()).assertDepartmentNameUnique(anyString());
        verify(departmentRepository, never()).save(any(Department.class));
        verifyNoMoreInteractions(departmentRepository, validationService);
    }
    
    /**
     * Unit test for {@link DepartmentService#updateDepartment(Long, DepartmentDTO)}.
     * <p>
     * <b>Scenario:</b> When updating an existing department with a unique name, the service should:
     * <ul>
     *   <li>Fetch the existing department entity via {@link ValidationService#getDepartmentByIdOrThrow(Long)}</li>
     *   <li>Validate that the new name is unique</li>
     *   <li>Fetch the faculty entity via {@link ValidationService#getFacultyByIdOrThrow(Long)}</li>
     *   <li>Update the existing entity with values from the DTO</li>
     *   <li>Save the updated entity via {@link DepartmentRepository#save(Object)}</li>
     *   <li>Return the updated department as a DTO</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The returned DTO matches the updated values</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> The returned DTO contains the updated department data.
     */
    @Test
    @DisplayName("Should update an existing department with unique name")
    void shouldUpdateDepartment_whenIdExistsAndNameIsUnique() {
        // Arrange: prepare mocks and inputs
        DepartmentDTO updatedDTO = TestDataFactory.buildDepartmentDTO(department.getId(), "New Computer Engineering", faculty.getId());
        
        when(validationService.getDepartmentByIdOrThrow(department.getId())).thenReturn(department);
        doNothing().when(validationService).assertDepartmentNameUnique(updatedDTO.getName());
        when(departmentRepository.save(any(Department.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act: Call the method under test
        DepartmentDTO result = departmentService.updateDepartment(department.getId(), updatedDTO);
        
        // Assert: Verify the output and interactions
        assertThat(result)
                .as("Returned department should match the mock updated entity")
                .extracting(DepartmentDTO::getId, DepartmentDTO::getName, DepartmentDTO::getFacultyId)
                .containsExactly(department.getId(), updatedDTO.getName(), faculty.getId());
        
        verify(validationService).getDepartmentByIdOrThrow(department.getId());
        verify(validationService).assertDepartmentNameUnique(updatedDTO.getName());
        verify(departmentRepository).save(any(Department.class));
        verifyNoMoreInteractions(departmentRepository, validationService);
    }
    
    // Delete =========================================================
    
    /**
     * Unit test for {@link DepartmentService#deleteDepartment(Long)}.
     * <p>
     * <b>Scenario:</b> When deleting an existing department that has no associations, the service should:
     * <ul>
     *   <li>Fetch the existing department entity via {@link ValidationService#getDepartmentByIdOrThrow(Long)}</li>
     *   <li>Check for associations (students or courses) and ensure there are none</li>
     *   <li>Delete the entity via {@link DepartmentRepository#delete(Object)}</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>No exceptions are thrown</li>
     *   <li>The repository delete method is called with the correct entity</li>
     *   <li>No unexpected interactions occur with the validation service or repository</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> The department is deleted successfully
     */
    @Test
    @DisplayName("Should delete an existing department that  has no associations")
    void shouldDeleteDepartmentById_whenDepartmentExistsAndHasNoAssociations() {
        // Arrange: prepare mocks and inputs
        when(validationService.getDepartmentByIdOrThrow(department.getId())).thenReturn(department);
        // TODO in level and faculty doNothing
        doNothing().when(departmentRepository).delete(any(Department.class));
        
        // Act: Call the method under test
        departmentService.deleteDepartment(department.getId());
        
        // Assert: Verify the interactions
        verify(validationService).getDepartmentByIdOrThrow(department.getId());
        verify(departmentRepository).delete(department);
        verifyNoMoreInteractions(departmentRepository, validationService);
    }
    
    // ================================================================
    // Negative Test Cases
    // ================================================================
    
    // Get ============================================================
    
    /**
     * Unit test for {@link DepartmentService#getDepartmentById(Long)}.
     * <p>
     * <b>Scenario:</b> When attempting to retrieve a department by an ID that does not exist,
     * the service should:
     * <ul>
     *   <li>Call {@link ValidationService#getDepartmentByIdOrThrow(Long)} to check existence</li>
     *   <li>Throw a {@link NotFoundException} if the department is not found</li>
     *   <li>Never call {@link DepartmentRepository} since the department does not exist</li>
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
    @DisplayName("Should throw NotFoundException when department ID does not exist")
    void shouldThrowNotFoundException_whenDepartmentIdDoesNotExist() {
        // Arrange, Act & Assert: Use helper to assert not found behavior
        TestAssertionUtils.assertDepartmentNotFound(() -> departmentService.getDepartmentById(TestDataFactory.NON_EXISTENT_ID));
    }
    
    // Create =========================================================
    
    /**
     * Unit test for {@link DepartmentService#createDepartment(DepartmentDTO)}.
     * <p>
     * <b>Scenario:</b> When creating a department without setting the faculty, the service should:
     * <ul>
     *   <li>Throw a {@link BadRequestException}</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception type is thrown</li>
     *   <li>No interactions with the repository occur</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> A {@code BadRequestException} is thrown indicating the faculty must be set.
     */
    @Test
    @DisplayName("Should throw BadRequestException when creating department without setting faculty")
    void shouldThrowBadRequestException_whenCreatingDepartmentWithoutSettingFaculty() {
        // Arrange : prepare inputs
        departmentDTO.setFacultyId(null);
        
        // Act & Assert: Execute the service call and expect an exception
        assertThatThrownBy(() -> departmentService.createDepartment(departmentDTO))
                .as("Service should throw Not Found Exception when Faculty ID is not set")
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Department must be in a faculty");
        
        // Assert: Verify interactions
        verifyNoInteractions(departmentRepository);
    }
    
    /**
     * Unit test for {@link DepartmentService#createDepartment(DepartmentDTO)}.
     * <p>
     * <b>Scenario:</b> When creating a department with a non-existent faculty, the service should:
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
    @DisplayName("Should throw NotFoundException when creating department with non-existent faculty")
    void shouldThrowNotFoundException_whenCreatingDepartmentWithNonExistentFaculty() {
        // Arrange: prepare mocks and inputs
        departmentDTO.setFacultyId(TestDataFactory.NON_EXISTENT_ID);
        doThrow(new NotFoundException("Faculty with id " + TestDataFactory.NON_EXISTENT_ID + " not found"
                , "FACULTY_NOT_FOUND")
        ).when(validationService).getFacultyByIdOrThrow(TestDataFactory.NON_EXISTENT_ID);
        
        // Act & Assert: Execute the service call and expect an exception
        assertThatThrownBy(() -> departmentService.createDepartment(departmentDTO))
                .as("Service should throw Not Found Exception when Faculty ID does not exist")
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Faculty with id " + TestDataFactory.NON_EXISTENT_ID + " not found");
        
        // Assert: Verify interactions
        verify(validationService).getFacultyByIdOrThrow(TestDataFactory.NON_EXISTENT_ID);
        verifyNoInteractions(departmentRepository);
    }
    
    /**
     * Unit test for {@link DepartmentService#createDepartment(DepartmentDTO)}.
     * <p>
     * <b>Scenario:</b> When attempting to create a department with a name that already exists,
     * the service should:
     * <ul>
     *   <li>Call {@link ValidationService#assertDepartmentNameUnique(String)} to check uniqueness</li>
     *   <li>Throw a {@link ConflictException} if the name is already taken</li>
     *   <li>Never call {@link DepartmentRepository} to persist the duplicate department</li>
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
    @DisplayName("Should throw ConflictException when creating department with non-unique name")
    void shouldThrowConflictException_whenCreatingDepartmentWithNonUniqueName() {
        // Arrange: prepare mocks and inputs
        doThrow(new ConflictException("Department with name '" + department.getName() + "' already exists",
                "DEPARTMENT_ALREADY_EXISTS")
        ).when(validationService).assertDepartmentNameUnique(departmentDTO.getName());
        
        // Act & Assert: Execute the service call and expect an exception
        assertThatThrownBy(() -> departmentService.createDepartment(departmentDTO))
                .as("Service should throw Conflict Exception when Department name is not unique")
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Department with name '" + department.getName() + "' already exists");
        
        // Assert: Verify interactions
        verify(validationService).assertDepartmentNameUnique(departmentDTO.getName());
        verifyNoInteractions(departmentRepository);
        verifyNoMoreInteractions(validationService);
    }
    
    // Update =========================================================
    
    /**
     * Unit test for {@link DepartmentService#updateDepartment(Long, DepartmentDTO)}.
     * <p>
     * <b>Scenario:</b> When updating a department with a non-existent ID, the service should:
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
     * <b>Expected result:</b> A {@code NotFoundException} is thrown indicating the department was not found.
     */
    @Test
    @DisplayName("Should throw NotFoundException when updating department with non-existent ID")
    void shouldThrowNotFoundException_whenUpdatingNonExistingDepartment() {
        // Arrange, Act & Assert: Use helper to assert not found behavior
        TestAssertionUtils.assertDepartmentNotFound(() -> departmentService.updateDepartment(TestDataFactory.NON_EXISTENT_ID, departmentDTO));
    }
    
    /**
     * Unit test for {@link DepartmentService#updateDepartment(Long, DepartmentDTO)}.
     * <p>
     * <b>Scenario:</b> When updating a department with a non-unique name, the service should:
     * <ul>
     *   <li>Throw a {@link ConflictException}</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception type is thrown</li>
     *   <li>No interactions with the repository occur</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> A {@code ConflictException} is thrown indicating the department name is not unique.
     */
    @Test
    @DisplayName("Should throw ConflictException when updating department with non-unique name")
    void shouldThrowConflictException_whenUpdatingDepartmentWithNonUniqueName() {
        // Arrange: prepare mocks and inputs
        DepartmentDTO updatedDTO = TestDataFactory.buildDepartmentDTO(department.getId(), "New Computer Engineering", faculty.getId());
        when(validationService.getDepartmentByIdOrThrow(department.getId())).thenReturn(department);
        doThrow(new ConflictException("Department with name '" + updatedDTO.getName() + "' already exists",
                "DEPARTMENT_ALREADY_EXISTS")
        ).when(validationService).assertDepartmentNameUnique(updatedDTO.getName());
        
        // Act & Assert: Execute the service call and expect an exception
        assertThatThrownBy(() -> departmentService.updateDepartment(department.getId(), updatedDTO))
                .as("Service should throw ConflictException when Department name is not unique")
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Department with name '" + updatedDTO.getName() + "' already exists");
        
        // Assert: Verify interactions
        verify(validationService).getDepartmentByIdOrThrow(department.getId());
        verify(validationService).assertDepartmentNameUnique(updatedDTO.getName());
        verifyNoInteractions(departmentRepository);
        verifyNoMoreInteractions(validationService);
    }
    
    // Delete =========================================================
    
    /**
     * Unit test for {@link DepartmentService#deleteDepartment(Long)}.
     * <p>
     * <b>Scenario:</b> When deleting a department with a non-existent ID, the service should:
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
     * <b>Expected result:</b> A {@code NotFoundException} is thrown indicating the department was not found.
     */
    @Test
    @DisplayName("Should throw NotFoundException when deleting department with non-existent ID")
    void shouldThrowNotFoundException_whenDeletingNonExistingDepartment() {
        // Arrange, Act & Assert: Use helper to assert not found behavior
        TestAssertionUtils.assertDepartmentNotFound(() -> departmentService.deleteDepartment(TestDataFactory.NON_EXISTENT_ID));
    }
    
    /**
     * Unit test for {@link DepartmentService#deleteDepartment(Long)}.
     * <p>
     * <b>Scenario:</b> When deleting a department that has associations with students or courses, the service should:
     * <ul>
     *   <li>Fetch the department entity via {@link ValidationService#getDepartmentByIdOrThrow(Long)}</li>
     *   <li>Check for associations (students or courses) and throw a {@link ConflictException} if any exist</li>
     *   <li>Never call {@link DepartmentRepository#delete(Object)} since deletion is not allowed</li>
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
    @DisplayName("Should throw ConflictException when deleting department with associations")
    void shouldThrowConflictException_whenDeletingDepartmentWithAssociations() {
        // Arrange: prepare mocks and inputs
        when(validationService.getDepartmentByIdOrThrow(department.getId())).thenReturn(department);
        // TODO change this to use a real Test Data Factory
        Student student = new Student();
        Course course = new Course();
        department.setStudents(List.of(student));
        department.setCourses(List.of(course));
        
        // Act & Assert: Execute the service call and expect an exception
        assertThatThrownBy(() -> departmentService.deleteDepartment(department.getId()))
                .as("Service should throw ConflictException when Department has associations with students or courses")
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Cannot delete department with id " + department.getId() + " because it has associated students or courses");
        
        // Assert: Verify interactions
        verify(validationService).getDepartmentByIdOrThrow(department.getId());
        verifyNoInteractions(departmentRepository);
        verifyNoMoreInteractions(validationService);
    }
}
