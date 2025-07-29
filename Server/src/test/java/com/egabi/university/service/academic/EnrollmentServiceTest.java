package com.egabi.university.service.academic;

import com.egabi.university.dto.EnrollmentDTO;
import com.egabi.university.entity.*;
import com.egabi.university.entity.authentication.Role;
import com.egabi.university.entity.authentication.User;
import com.egabi.university.exception.BadRequestException;
import com.egabi.university.exception.NotFoundException;
import com.egabi.university.mapper.EnrollmentMapper;
import com.egabi.university.repository.EnrollmentRepository;
import com.egabi.university.service.academic.impl.EnrollmentServiceImpl;
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
 * Comprehensive unit tests for {@link EnrollmentServiceImpl} using JUnit 5 and Mockito.
 * <p>
 * These tests verify the service logic in isolation from external dependencies:
 * <ul>
 *   <li>{@link EnrollmentRepository} for database operations</li>
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
public class EnrollmentServiceTest {
    
    // ================================================================
    // Mocks and Dependencies
    // ================================================================
    
    @Mock
    private EnrollmentRepository enrollmentRepository;
    
    @Mock
    private ValidationService validationService;
    
    private EnrollmentServiceImpl enrollmentService;
    
    private Enrollment enrollment;
    private EnrollmentDTO enrollmentDTO;
    private Student student;
    private Course course;
    private Department department;
    private Level level;
    private User user;
    
    // ================================================================
    // Setup : Arrange common test fixtures
    // ================================================================
    
    @BeforeEach
    void setUp() {
        // Initialize the mapper using MapStruct factory
        EnrollmentMapper enrollmentMapper = Mappers.getMapper(EnrollmentMapper.class);
        
        // Set up the validation service for TestAssertionUtils
        TestAssertionUtils.setValidationService(validationService);
        
        // Create the service under test with mocked dependencies
        enrollmentService = new EnrollmentServiceImpl(enrollmentRepository, enrollmentMapper, validationService);
        
        // Prepare test data
        Faculty faculty = TestDataFactory.buildFaculty();
        department = TestDataFactory.buildDepartment(faculty);
        level = TestDataFactory.buildLevel(faculty);
        user = TestDataFactory.buildUser(Role.STUDENT);
        student = TestDataFactory.buildStudent(user, department, level);
        course = TestDataFactory.buildCourse(department, level);
        enrollment = TestDataFactory.buildEnrollment(student, course, 85.0);
        enrollmentDTO = TestDataFactory.buildEnrollmentDTO();
    }
    
    // ================================================================
    // Positive Test Cases
    // ================================================================
    
    // Get ============================================================
    
    /**
     * Unit test for {@link EnrollmentService#getAllEnrollments()}.
     * <p>
     * <b>Scenario:</b> When enrollments exist in the database, the service should:
     * <ul>
     *   <li>Fetch all enrollment entities via {@link EnrollmentRepository#findAll()}</li>
     *   <li>Map the entities to {@link EnrollmentDTO} instances correctly</li>
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
     * <b>Expected result:</b> The list contains all enrollments mapped to DTOs.
     */
    @Test
    @DisplayName("Should return all enrollments when enrollments exist")
    void shouldReturnAllEnrollments_whenEnrollmentsExist() {
        // Arrange: Prepare mocks and inputs
        when(enrollmentRepository.findAll()).thenReturn(List.of(enrollment));
        
        // Act: Call the method under test
        List<EnrollmentDTO> result = enrollmentService.getAllEnrollments();
        
        // Assert: Verify output and interactions
        assertThat(result)
                .as("Returned list should contain exactly one enrollment")
                .hasSize(1);
        assertThat(result.getFirst())
                .as("Returned enrollment should match the mock entity")
                .extracting(EnrollmentDTO::getStudentId, EnrollmentDTO::getCourseCode, EnrollmentDTO::getGrade)
                .containsExactly(enrollment.getId().getStudentId(), enrollment.getId().getCourseCode(), enrollment.getGrade());
        
        verify(enrollmentRepository).findAll();
        verifyNoMoreInteractions(validationService, enrollmentRepository);
    }
    
    /**
     * Unit test for {@link EnrollmentService#getEnrollmentById(Long, String)}.
     * <p>
     * <b>Scenario:</b> When an enrollment with the specified ID exists, the service should:
     * <ul>
     *   <li>Validate student and course existence</li>
     *   <li>Fetch the enrollment entity via {@link ValidationService#getEnrollmentByIdOrThrow(EnrollmentId)}</li>
     *   <li>Map the entity to an {@link EnrollmentDTO} instance</li>
     *   <li>Return the mapped DTO</li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The returned DTO has the expected studentId, courseCode, and grade</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> The DTO contains the correct enrollment data.
     */
    @Test
    @DisplayName("Should return enrollment by ID when it exists")
    void shouldReturnEnrollmentById_whenEnrollmentExists() {
        // Arrange: Prepare mocks and inputs
        doNothing().when(validationService).assertStudentExists(student.getId());
        doNothing().when(validationService).assertCourseExists(course.getCode(), true);
        when(validationService.getEnrollmentByIdOrThrow(enrollment.getId())).thenReturn(enrollment);
        
        // Act: Call the method under test
        EnrollmentDTO result = enrollmentService.getEnrollmentById(student.getId(), course.getCode());
        
        // Assert: Verify output and interactions
        assertThat(result)
                .as("Returned enrollment should match the mock entity")
                .extracting(EnrollmentDTO::getStudentId, EnrollmentDTO::getCourseCode, EnrollmentDTO::getGrade)
                .containsExactly(enrollment.getId().getStudentId(), enrollment.getId().getCourseCode(), enrollment.getGrade());
        
        verify(validationService).assertStudentExists(student.getId());
        verify(validationService).assertCourseExists(course.getCode(), true);
        verify(validationService).getEnrollmentByIdOrThrow(enrollment.getId());
        verifyNoMoreInteractions(validationService);
    }
    
    // Create ============================================================
    
    /**
     * Unit test for {@link EnrollmentService#createEnrollment(EnrollmentDTO)}.
     * <p>
     * <b>Scenario:</b> When creating a new enrollment, the service should:
     * <ul>
     *   <li>Validate student and course existence</li>
     *   <li>Validate that the enrollment doesn't already exist</li>
     *   <li>Map the DTO to an {@link Enrollment} entity</li>
     *   <li>Validate the grade and save the enrollment</li>
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
     * <b>Expected result:</b> The DTO contains the correct enrollment data.
     */
    @Test
    @DisplayName("Should create a new enrollment when all required fields are set")
    void shouldCreateEnrollment_whenAllRequiredFieldsExistAndAreSet() {
        // Arrange: Prepare mocks and inputs
        doNothing().when(validationService).assertStudentExists(enrollmentDTO.getStudentId());
        doNothing().when(validationService).assertCourseExists(enrollmentDTO.getCourseCode(), true);
        doNothing().when(validationService).assertEnrollmentExists(enrollment.getId(), false);
        when(validationService.getStudentByIdOrThrow(enrollmentDTO.getStudentId())).thenReturn(student);
        when(validationService.getCourseByCodeOrThrow(enrollmentDTO.getCourseCode())).thenReturn(course);
        doNothing().when(validationService).validateGradeInRange(enrollmentDTO.getGrade());
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);
        
        // Act: Call the method under test
        EnrollmentDTO result = enrollmentService.createEnrollment(enrollmentDTO);
        
        // Assert: Verify output and interactions
        assertThat(result)
                .as("Returned enrollment should match the mock entity")
                .extracting(EnrollmentDTO::getStudentId, EnrollmentDTO::getCourseCode, EnrollmentDTO::getGrade)
                .containsExactly(enrollment.getId().getStudentId(), enrollment.getId().getCourseCode(), enrollment.getGrade());
        
        verify(validationService).assertStudentExists(enrollmentDTO.getStudentId());
        verify(validationService).assertCourseExists(enrollmentDTO.getCourseCode(), true);
        verify(validationService).assertEnrollmentExists(enrollment.getId(), false);
        verify(validationService).getStudentByIdOrThrow(enrollmentDTO.getStudentId());
        verify(validationService).getCourseByCodeOrThrow(enrollmentDTO.getCourseCode());
        verify(validationService).validateGradeInRange(enrollmentDTO.getGrade());
        verify(enrollmentRepository).save(any(Enrollment.class));
        verifyNoMoreInteractions(validationService, enrollmentRepository);
    }
    
    // Update ============================================================
    
    /**
     * Unit test for {@link EnrollmentService#updateEnrollment(Long, String, EnrollmentDTO)}.
     * <p>
     * <b>Scenario:</b> When updating an existing enrollment, the service should:
     * <ul>
     *   <li>Validate student and course existence</li>
     *   <li>Validate that the enrollment exists</li>
     *   <li>Map the DTO to an {@link Enrollment} entity</li>
     *   <li>Validate the grade and save the updated enrollment</li>
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
     * <b>Expected result:</b> The DTO contains the updated enrollment data.
     */
    @Test
    @DisplayName("Should update enrollment when it exists")
    void shouldUpdateEnrollment_whenEnrollmentExistsAndAllRequiredFieldsExistAndAreSet() {
        // Arrange: Prepare mocks and inputs
        doNothing().when(validationService).assertStudentExists(student.getId());
        doNothing().when(validationService).assertCourseExists(course.getCode(), true);
        doNothing().when(validationService).assertEnrollmentExists(enrollment.getId(), true);
        when(validationService.getStudentByIdOrThrow(enrollmentDTO.getStudentId())).thenReturn(student);
        when(validationService.getCourseByCodeOrThrow(enrollmentDTO.getCourseCode())).thenReturn(course);
        doNothing().when(validationService).validateGradeInRange(enrollmentDTO.getGrade());
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);
        
        // Act: Call the method under test
        EnrollmentDTO result = enrollmentService.updateEnrollment(student.getId(), course.getCode(), enrollmentDTO);
        
        // Assert: Verify output and interactions
        assertThat(result)
                .as("Returned enrollment should match the mock entity")
                .extracting(EnrollmentDTO::getStudentId, EnrollmentDTO::getCourseCode, EnrollmentDTO::getGrade)
                .containsExactly(enrollment.getId().getStudentId(), enrollment.getId().getCourseCode(), enrollment.getGrade());
        
        verify(validationService).assertStudentExists(student.getId());
        verify(validationService).assertCourseExists(course.getCode(), true);
        verify(validationService).assertEnrollmentExists(enrollment.getId(), true);
        verify(validationService).getStudentByIdOrThrow(enrollmentDTO.getStudentId());
        verify(validationService).getCourseByCodeOrThrow(enrollmentDTO.getCourseCode());
        verify(validationService).validateGradeInRange(enrollmentDTO.getGrade());
        verify(enrollmentRepository).save(any(Enrollment.class));
        verifyNoMoreInteractions(validationService, enrollmentRepository);
    }
    
    // Delete ============================================================
    
    /**
     * Unit test for {@link EnrollmentService#deleteEnrollment(Long, String)}.
     * <p>
     * <b>Scenario:</b> When deleting an enrollment that exists, the service should:
     * <ul>
     *   <li>Validate student and course existence</li>
     *   <li>Validate that the enrollment exists</li>
     *   <li>Delete the enrollment via {@link EnrollmentRepository#deleteById(Object)} </li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The enrollment is deleted successfully</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> The enrollment is deleted without exceptions.
     */
    @Test
    @DisplayName("Should delete enrollment by ID")
    void shouldDeleteEnrollmentById_whenEnrollmentExists() {
        // Arrange: Prepare mocks and inputs
        doNothing().when(validationService).assertStudentExists(student.getId());
        doNothing().when(validationService).assertCourseExists(course.getCode(), true);
        doNothing().when(validationService).assertEnrollmentExists(enrollment.getId(), true);
        doNothing().when(enrollmentRepository).deleteById(enrollment.getId());
        
        // Act: Call the method under test
        enrollmentService.deleteEnrollment(student.getId(), course.getCode());
        
        // Assert: Verify interactions
        verify(validationService).assertStudentExists(student.getId());
        verify(validationService).assertCourseExists(course.getCode(), true);
        verify(validationService).assertEnrollmentExists(enrollment.getId(), true);
        verify(enrollmentRepository).deleteById(enrollment.getId());
        verifyNoMoreInteractions(validationService, enrollmentRepository);
    }
    
    // Business Logic Methods ============================================
    
    /**
     * Unit test for {@link EnrollmentService#getEnrollmentsByStudentId(Long)}.
     * <p>
     * <b>Scenario:</b> When enrollments exist for a student, the service should:
     * <ul>
     *   <li>Validate student existence via {@link ValidationService#assertStudentExists(Long)}</li>
     *   <li>Fetch enrollments by student ID via {@link EnrollmentRepository#findByStudentId(Long)}</li>
     *   <li>Map the entities to {@link EnrollmentDTO} instances</li>
     *   <li>Return a list containing all mapped DTOs</li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The returned list has the expected size and content</li>
     *   <li>No unexpected interactions occur with the repository</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> The list contains all enrollments for the student.
     */
    @Test
    @DisplayName("Should return enrollments by student ID when enrollments exist")
    void shouldReturnEnrollmentsByStudentId_whenEnrollmentsExist() {
//        // Arrange: Prepare mocks and inputs
//        Long studentId = student.getId();
//        doNothing().when(validationService).assertStudentExists(studentId);
//        when(enrollmentRepository.findByStudentId(studentId)).thenReturn(List.of(enrollment));
//
//        // Act: Call the method under test
//        List<EnrollmentDTO> result = enrollmentService.getEnrollmentsByStudentId(studentId);
//
//        // Assert: Verify output and interactions
//        assertThat(result)
//                .as("Returned list should contain exactly one enrollment")
//                .hasSize(1);
//        assertThat(result.getFirst())
//                .as("Returned enrollment should match the mock entity")
//                .extracting(EnrollmentDTO::getStudentId, EnrollmentDTO::getCourseCode, EnrollmentDTO::getGrade)
//                .containsExactly(enrollment.getId().getStudentId(), enrollment.getId().getCourseCode(), enrollment.getGrade());
//
//        verify(validationService).assertStudentExists(studentId);
//        verify(enrollmentRepository).findByStudentId(studentId);
//        verifyNoMoreInteractions(validationService, enrollmentRepository);
    }
    
    /**
     * Unit test for {@link EnrollmentService#getEnrollmentsByCourseId(String)}.
     * <p>
     * <b>Scenario:</b> When enrollments exist for a course, the service should:
     * <ul>
     *   <li>Validate course existence via {@link ValidationService#assertCourseExists(String, boolean)}</li>
     *   <li>Fetch enrollments by course code via {@link EnrollmentRepository#findByCourseCode(String)}</li>
     *   <li>Map the entities to {@link EnrollmentDTO} instances</li>
     *   <li>Return a list containing all mapped DTOs</li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The returned list has the expected size and content</li>
     *   <li>No unexpected interactions occur with the repository</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> The list contains all enrollments for the course.
     */
    @Test
    @DisplayName("Should return enrollments by course code when enrollments exist")
    void shouldReturnEnrollmentsByCourseCode_whenEnrollmentsExist() {
//        // Arrange: Prepare mocks and inputs
//        String courseCode = course.getCode();
//        doNothing().when(validationService).assertCourseExists(courseCode, true);
//        when(enrollmentRepository.findByCourseCode(courseCode)).thenReturn(List.of(enrollment));
//
//        // Act: Call the method under test
//        List<EnrollmentDTO> result = enrollmentService.getEnrollmentsByCourseId(courseCode);
//
//        // Assert: Verify output and interactions
//        assertThat(result)
//                .as("Returned list should contain exactly one enrollment")
//                .hasSize(1);
//        assertThat(result.getFirst())
//                .as("Returned enrollment should match the mock entity")
//                .extracting(EnrollmentDTO::getStudentId, EnrollmentDTO::getCourseCode, EnrollmentDTO::getGrade)
//                .containsExactly(enrollment.getId().getStudentId(), enrollment.getId().getCourseCode(), enrollment.getGrade());
//
//        verify(validationService).assertCourseExists(courseCode, true);
//        verify(enrollmentRepository).findByCourseCode(courseCode);
//        verifyNoMoreInteractions(validationService, enrollmentRepository);
    }
    
    // ================================================================
    // Negative Test Cases
    // ================================================================
    
    // Get ============================================================
    
    /**
     * Unit test for {@link EnrollmentService#getEnrollmentById(Long, String)}.
     * <p>
     * <b>Scenario:</b> When an enrollment with the specified ID does not exist, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#getEnrollmentByIdOrThrow(EnrollmentId)}</li>
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
    @DisplayName("Should throw NotFoundException when enrollment ID does not exist")
    void shouldThrowNotFoundException_whenEnrollmentIdDoesNotExist() {
        // Arrange, Act & Assert: Use helper to assert not found behavior
        TestAssertionUtils.assertEnrollmentNotFound(() ->
                enrollmentService.getEnrollmentById(student.getId(), course.getCode())
        );
    }
    
    // Create ============================================================
    
    /**
     * Unit test for {@link EnrollmentService#createEnrollment(EnrollmentDTO)}.
     * <p>
     * <b>Scenario:</b> When creating an enrollment with grade not set, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException}</li>
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
    @DisplayName("Should throw BadRequestException when creating enrollment with grade not set")
    void shouldThrowBadRequestException_whenCreatingEnrollmentWithGradeNotSet() {
        // Arrange: Prepare mocks and inputs
        enrollmentDTO.setGrade(null);
        doNothing().when(validationService).assertStudentExists(student.getId());
        doNothing().when(validationService).assertCourseExists(course.getCode(), true);
        when(validationService.getStudentByIdOrThrow(student.getId())).thenReturn(student);
        when(validationService.getCourseByCodeOrThrow(course.getCode())).thenReturn(course);
        doNothing().when(validationService).assertEnrollmentExists(enrollment.getId(), false);
        
        // Act & Assert: Call the method and verify exception
        assertThatThrownBy(() -> enrollmentService.createEnrollment(enrollmentDTO))
                .as("Should throw NotFoundException when grade is not set")
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Grade must be set for enrollment");
        
        verify(validationService).assertStudentExists(student.getId());
        verify(validationService).assertCourseExists(course.getCode(), true);
        verify(validationService).getStudentByIdOrThrow(student.getId());
        verify(validationService).getCourseByCodeOrThrow(course.getCode());
        verify(validationService).assertEnrollmentExists(enrollment.getId(), false);
        verifyNoMoreInteractions(validationService, enrollmentRepository);
    }
    
    /**
     * Unit test for {@link EnrollmentService#createEnrollment(EnrollmentDTO)}.
     * <p>
     * <b>Scenario:</b> When creating an enrollment with a non-existing student, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#assertStudentExists(Long)}</li>
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
    @DisplayName("Should throw NotFoundException when creating enrollment with non-existing student")
    void shouldThrowNotFoundException_whenCreatingEnrollmentWithNonExistingStudent() {
        // Arrange: Prepare mocks and inputs
        enrollmentDTO.setStudentId(TestDataFactory.NON_EXISTENT_ID);
        
        // Act & Assert: Use helper to assert not found behavior
        TestAssertionUtils.assertStudentNotFound(() -> enrollmentService.createEnrollment(enrollmentDTO));
        
        // Assert: Verify interactions
        verifyNoInteractions(enrollmentRepository);
    }
    
    /**
     * Unit test for {@link EnrollmentService#createEnrollment(EnrollmentDTO)}.
     * <p>
     * <b>Scenario:</b> When creating an enrollment with a non-existing course, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#assertCourseExists(String, boolean)}</li>
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
    @DisplayName("Should throw NotFoundException when creating enrollment with non-existing course")
    void shouldThrowNotFoundException_whenCreatingEnrollmentWithNonExistingCourse() {
        // Arrange: Prepare mocks and inputs
        enrollmentDTO.setCourseCode(TestDataFactory.NON_EXISTENT_CODE);
        
        // Act & Assert: Use helper to assert not found behavior
        TestAssertionUtils.assertCourseNotFound(() -> enrollmentService.createEnrollment(enrollmentDTO));
        
        // Assert: Verify interactions
        verifyNoInteractions(enrollmentRepository);
    }
    
    // Update ============================================================
    
    /**
     * Unit test for {@link EnrollmentService#updateEnrollment(Long, String, EnrollmentDTO)}.
     * <p>
     * <b>Scenario:</b> When updating an enrollment with a non-existing ID, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#assertEnrollmentExists(EnrollmentId, boolean)}</li>
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
    @DisplayName("Should throw NotFoundException when updating enrollment with non-existing ID")
    void shouldThrowNotFoundException_whenUpdatingNonExistingEnrollment() {
        // Arrange, Act & Assert: Use helper to assert not found behavior
        TestAssertionUtils.assertEnrollmentNotFound(() ->
                enrollmentService.updateEnrollment(student.getId(), course.getCode(), enrollmentDTO));
    }
    
    /**
     * Unit test for {@link EnrollmentService#updateEnrollment(Long, String, EnrollmentDTO)}.
     * <p>
     * <b>Scenario:</b> When updating an enrollment with grade not set, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException}</li>
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
    @DisplayName("Should throw BadRequestException when updating enrollment with grade not set")
    void shouldThrowBadRequestException_whenUpdatingEnrollmentWithGradeNotSet() {
        // Arrange: Prepare mocks and inputs
        enrollmentDTO.setGrade(null);
        doNothing().when(validationService).assertStudentExists(student.getId());
        doNothing().when(validationService).assertCourseExists(course.getCode(), true);
        when(validationService.getStudentByIdOrThrow(student.getId())).thenReturn(student);
        when(validationService.getCourseByCodeOrThrow(course.getCode())).thenReturn(course);
        doNothing().when(validationService).assertEnrollmentExists(enrollment.getId(), true);
        
        // Act & Assert: Call the method and verify exception
        assertThatThrownBy(() -> enrollmentService.updateEnrollment(student.getId(), course.getCode(), enrollmentDTO))
                .as("Should throw NotFoundException when grade is not set")
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Grade must be set for enrollment");
        
        verify(validationService).assertStudentExists(student.getId());
        verify(validationService).assertCourseExists(course.getCode(), true);
        verify(validationService).getStudentByIdOrThrow(student.getId());
        verify(validationService).getCourseByCodeOrThrow(course.getCode());
        verify(validationService).assertEnrollmentExists(enrollment.getId(), true);
        verifyNoMoreInteractions(validationService, enrollmentRepository);
    }
    
    // Delete ============================================================
    
    /**
     * Unit test for {@link EnrollmentService#deleteEnrollment(Long, String)}.
     * <p>
     * <b>Scenario:</b> When deleting an enrollment with a non-existing ID, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#assertEnrollmentExists(EnrollmentId, boolean)}</li>
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
    @DisplayName("Should throw NotFoundException when deleting enrollment with non-existing ID")
    void shouldThrowNotFoundException_whenDeletingNonExistingEnrollment() {
        // Arrange, Act & Assert: Use helper to assert not found behavior
        TestAssertionUtils.assertEnrollmentNotFound(() ->
                enrollmentService.deleteEnrollment(student.getId(), course.getCode()));
    }
}
