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

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link DepartmentServiceImpl}.
 * <p>
 * This test class verifies the correct behavior of the service by isolating it from
 * external dependencies:
 * <ul>
 *   <li>{@link DepartmentRepository} for database operations</li>
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
public class DepartmentServiceTest {
    
    // ================================================================
    // Mocks and Dependencies
    // ================================================================
    
    @Mock
    private DepartmentRepository departmentRepository;
    
    @Mock
    private ValidationServiceImpl validationService;
    
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
        
        // Create the service under test with mocked dependencies
        departmentService = new DepartmentServiceImpl(departmentRepository, departmentMapper, validationService);
        
        // Prepare a Faculty entity with [id = 1 and name = "Engineering"] to be used in tests
        faculty = TestDataFactory.buildFaculty();
        
        // Prepare a Department entity with [id = 1, name = "Computer Engineering", and the above faculty (id = 1)]
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
     * Scenario: When departments exist in the database, the service should:
     * <ul>
     *   <li>Fetch all department entities via {@link DepartmentRepository#findAll()}</li>
     *   <li>Map the entities to {@link DepartmentDTO} instances correctly</li>
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
    @DisplayName("Should return all departments")
    void shouldGetAllDepartments_whenTheyExist() {
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
     * Scenario: When a department with the given ID exists, the service should:
     * <ul>
     *   <li>Fetch the department entity via {@link ValidationService#getDepartmentByIdOrThrow(Long)}</li>
     *   <li>Map the entity to a {@link DepartmentDTO} instance</li>
     *   <li>Return the mapped DTO</li>
     * </ul>
     * <p>
     * Verifies:
     * <ul>
     *   <li>The returned DTO matches the expected values</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * <p>
     * Expected result: The returned DTO contains the correct department data.
     */
    @Test
    @DisplayName("Should return department by ID when it exists")
    void shouldGetDepartment_whenIdExists() {
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
     * Scenario: When updating an existing department with a unique name, the service should:
     * <ul>
     *   <li>Fetch the existing department entity via {@link ValidationService#getDepartmentByIdOrThrow(Long)}</li>
     *   <li>Validate that the new name is unique</li>
     *   <li>Fetch the faculty entity via {@link ValidationService#getFacultyByIdOrThrow(Long)}</li>
     *   <li>Update the existing entity with values from the DTO</li>
     *   <li>Save the updated entity via {@link DepartmentRepository#save(Object)}</li>
     *   <li>Return the updated department as a DTO</li>
     * </ul>
     * <p>
     * Verifies:
     * <ul>
     *   <li>The returned DTO matches the updated values</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * <p>
     * Expected result: The returned DTO contains the updated department data.
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
     * Scenario: When deleting an existing department, the service should:
     * <ul>
     *   <li>Fetch the existing department entity via {@link ValidationService#getDepartmentByIdOrThrow(Long)}</li>
     *   <li>Delete the entity via {@link DepartmentRepository#delete(Object)}</li>
     * </ul>
     * <p>
     * Verifies:
     * <ul>
     *   <li>No exceptions are thrown</li>
     *   <li>Correct interactions with the repository and validation service</li>
     * </ul>
     * <p>
     * Expected result: The department is deleted without errors.
     */
    @Test
    @DisplayName("Should delete an existing department when it has no associations")
    void shouldDeleteDepartment_whenIdExistsAndIsNotAssociatedWithAnyStudentsOrCourses() {
        // Arrange: prepare mocks and inputs
        when(validationService.getDepartmentByIdOrThrow(department.getId())).thenReturn(department);
        
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
     * Scenario: When a department with the given ID does not exist, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException}</li>
     * </ul>
     * <p>
     * Verifies:
     * <ul>
     *   <li>No interactions with the repository occur</li>
     * </ul>
     * <p>
     * Expected result: A {@code NotFoundException} is thrown indicating the department was not found.
     */
    @Test
    @DisplayName("Should throw NotFoundException when department does not exist")
    void shouldThrowNotFoundException_whenDepartmentDoesNotExist() {
        // Arrange, Act & Assert: Use helper to assert not found behavior
        assertDepartmentNotFound(() -> departmentService.getDepartmentById(TestDataFactory.NON_EXISTENT_ID));
    }
    
    // Create =========================================================
    
    /**
     * Unit test for {@link DepartmentService#createDepartment(DepartmentDTO)}.
     * <p>
     * Scenario: When creating a department without setting faculty , the service should:
     * <ul>
     *   <li>Throw a {@link BadRequestException}</li>
     * </ul>
     * <p>
     * Verifies:
     * <ul>
     *   <li>No interactions with the repository occur</li>
     * </ul>
     * <p>
     * Expected result: A {@code BadRequestException} is thrown indicating the department has no faculty.
     */
    @Test
    @DisplayName("Should throw Bad Request Exception when creating department without setting faculty")
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
     * Scenario: When creating a department with a non-existent faculty, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException}</li>
     * </ul>
     * <p>
     * Verifies:
     * <ul>
     *   <li>No interactions with the repository occur</li>
     * </ul>
     * <p>
     * Expected result: A {@code NotFoundException} is thrown indicating the faculty was not found.
     */
    @Test
    @DisplayName("Should throw Not Found Exception when creating department with non-existent faculty")
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
     * Scenario: When creating a department with a non-unique name, the service should:
     * <ul>
     *   <li>Throw a {@link ConflictException}</li>
     * </ul>
     * <p>
     * Verifies:
     * <ul>
     *   <li>No interactions with the repository occur</li>
     * </ul>
     * <p>
     * Expected result: A {@code ConflictException} is thrown indicating the department name is not unique.
     */
    @Test
    @DisplayName("Should throw Conflict Exception when creating department with non-unique name")
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
    }
    
    // Update =========================================================
    
    /**
     * Unit test for {@link DepartmentService#updateDepartment(Long, DepartmentDTO)}.
     * <p>
     * Scenario: When updating a department with a non-existent ID, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException}</li>
     * </ul>
     * <p>
     * Verifies:
     * <ul>
     *   <li>No interactions with the repository occur</li>
     * </ul>
     * <p>
     * Expected result: A {@code NotFoundException} is thrown indicating the department was not found.
     */
    @Test
    @DisplayName("Should throw Not Found Exception when updating department with non-existent ID")
    void shouldThrowNotFoundException_whenUpdatingNonExistingDepartment() {
        // Arrange, Act & Assert: Use helper to assert not found behavior
        assertDepartmentNotFound(() -> departmentService.updateDepartment(TestDataFactory.NON_EXISTENT_ID, departmentDTO));
    }
    
    /**
     * Unit test for {@link DepartmentService#updateDepartment(Long, DepartmentDTO)}.
     * <p>
     * Scenario: When updating a department with a non-unique name, the service should:
     * <ul>
     *   <li>Throw a {@link ConflictException}</li>
     * </ul>
     * <p>
     * Verifies:
     * <ul>
     *   <li>No interactions with the repository occur</li>
     * </ul>
     * <p>
     * Expected result: A {@code ConflictException} is thrown indicating the department name is not unique.
     */
    @Test
    @DisplayName("Should throw Conflict Exception when updating department with non-unique name")
    void shouldThrowConflictException_whenUpdatingDepartmentWithNonUniqueName() {
        // Arrange: prepare mocks and inputs
        DepartmentDTO updatedDTO = TestDataFactory.buildDepartmentDTO(department.getId(), "New Computer Engineering", faculty.getId());
        when(validationService.getDepartmentByIdOrThrow(department.getId())).thenReturn(department);
        doThrow(new ConflictException("Department with name '" + updatedDTO.getName() + "' already exists",
                "DEPARTMENT_ALREADY_EXISTS")
        ).when(validationService).assertDepartmentNameUnique(updatedDTO.getName());
        
        // Act & Assert: Execute the service call and expect an exception
        assertThatThrownBy(() -> departmentService.updateDepartment(department.getId(), updatedDTO))
                .as("Service should throw Conflict Exception when Department name is not unique")
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Department with name '" + updatedDTO.getName() + "' already exists");
        
        // Assert: Verify interactions
        verify(validationService).assertDepartmentNameUnique(updatedDTO.getName());
        verifyNoInteractions(departmentRepository);
    }
    
    // Delete =========================================================
    
    /**
     * Unit test for {@link DepartmentService#deleteDepartment(Long)}.
     * <p>
     * Scenario: When deleting a department with a non-existent ID, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException}</li>
     * </ul>
     * <p>
     * Verifies:
     * <ul>
     *   <li>No interactions with the repository occur</li>
     * </ul>
     * <p>
     * Expected result: A {@code NotFoundException} is thrown indicating the department was not found.
     */
    @Test
    @DisplayName("Should throw Not Found Exception when deleting department with non-existent ID")
    void shouldThrowNotFoundException_whenDeletingNonExistingDepartment() {
        // Arrange, Act & Assert: Use helper to assert not found behavior
        assertDepartmentNotFound(() -> departmentService.deleteDepartment(TestDataFactory.NON_EXISTENT_ID));
    }
    
    /**
     * Unit test for {@link DepartmentService#deleteDepartment(Long)}.
     * <p>
     * Scenario: When deleting a department that has associations with students or courses, the service should:
     * <ul>
     *   <li>Throw a {@link ConflictException}</li>
     * </ul>
     * <p>
     * Verifies:
     * <ul>
     *   <li>No interactions with the repository occur</li>
     * </ul>
     * <p>
     * Expected result: A {@code ConflictException} is thrown indicating the department has associations.
     */
    @Test
    @DisplayName("Should throw Conflict Exception when deleting department with associations")
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
                .as("Service should throw Conflict Exception when Department has associations with students or courses")
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Cannot delete department with id " + department.getId() + " because it has associated students or courses");
        
        // Assert: Verify interactions
        verify(validationService).getDepartmentByIdOrThrow(department.getId());
        verifyNoInteractions(departmentRepository);
    }
    
    // ================================================================
    // Utility Methods
    // ================================================================
    
    /**
     * Utility to verify that a service call throws {@link NotFoundException} for a missing department.
     *
     * @param executable The service call expected to throw.
     */
    private void assertDepartmentNotFound(@NotNull Runnable executable) {
        // Arrange: Stub validation service to throw NotFoundException
        doThrow(new NotFoundException("Department with id " + TestDataFactory.NON_EXISTENT_ID + " not found"
                , "DEPARTMENT_NOT_FOUND")
        ).when(validationService).getDepartmentByIdOrThrow(TestDataFactory.NON_EXISTENT_ID);
        
        // Act : Execute the service call and expect an exception
        assertThatThrownBy(executable::run)
                .as("Service should throw Not Found Exception when Department ID does not exist")
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Department with id " + TestDataFactory.NON_EXISTENT_ID + " not found");
        
        // Assert: Verify interactions
        verify(validationService).getDepartmentByIdOrThrow(TestDataFactory.NON_EXISTENT_ID);
        verifyNoInteractions(departmentRepository);
        verifyNoMoreInteractions(validationService);
    }
}
