package com.egabi.university.service.academic;

import com.egabi.university.dto.StudentDTO;
import com.egabi.university.entity.*;
import com.egabi.university.entity.authentication.Role;
import com.egabi.university.entity.authentication.User;
import com.egabi.university.exception.BadRequestException;
import com.egabi.university.exception.ConflictException;
import com.egabi.university.exception.NotFoundException;
import com.egabi.university.mapper.StudentMapper;
import com.egabi.university.repository.StudentRepository;
import com.egabi.university.service.academic.impl.StudentServiceImpl;
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
 * Comprehensive unit tests for {@link StudentServiceImpl} using JUnit 5 and Mockito.
 * <p>
 * These tests verify the service logic in isolation from external dependencies:
 * <ul>
 *   <li>{@link StudentRepository} for database operations</li>
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
public class StudentServiceTest {
    
    // ================================================================
    // Mocks and Dependencies
    // ================================================================
    
    @Mock
    private StudentRepository studentRepository;
    
    @Mock
    private ValidationService validationService;
    
    private StudentServiceImpl studentService;
    
    private Student student;
    private StudentDTO studentDTO;
    private User user;
    private Faculty faculty;
    private Department department;
    private Level level;
    
    // ================================================================
    // Setup : Arrange common test fixtures
    // ================================================================
    
    @BeforeEach
    void setUp() {
        // Initialize the mapper using MapStruct factory
        StudentMapper studentMapper = Mappers.getMapper(StudentMapper.class);
        
        // Set the validation service in the TestAssertionUtils
        TestAssertionUtils.setValidationService(validationService);
        
        // Create the service under test with mocked dependencies
        studentService = new StudentServiceImpl(studentRepository, studentMapper, validationService);
        
        // Prepare test data
        faculty = TestDataFactory.buildFaculty();
        user = TestDataFactory.buildUser(Role.STUDENT);
        department = TestDataFactory.buildDepartment(faculty);
        level = TestDataFactory.buildLevel(faculty);
        student = TestDataFactory.buildStudent(user, department, level);
        studentDTO = TestDataFactory.buildStudentDTO();
        // Modify the DTO to have different values to trigger update
        studentDTO.setFirstName("Updated Student");
        studentDTO.setLastName("Updated One");
    }
    
    // ================================================================
    // Positive Test Cases
    // ================================================================
    
    // Get ============================================================
    
    /**
     * Unit test for {@link StudentService#getAllStudents()}.
     * <p>
     * <b>Scenario:</b> When students exist in the database, the service should:
     * <ul>
     *   <li>Fetch all student entities via {@link StudentRepository#findAll()}</li>
     *   <li>Map the entities to {@link StudentDTO} instances correctly</li>
     *   <li>Return a list containing all mapped DTOs</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The returned list has the expected size and content</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> The list contains all students mapped to DTOs.
     */
    @Test
    @DisplayName("Should return all students when students exist")
    void shouldReturnAllStudents_whenStudentsExist() {
        // Arrange: Prepare mocks and inputs
        when(studentRepository.findAll()).thenReturn(List.of(student));
        
        // Act: Call the method under test
        List<StudentDTO> result = studentService.getAllStudents();
        
        // Assert: Verify output and interactions
        assertThat(result)
                .as("Returned list should contain exactly one student")
                .hasSize(1);
        assertThat(result.getFirst())
                .as("Returned student should match the mock entity")
                .extracting(StudentDTO::getId, StudentDTO::getUserId,
                        StudentDTO::getFirstName, StudentDTO::getLastName,
                        StudentDTO::getPhoneNumber, StudentDTO::getDateOfBirth, StudentDTO::getGender,
                        StudentDTO::getFacultyId, StudentDTO::getFacultyName,
                        StudentDTO::getDepartmentId, StudentDTO::getDepartmentName,
                        StudentDTO::getLevelId, StudentDTO::getLevelName)
                .containsExactly(student.getId(), user.getId(),
                        student.getFirstName(), student.getLastName(),
                        student.getPhoneNumber(), student.getDateOfBirth(), student.getGender(),
                        faculty.getId(), faculty.getName(),
                        department.getId(), department.getName(),
                        level.getId(), level.getName());
        
        verify(studentRepository).findAll();
        verifyNoMoreInteractions(validationService, studentRepository);
    }
    
    /**
     * Unit test for {@link StudentService#getStudentById(Long)}.
     * <p>
     * <b>Scenario:</b> When a student with the specified ID exists, the service should:
     * <ul>
     *   <li>Fetch the student entity via {@link ValidationService#getStudentByIdOrThrow(Long)}</li>
     *   <li>Map the entity to a {@link StudentDTO} instance</li>
     *   <li>Return the mapped DTO</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The returned DTO has the expected ID, firstName, and lastName</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> The DTO contains the correct student data.
     */
    @Test
    @DisplayName("Should return student by ID when it exists")
    void shouldReturnStudentById_whenStudentExists() {
        // Arrange: Prepare mocks and inputs
        when(validationService.getStudentByIdOrThrow(student.getId())).thenReturn(student);
        
        // Act: Call the method under test
        StudentDTO result = studentService.getStudentById(student.getId());
        
        // Assert: Verify output and interactions
        assertThat(result)
                .as("Returned student should match the mock entity")
                .extracting(StudentDTO::getId, StudentDTO::getUserId,
                        StudentDTO::getFirstName, StudentDTO::getLastName,
                        StudentDTO::getPhoneNumber, StudentDTO::getDateOfBirth, StudentDTO::getGender,
                        StudentDTO::getFacultyId, StudentDTO::getFacultyName,
                        StudentDTO::getDepartmentId, StudentDTO::getDepartmentName,
                        StudentDTO::getLevelId, StudentDTO::getLevelName)
                .containsExactly(student.getId(), user.getId(),
                        student.getFirstName(), student.getLastName(),
                        student.getPhoneNumber(), student.getDateOfBirth(), student.getGender(),
                        faculty.getId(), faculty.getName(),
                        department.getId(), department.getName(),
                        level.getId(), level.getName());
        
        verify(validationService).getStudentByIdOrThrow(student.getId());
        verifyNoMoreInteractions(validationService);
    }
    
    // Create ============================================================
    
    /**
     * Unit test for {@link StudentService#createStudent(StudentDTO)}.
     * <p>
     * <b>Scenario:</b> When creating a new student, the service should:
     * <ul>
     *   <li>Map the DTO to a {@link Student} entity</li>
     *   <li>Validate the student associations [department and level]</li>
     *   <li>Save the entity via {@link StudentRepository#save(Object)}</li>
     *   <li>Return the saved entity as a DTO</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The returned DTO has the expected firstName and lastName</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> The DTO contains the correct student data.
     */
    @Test
    @DisplayName("Should create a new student when all required fields are set")
    void shouldCreateStudent_whenAllRequiredFieldsExistAndAreSet() {
        // Arrange: Prepare mocks and inputs
        student.setUser(null);
        when(validationService.getDepartmentByIdOrThrow(department.getId())).thenReturn(department);
        when(validationService.getLevelByIdOrThrow(level.getId())).thenReturn(level);
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        
        // Act: Call the method under test
        StudentDTO result = studentService.createStudent(studentDTO);
        
        // Assert: Verify output and interactions
        assertThat(result)
                .as("Returned student should match the mock entity")
                .extracting(StudentDTO::getId, StudentDTO::getFirstName, StudentDTO::getLastName,
                        StudentDTO::getPhoneNumber, StudentDTO::getDateOfBirth, StudentDTO::getGender,
                        StudentDTO::getFacultyId, StudentDTO::getFacultyName,
                        StudentDTO::getDepartmentId, StudentDTO::getDepartmentName,
                        StudentDTO::getLevelId, StudentDTO::getLevelName)
                .containsExactly(student.getId(), student.getFirstName(), student.getLastName(),
                        student.getPhoneNumber(), student.getDateOfBirth(), student.getGender(),
                        faculty.getId(), faculty.getName(),
                        department.getId(), department.getName(),
                        level.getId(), level.getName());
        
        verify(validationService).getDepartmentByIdOrThrow(department.getId());
        verify(validationService).getLevelByIdOrThrow(level.getId());
        verify(studentRepository).save(any(Student.class));
        verifyNoMoreInteractions(validationService, studentRepository);
    }
    
    /**
     * Unit test for {@link StudentService#createStudent(StudentDTO, User)}.
     * <p>
     * <b>Scenario:</b> When creating a new student with associated user, the service should:
     * <ul>
     *   <li>Map the DTO to a {@link Student} entity</li>
     *   <li>Associate the user with the student</li>
     *   <li>Validate the student associations [department and level]</li>
     *   <li>Save the entity via {@link StudentRepository#save(Object)}</li>
     *   <li>Return the saved entity as a DTO</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The returned DTO has the expected firstName and lastName</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> The DTO contains the correct student data with user association.
     */
    @Test
    @DisplayName("Should create a new student with associated user")
    void shouldCreateStudentWithUser_whenAllRequiredFieldsExistAndAreSet() {
        // Arrange: Prepare mocks and inputs
        when(validationService.getLevelByIdOrThrow(level.getId())).thenReturn(level);
        when(validationService.getDepartmentByIdOrThrow(department.getId())).thenReturn(department);
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        
        // Act: Call the method under test
        StudentDTO result = studentService.createStudent(studentDTO, user);
        
        // Assert: Verify output and interactions
        assertThat(result)
                .as("Returned student should match the mock entity")
                .extracting(StudentDTO::getId, StudentDTO::getUserId,
                        StudentDTO::getFirstName, StudentDTO::getLastName,
                        StudentDTO::getFacultyId, StudentDTO::getFacultyName,
                        StudentDTO::getDepartmentId, StudentDTO::getDepartmentName,
                        StudentDTO::getLevelId, StudentDTO::getLevelName)
                .containsExactly(student.getId(), user.getId(),
                        student.getFirstName(), student.getLastName(),
                        faculty.getId(), faculty.getName(),
                        department.getId(), department.getName(),
                        level.getId(), level.getName());
        
        verify(validationService).getLevelByIdOrThrow(level.getId());
        verify(validationService).getDepartmentByIdOrThrow(department.getId());
        verify(studentRepository).save(any(Student.class));
        verifyNoMoreInteractions(validationService, studentRepository);
    }
    
    // Update ============================================================
    
    //TODO add the cases of updating with same data
    
    /**
     * Unit test for {@link StudentService#updateStudent(Long, StudentDTO)}.
     * <p>
     * <b>Scenario:</b> When updating an existing student, the service should:
     * <ul>
     *   <li>Fetch the existing student via {@link ValidationService#getStudentByIdOrThrow(Long)}</li>
     *   <li>Update the entity with new data</li>
     *   <li>Save the updated entity via {@link StudentRepository#save(Object)}</li>
     *   <li>Return the updated entity as a DTO</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The returned DTO has the updated values</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> The DTO contains the updated student data.
     */
    @Test
    @DisplayName("Should update student when it exists")
    void shouldUpdateStudent_whenStudentExistsAndAllRequiredFieldsExistAndAreSet() {
        // Arrange: Prepare mocks and inputs
        when(validationService.getStudentByIdOrThrow(student.getId())).thenReturn(student);
        when(validationService.getDepartmentByIdOrThrow(department.getId())).thenReturn(department);
        when(validationService.getLevelByIdOrThrow(level.getId())).thenReturn(level);
        when(studentRepository.save(any(Student.class))).thenReturn(student);
        
        // Act: Call the method under test
        StudentDTO result = studentService.updateStudent(student.getId(), studentDTO);
        
        // Assert: Verify output and interactions
        assertThat(result)
                .as("Returned student should match the mock entity")
                .extracting(StudentDTO::getId, StudentDTO::getFirstName, StudentDTO::getLastName,
                        StudentDTO::getPhoneNumber, StudentDTO::getDateOfBirth, StudentDTO::getGender,
                        StudentDTO::getFacultyId, StudentDTO::getFacultyName,
                        StudentDTO::getDepartmentId, StudentDTO::getDepartmentName,
                        StudentDTO::getLevelId, StudentDTO::getLevelName)
                .containsExactly(student.getId(), student.getFirstName(), student.getLastName(),
                        student.getPhoneNumber(), student.getDateOfBirth(), student.getGender(),
                        faculty.getId(), faculty.getName(),
                        department.getId(), department.getName(),
                        level.getId(), level.getName());
        
        verify(validationService).getStudentByIdOrThrow(student.getId());
        verify(validationService).getDepartmentByIdOrThrow(department.getId());
        verify(validationService).getLevelByIdOrThrow(level.getId());
        verify(studentRepository).save(any(Student.class));
        verifyNoMoreInteractions(validationService, studentRepository);
    }
    
    // Delete ============================================================
    
    /**
     * Unit test for {@link StudentService#deleteStudent(Long)}.
     * <p>
     * <b>Scenario:</b> When deleting a student that exists and has no associations, the service should:
     * <ul>
     *   <li>Fetch the student via {@link ValidationService#getStudentByIdOrThrow(Long)}</li>
     *
     *   <li>Delete the student via {@link StudentRepository#delete(Object)}</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The student is deleted successfully</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> The student is deleted without exceptions.
     */
    @Test
    @DisplayName("Should delete student by ID")
    void shouldDeleteStudentById_whenStudentExistsAndHasNoEnrollments() {
        // Arrange: Prepare mocks and inputs
        when(validationService.getStudentByIdOrThrow(student.getId())).thenReturn(student);
        doNothing().when(studentRepository).delete(any(Student.class));
        
        // Act: Call the method under test
        studentService.deleteStudent(student.getId());
        
        // Assert: Verify interactions
        verify(validationService).getStudentByIdOrThrow(student.getId());
        verify(studentRepository).delete(any(Student.class));
        verifyNoMoreInteractions(validationService, studentRepository);
    }
    
    // ================================================================
    // Negative Test Cases
    // ================================================================
    
    // Get ============================================================
    
    /**
     * Unit test for {@link StudentService#getStudentById(Long)}.
     * <p>
     * <b>Scenario:</b> When a student with the specified ID does not exist, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#getStudentByIdOrThrow(Long)}</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception is thrown with the expected message</li>
     *   <li>No unexpected interactions occur with the repository</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> NotFoundException is thrown.
     */
    @Test
    @DisplayName("Should throw NotFoundException when student ID does not exist")
    void shouldThrowNotFoundException_whenStudentIdDoesNotExist() {
        // Arrange, Act & Assert: Use helper to assert not found behavior
        TestAssertionUtils.assertStudentNotFound(() -> studentService.getStudentById(TestDataFactory.NON_EXISTENT_ID));
    }
    
    // Create ============================================================
    
    /**
     * Unit test for {@link StudentService#createStudent(StudentDTO)}.
     * <p>
     * <b>Scenario:</b> When creating a student with department not set, the service should:
     * <ul>
     *   <li>Throw a {@link BadRequestException}</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception is thrown with the expected message</li>
     *   <li>No unexpected interactions occur with the repository</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> BadRequestException is thrown.
     */
    @Test
    @DisplayName("Should throw BadRequestException when creating student with department not set")
    void shouldThrowBadRequestException_whenCreatingStudentWithDepartmentNotSet() {
        studentDTO.setDepartmentId(null);
        
        assertThatThrownBy(() -> studentService.createStudent(studentDTO))
                .as("Should throw BadRequestException when department is not set")
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Student must be in a department");
    }
    
    /**
     * Unit test for {@link StudentService#createStudent(StudentDTO)}.
     * <p>
     * <b>Scenario:</b> When creating a student with a non-existing department, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#getDepartmentByIdOrThrow(Long)}</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception is thrown with the expected message</li>
     *   <li>No unexpected interactions occur with the repository</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> NotFoundException is thrown.
     */
    @Test
    @DisplayName("Should throw NotFoundException when creating student with non-existing department")
    void shouldThrowNotFoundException_whenCreatingStudentWithNonExistingDepartment() {
        studentDTO.setDepartmentId(TestDataFactory.NON_EXISTENT_ID);
        TestAssertionUtils.assertDepartmentNotFound(() -> studentService.createStudent(studentDTO));
    }
    
    /**
     * Unit test for {@link StudentService#createStudent(StudentDTO)}.
     * <p>
     * <b>Scenario:</b> When creating a student with level not set, the service should:
     * <ul>
     *   <li>Throw a {@link BadRequestException}</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception is thrown with the expected message</li>
     *   <li>No unexpected interactions occur with the repository</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> BadRequestException is thrown.
     */
    @Test
    @DisplayName("Should throw BadRequestException when creating student with level not set")
    void shouldThrowBadRequestException_whenCreatingStudentWithLevelNotSet() {
        studentDTO.setLevelId(null);
        
        assertThatThrownBy(() -> studentService.createStudent(studentDTO))
                .as("Should throw BadRequestException when level is not set")
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Student must have a level");
    }
    
    /**
     * Unit test for {@link StudentService#createStudent(StudentDTO)}.
     * <p>
     * <b>Scenario:</b> When creating a student with a non-existing level, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#getLevelByIdOrThrow(Long)}</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception is thrown with the expected message</li>
     *   <li>No unexpected interactions occur with the repository</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> NotFoundException is thrown.
     */
    @Test
    @DisplayName("Should throw NotFoundException when creating student with non-existing level")
    void shouldThrowNotFoundException_whenCreatingStudentWithNonExistingLevel() {
        studentDTO.setLevelId(TestDataFactory.NON_EXISTENT_ID);
        TestAssertionUtils.assertLevelNotFound(() -> studentService.createStudent(studentDTO));
    }
    
    // Update ============================================================
    
    /**
     * Unit test for {@link StudentService#updateStudent(Long, StudentDTO)}.
     * <p>
     * <b>Scenario:</b> When updating a student with a non-existing ID, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#getStudentByIdOrThrow(Long)}</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception is thrown with the expected message</li>
     *   <li>No unexpected interactions occur with the repository</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> NotFoundException is thrown.
     */
    @Test
    @DisplayName("Should throw NotFoundException when updating student with non-existing ID")
    void shouldThrowNotFoundException_whenUpdatingNonExistingStudent() {
        // Arrange, Act & Assert: Use helper to assert not found behavior
        TestAssertionUtils.assertStudentNotFound(() -> studentService.updateStudent(TestDataFactory.NON_EXISTENT_ID, studentDTO));
    }
    
    /**
     * Unit test for {@link StudentService#updateStudent(Long, StudentDTO)}.
     * <p>
     * <b>Scenario:</b> When updating a student with department not set, the service should:
     * <ul>
     *   <li>Throw a {@link BadRequestException}</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception is thrown with the expected message</li>
     *   <li>No unexpected interactions occur with the repository</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> BadRequestException is thrown.
     */
    @Test
    @DisplayName("Should throw BadRequestException when updating student with department not set")
    void shouldThrowBadRequestException_whenUpdatingStudentWithDepartmentNotSet() {
        studentDTO.setDepartmentId(null);
        when(validationService.getStudentByIdOrThrow(student.getId())).thenReturn(student);
        
        // The service should call validate And Save Student when department is null
        assertThatThrownBy(() -> studentService.updateStudent(student.getId(), studentDTO))
                .as("Should throw BadRequestException when department is not set")
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Student must be in a department");
        
        verify(validationService).getStudentByIdOrThrow(student.getId());
    }
    
    /**
     * Unit test for {@link StudentService#updateStudent(Long, StudentDTO)}.
     * <p>
     * <b>Scenario:</b> When updating a student with a non-existing department, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#getDepartmentByIdOrThrow(Long)}</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception is thrown with the expected message</li>
     *   <li>No unexpected interactions occur with the repository</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> NotFoundException is thrown.
     */
    @Test
    @DisplayName("Should throw NotFoundException when updating student with non-existing department")
    void shouldThrowNotFoundException_whenUpdatingStudentWithNonExistingDepartment() {
        studentDTO.setDepartmentId(TestDataFactory.NON_EXISTENT_ID);
        when(validationService.getStudentByIdOrThrow(student.getId())).thenReturn(student);
        TestAssertionUtils.assertDepartmentNotFound(() -> studentService.updateStudent(student.getId(), studentDTO));
        verify(validationService).getStudentByIdOrThrow(student.getId());
    }
    
    /**
     * Unit test for {@link StudentService#updateStudent(Long, StudentDTO)}.
     * <p>
     * <b>Scenario:</b> When updating a student with level not set, the service should:
     * <ul>
     *   <li>Throw a {@link BadRequestException}</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception is thrown with the expected message</li>
     *   <li>No unexpected interactions occur with the repository</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> BadRequestException is thrown.
     */
    @Test
    @DisplayName("Should throw BadRequestException when updating student with level not set")
    void shouldThrowBadRequestException_whenUpdatingStudentWithLevelNotSet() {
        studentDTO.setLevelId(null);
        when(validationService.getStudentByIdOrThrow(student.getId())).thenReturn(student);
        
        // The service should call validate And Save Student when level is null
        assertThatThrownBy(() -> studentService.updateStudent(student.getId(), studentDTO))
                .as("Should throw BadRequestException when level is not set")
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Student must have a level");
        
        verify(validationService).getStudentByIdOrThrow(student.getId());
    }
    
    /**
     * Unit test for {@link StudentService#updateStudent(Long, StudentDTO)}.
     * <p>
     * <b>Scenario:</b> When updating a student with a non-existing level, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#getLevelByIdOrThrow(Long)}</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception is thrown with the expected message</li>
     *   <li>No unexpected interactions occur with the repository</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> NotFoundException is thrown.
     */
    @Test
    @DisplayName("Should throw NotFoundException when updating student with non-existing level")
    void shouldThrowNotFoundException_whenUpdatingStudentWithNonExistingLevel() {
        studentDTO.setLevelId(TestDataFactory.NON_EXISTENT_ID);
        when(validationService.getStudentByIdOrThrow(student.getId())).thenReturn(student);
        TestAssertionUtils.assertLevelNotFound(() -> studentService.updateStudent(student.getId(), studentDTO));
        verify(validationService).getStudentByIdOrThrow(student.getId());
    }
    
    // Delete ============================================================
    
    /**
     * Unit test for {@link StudentService#deleteStudent(Long)}.
     * <p>
     * <b>Scenario:</b> When deleting a student with a non-existing ID, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#getStudentByIdOrThrow(Long)}</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception is thrown with the expected message</li>
     *   <li>No unexpected interactions occur with the repository</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> NotFoundException is thrown.
     */
    @Test
    @DisplayName("Should throw NotFoundException when deleting student with non-existing ID")
    void shouldThrowNotFoundException_whenDeletingNonExistingStudent() {
        // Arrange, Act & Assert: Use helper to assert not found behavior
        TestAssertionUtils.assertStudentNotFound(() -> studentService.deleteStudent(TestDataFactory.NON_EXISTENT_ID));
    }
    
    /**
     * Unit test for {@link StudentService#deleteStudent(Long)}.
     * <p>
     * <b>Scenario:</b> When deleting a student with associated enrollments, the service should:
     * <ul>
     *   <li>Fetch the student via {@link ValidationService#getStudentByIdOrThrow(Long)}</li>
     * </ul>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception is thrown with the expected message</li>
     *   <li>No unexpected interactions occur with the repository</li>
     * </ul>
     * <p>
     * <b>Expected result:</b> ConflictException is thrown.
     */
    @Test
    @DisplayName("Should throw ConflictException when deleting student with associated enrollments")
    void shouldThrowConflictException_whenDeletingStudentWithEnrollments() {
        // Arrange: Prepare mocks and inputs
        when(validationService.getStudentByIdOrThrow(student.getId())).thenReturn(student);
        Course course = TestDataFactory.buildCourse(department, level);
        Enrollment enrollment = TestDataFactory.buildEnrollment(student, course, 87.6);
        student.setEnrollments(List.of(enrollment));
        
        // Act & Assert: Verify exception is thrown
        assertThatThrownBy(() -> studentService.deleteStudent(student.getId()))
                .as("Should throw ConflictException when Student is associated with any Enrollments")
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Cannot delete student with id " + student.getId() +
                        " because they are enrolled in courses");
        
        // Assert: Verify interactions
        verify(validationService).getStudentByIdOrThrow(student.getId());
        verifyNoInteractions(studentRepository);
        verifyNoMoreInteractions(validationService);
    }
}
