package com.egabi.university.service.academic;

import com.egabi.university.dto.CourseDTO;
import com.egabi.university.entity.*;
import com.egabi.university.entity.authentication.Role;
import com.egabi.university.entity.authentication.User;
import com.egabi.university.exception.BadRequestException;
import com.egabi.university.exception.ConflictException;
import com.egabi.university.exception.NotFoundException;
import com.egabi.university.mapper.CourseMapper;
import com.egabi.university.repository.CourseRepository;
import com.egabi.university.service.academic.impl.CourseServiceImpl;
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
 * Comprehensive unit tests for {@link CourseServiceImpl} using JUnit 5 and Mockito.
 * <p>
 * These tests verify the service logic in isolation from external dependencies:
 * <ul>
 *   <li>{@link CourseRepository} for database operations</li>
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
public class CourseServiceTest {
    
    // ================================================================
    // Mocks and Dependencies
    // ================================================================
    
    @Mock
    private CourseRepository courseRepository;
    
    @Mock
    private ValidationService validationService;
    
    private CourseServiceImpl courseService;
    
    private Course course;
    private CourseDTO courseDTO;
    private Department department;
    private Level level;
    
    // ================================================================
    // Setup : Arrange common test fixtures
    // ================================================================
    
    @BeforeEach
    void setUp() {
        // Initialize the mapper using MapStruct factory
        CourseMapper courseMapper = Mappers.getMapper(CourseMapper.class);
        
        // Set up the validation service for TestAssertionUtils
        TestAssertionUtils.setValidationService(validationService);
        
        // Create the service under test with mocked dependencies
        courseService = new CourseServiceImpl(courseRepository, courseMapper, validationService);
        
        // Prepare test data
        Faculty faculty = TestDataFactory.buildFaculty();
        department = TestDataFactory.buildDepartment(faculty);
        level = TestDataFactory.buildLevel(faculty);
        course = TestDataFactory.buildCourse(department, level);
        courseDTO = TestDataFactory.buildCourseDTO();
    }
    
    // ================================================================
    // Positive Test Cases
    // ================================================================
    
    // Get ============================================================
    
    /**
     * Unit test for {@link CourseService#getAllCourses()}.
     * <p>
     * <b>Scenario:</b> When courses exist in the database, the service should:
     * <ul>
     *   <li>Fetch all course entities via {@link CourseRepository#findAll()}</li>
     *   <li>Map the entities to {@link CourseDTO} instances correctly</li>
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
     * <b>Expected result:</b> The list contains all courses mapped to DTOs.
     */
    @Test
    @DisplayName("Should return all courses when courses exist")
    void shouldReturnAllCourses_whenCoursesExist() {
        // Arrange: Prepare mocks and inputs
        when(courseRepository.findAll()).thenReturn(List.of(course));
        
        // Act: Call the method under test
        List<CourseDTO> result = courseService.getAllCourses();
        
        // Assert: Verify output and interactions
        assertThat(result)
                .as("Returned list should contain exactly one course")
                .hasSize(1);
        assertThat(result.getFirst())
                .as("Returned course should match the mock entity")
                .extracting(CourseDTO::getCode, CourseDTO::getName, CourseDTO::getCredits,
                        CourseDTO::getDepartmentId, CourseDTO::getLevelId)
                .containsExactly(course.getCode(), course.getName(), course.getCredits(),
                        department.getId(), level.getId());
        
        verify(courseRepository).findAll();
        verifyNoMoreInteractions(validationService, courseRepository);
    }
    
    /**
     * Unit test for {@link CourseService#getCourseByCode(String)}.
     * <p>
     * <b>Scenario:</b> When a course with the specified code exists, the service should:
     * <ul>
     *   <li>Fetch the course entity via {@link ValidationService#getCourseByCodeOrThrow(String)}</li>
     *   <li>Map the entity to a {@link CourseDTO} instance</li>
     *   <li>Return the mapped DTO</li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The returned DTO has the expected code, name, and credits</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> The DTO contains the correct course data.
     */
    @Test
    @DisplayName("Should return course by code when it exists")
    void shouldReturnCourseByCode_whenCourseExists() {
        // Arrange: Prepare mocks and inputs
        when(validationService.getCourseByCodeOrThrow(course.getCode())).thenReturn(course);
        
        // Act: Call the method under test
        CourseDTO result = courseService.getCourseByCode(course.getCode());
        
        // Assert: Verify output and interactions
        assertThat(result)
                .as("Returned course should match the mock entity")
                .extracting(CourseDTO::getCode, CourseDTO::getName, CourseDTO::getCredits,
                        CourseDTO::getDepartmentId, CourseDTO::getLevelId)
                .containsExactly(course.getCode(), course.getName(), course.getCredits(),
                        department.getId(), level.getId());
        
        verify(validationService).getCourseByCodeOrThrow(course.getCode());
        verifyNoMoreInteractions(validationService);
    }
    
    // Create ============================================================
    
    /**
     * Unit test for {@link CourseService#createCourse(CourseDTO)}.
     * <p>
     * <b>Scenario:</b> When creating a new course, the service should:
     * <ul>
     *   <li>Validate that the course code doesn't exist via {@link ValidationService#assertCourseExists(String, boolean)}</li>
     *   <li>Map the DTO to a {@link Course} entity</li>
     *   <li>Validate the department and level</li>
     *   <li>Save the entity via {@link CourseRepository#save(Object)}</li>
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
     * <b>Expected result:</b> The DTO contains the correct course data.
     */
    @Test
    @DisplayName("Should create a new course when all required fields are set")
    void shouldCreateCourse_whenAllRequiredFieldsExistAndAreSet() {
        // Arrange: Prepare mocks and inputs
        doNothing().when(validationService).assertCourseExists(courseDTO.getCode(), false);
        when(validationService.getDepartmentByIdOrThrow(department.getId())).thenReturn(department);
        when(validationService.getLevelByIdOrThrow(level.getId())).thenReturn(level);
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        
        // Act: Call the method under test
        CourseDTO result = courseService.createCourse(courseDTO);
        
        // Assert: Verify output and interactions
        assertThat(result)
                .as("Returned course should match the mock entity")
                .extracting(CourseDTO::getCode, CourseDTO::getName, CourseDTO::getCredits,
                        CourseDTO::getDepartmentId, CourseDTO::getLevelId)
                .containsExactly(course.getCode(), course.getName(), course.getCredits(),
                        department.getId(), level.getId());
        
        verify(validationService).assertCourseExists(courseDTO.getCode(), false);
        verify(validationService).getDepartmentByIdOrThrow(department.getId());
        verify(validationService).getLevelByIdOrThrow(level.getId());
        verify(courseRepository).save(any(Course.class));
        verifyNoMoreInteractions(validationService, courseRepository);
    }
    
    // Update ============================================================
    
    /**
     * Unit test for {@link CourseService#updateCourse(String, CourseDTO)}.
     * <p>
     * <b>Scenario:</b> When updating an existing course, the service should:
     * <ul>
     *   <li>Validate that the course exists via {@link ValidationService#assertCourseExists(String, boolean)}</li>
     *   <li>Map the DTO to a {@link Course} entity</li>
     *   <li>Validate the department and level</li>
     *   <li>Save the updated entity via {@link CourseRepository#save(Object)}</li>
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
     * <b>Expected result:</b> The DTO contains the updated course data.
     */
    @Test
    @DisplayName("Should update course when it exists")
    void shouldUpdateCourse_whenCourseExistsAndAllRequiredFieldsExistAndAreSet() {
        // Arrange: Prepare mocks and inputs
        when(validationService.getCourseByCodeOrThrow(course.getCode())).thenReturn(course);
        when(validationService.getDepartmentByIdOrThrow(department.getId())).thenReturn(department);
        when(validationService.getLevelByIdOrThrow(level.getId())).thenReturn(level);
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        
        // Act: Call the method under test
        CourseDTO result = courseService.updateCourse(course.getCode(), courseDTO);
        
        // Assert: Verify output and interactions
        assertThat(result)
                .as("Returned course should match the mock entity")
                .extracting(CourseDTO::getCode, CourseDTO::getName, CourseDTO::getCredits,
                        CourseDTO::getDepartmentId, CourseDTO::getLevelId)
                .containsExactly(course.getCode(), course.getName(), course.getCredits(),
                        department.getId(), level.getId());
        
        // Assert : Verify interactions
        verify(validationService).getCourseByCodeOrThrow(course.getCode());
        verify(validationService).getDepartmentByIdOrThrow(department.getId());
        verify(validationService).getLevelByIdOrThrow(level.getId());
        verify(courseRepository).save(any(Course.class));
        verifyNoMoreInteractions(validationService, courseRepository);
    }
    
    // Delete ============================================================
    
    /**
     * Unit test for {@link CourseService#deleteCourse(String)}.
     * <p>
     * <b>Scenario:</b> When deleting a course that exists and has no associations, the service should:
     * <ul>
     *   <li>Fetch the course via {@link ValidationService#getCourseByCodeOrThrow(String)}</li>
     *   <li>Delete the course via {@link CourseRepository#delete(Object)}</li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The course is deleted successfully</li>
     *   <li>No unexpected interactions occur with the repository or validation service</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> The course is deleted without exceptions.
     */
    @Test
    @DisplayName("Should delete course by code")
    void shouldDeleteCourseByCode_whenCourseExistsAndHasNoEnrollments() {
        // Arrange: Prepare mocks and inputs
        when(validationService.getCourseByCodeOrThrow(course.getCode())).thenReturn(course);
        doNothing().when(courseRepository).delete(course);
        
        // Act: Call the method under test
        courseService.deleteCourse(course.getCode());
        
        // Assert: Verify interactions
        verify(validationService).getCourseByCodeOrThrow(course.getCode());
        verify(courseRepository).delete(course);
        verifyNoMoreInteractions(validationService, courseRepository);
    }
    
    // Business Logic Methods ============================================
    
    /**
     * Unit test for {@link CourseService#getCoursesByDepartmentId(Long)}.
     * <p>
     * <b>Scenario:</b> When courses exist for a department, the service should:
     * <ul>
     *   <li>Validate department existence via {@link ValidationService#assertDepartmentExists(Long)}</li>
     *   <li>Fetch courses by department ID via {@link CourseRepository#findAllByDepartmentId(Long)}</li>
     *   <li>Map the entities to {@link CourseDTO} instances</li>
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
     * <b>Expected result:</b> The list contains all courses for the department.
     */
    @Test
    @DisplayName("Should return courses by department ID when courses exist")
    void shouldReturnCoursesByDepartmentId_whenCoursesExist() {
        // Arrange: Prepare mocks and inputs
        Long departmentId = department.getId();
        doNothing().when(validationService).assertDepartmentExists(departmentId);
        when(courseRepository.findAllByDepartmentId(departmentId)).thenReturn(List.of(course));
        
        // Act: Call the method under test
        List<CourseDTO> result = courseService.getCoursesByDepartmentId(departmentId);
        
        // Assert: Verify output and interactions
        assertThat(result)
                .as("Returned list should contain exactly one course")
                .hasSize(1);
        assertThat(result.getFirst())
                .as("Returned course should match the mock entity")
                .extracting(CourseDTO::getCode, CourseDTO::getName, CourseDTO::getCredits,
                        CourseDTO::getDepartmentId, CourseDTO::getLevelId)
                .containsExactly(course.getCode(), course.getName(), course.getCredits(),
                        department.getId(), level.getId());
        
        verify(validationService).assertDepartmentExists(departmentId);
        verify(courseRepository).findAllByDepartmentId(departmentId);
        verifyNoMoreInteractions(validationService, courseRepository);
    }
    
    /**
     * Unit test for {@link CourseService#countCoursesByDepartmentId(Long)}.
     * <p>
     * <b>Scenario:</b> When counting courses for a department, the service should:
     * <ul>
     *   <li>Validate department existence via {@link ValidationService#assertDepartmentExists(Long)}</li>
     *   <li>Count courses by department ID via {@link CourseRepository#countAllByDepartmentId(Long)}</li>
     *   <li>Return the count</li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The returned count is correct</li>
     *   <li>No unexpected interactions occur with the repository</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> The count matches the expected value.
     */
    @Test
    @DisplayName("Should count courses by department ID")
    void shouldCountCoursesByDepartmentId() {
        // Arrange: Prepare mocks and inputs
        Long departmentId = department.getId();
        Long expectedCount = 5L;
        doNothing().when(validationService).assertDepartmentExists(departmentId);
        when(courseRepository.countAllByDepartmentId(departmentId)).thenReturn(expectedCount);
        
        // Act: Call the method under test
        Long result = courseService.countCoursesByDepartmentId(departmentId);
        
        // Assert: Verify output and interactions
        assertThat(result)
                .as("Returned count should match the expected value")
                .isEqualTo(expectedCount);
        
        verify(validationService).assertDepartmentExists(departmentId);
        verify(courseRepository).countAllByDepartmentId(departmentId);
        verifyNoMoreInteractions(validationService, courseRepository);
    }
    
    // ================================================================
    // Negative Test Cases
    // ================================================================
    
    // Get ============================================================
    
    /**
     * Unit test for {@link CourseService#getCourseByCode(String)}.
     * <p>
     * <b>Scenario:</b> When a course with the specified code does not exist, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#getCourseByCodeOrThrow(String)}</li>
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
    @DisplayName("Should throw NotFoundException when course code does not exist")
    void shouldThrowNotFoundException_whenCourseCodeDoesNotExist() {
        // Arrange, Act & Assert: Use helper to assert not found behavior
        TestAssertionUtils.assertCourseNotFound(() -> courseService.getCourseByCode(TestDataFactory.NON_EXISTENT_CODE));
    }
    
    // Create ============================================================
    
    /**
     * Unit test for {@link CourseService#createCourse(CourseDTO)}.
     * <p>
     * <b>Scenario:</b> When creating a course with department not set, the service should:
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
    @DisplayName("Should throw BadRequestException when creating course with department not set")
    void shouldThrowBadRequestException_whenCreatingCourseWithDepartmentNotSet() {
        // Arrange: Prepare mocks and inputs
        courseDTO.setDepartmentId(null);
        
        // Act & Assert: Call the method and verify exception
        assertThatThrownBy(() -> courseService.createCourse(courseDTO))
                .as("Should throw NotFoundException when department is not set")
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Course must have a department");
    }
    
    /**
     * Unit test for {@link CourseService#createCourse(CourseDTO)}.
     * <p>
     * <b>Scenario:</b> When creating a course with a non-existing department, the service should:
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
    @DisplayName("Should throw NotFoundException when creating course with non-existing department")
    void shouldThrowNotFoundException_whenCreatingCourseWithNonExistingDepartment() {
        // Arrange: Prepare mocks and inputs
        courseDTO.setDepartmentId(TestDataFactory.NON_EXISTENT_ID);
        
        // Act & Assert: Use helper to assert not found behavior
        TestAssertionUtils.assertDepartmentNotFound(() -> courseService.createCourse(courseDTO));
    }
    
    /**
     * Unit test for {@link CourseService#createCourse(CourseDTO)}.
     * <p>
     * <b>Scenario:</b> When creating a course with level not set, the service should:
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
    @DisplayName("Should throw BadRequestException when creating course with level not set")
    void shouldThrowBadRequestException_whenCreatingCourseWithLevelNotSet() {
        // Arrange: Prepare mocks and inputs
        courseDTO.setLevelId(null);
        
        // Act & Assert: Call the method and verify exception
        assertThatThrownBy(() -> courseService.createCourse(courseDTO))
                .as("Should throw NotFoundException when level is not set")
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Course must have a level");
    }
    
    /**
     * Unit test for {@link CourseService#createCourse(CourseDTO)}.
     * <p>
     * <b>Scenario:</b> When creating a course with a non-existing level, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#getLevelByIdOrThrow(Long)}</li>
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
    @DisplayName("Should throw NotFoundException when creating course with non-existing level")
    void shouldThrowNotFoundException_whenCreatingCourseWithNonExistingLevel() {
        // Arrange: Prepare mocks and inputs
        courseDTO.setLevelId(TestDataFactory.NON_EXISTENT_ID);
        
        // Act & Assert: Use helper to assert not found behavior
        TestAssertionUtils.assertLevelNotFound(() -> courseService.createCourse(courseDTO));
    }
    
    // Update ============================================================
    
    /**
     * Unit test for {@link CourseService#updateCourse(String, CourseDTO)}.
     * <p>
     * <b>Scenario:</b> When updating a course with a non-existing code, the service should:
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
    @DisplayName("Should throw NotFoundException when updating course with non-existing code")
    void shouldThrowNotFoundException_whenUpdatingNonExistingCourse() {
        // Arrange, Act & Assert: Use helper to assert not found behavior
        TestAssertionUtils.assertCourseNotFound(() -> courseService.updateCourse(TestDataFactory.NON_EXISTENT_CODE, courseDTO));
    }
    
    /**
     * Unit test for {@link CourseService#updateCourse(String, CourseDTO)}.
     * <p>
     * <b>Scenario:</b> When updating a course with department not set, the service should:
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
    @DisplayName("Should throw BadRequestException when updating course with department not set")
    void shouldThrowBadRequestException_whenUpdatingCourseWithDepartmentNotSet() {
        // Arrange: Prepare mocks and inputs
        courseDTO.setDepartmentId(null);
        doNothing().when(validationService).assertCourseExists(course.getCode(), true);
        
        // Act & Assert: Call the method and verify exception
        assertThatThrownBy(() -> courseService.updateCourse(course.getCode(), courseDTO))
                .as("Should throw BadRequestException when department is not set")
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Course must have a department");
        
        verify(validationService).assertCourseExists(course.getCode(), true);
        verifyNoMoreInteractions(validationService, courseRepository);
    }
    
    /**
     * Unit test for {@link CourseService#updateCourse(String, CourseDTO)}.
     * <p>
     * <b>Scenario:</b> When updating a course with a non-existing department, the service should:
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
    @DisplayName("Should throw NotFoundException when updating course with non-existing department")
    void shouldThrowNotFoundException_whenUpdatingCourseWithNonExistingDepartment() {
        // Arrange: Prepare mocks and inputs
        courseDTO.setDepartmentId(TestDataFactory.NON_EXISTENT_ID);
        doNothing().when(validationService).assertCourseExists(course.getCode(), true);
        
        // Act & Assert: Use helper to assert not found behavior
        TestAssertionUtils.assertDepartmentNotFound(() ->
                courseService.updateCourse(course.getCode(), courseDTO));
        
        verify(validationService).assertCourseExists(course.getCode(), true);
    }
    
    /**
     * Unit test for {@link CourseService#updateCourse(String, CourseDTO)}.
     * <p>
     * <b>Scenario:</b> When updating a course with level not set, the service should:
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
    @DisplayName("Should throw BadRequestException when updating course with level not set")
    void shouldThrowBadRequestException_whenUpdatingCourseWithLevelNotSet() {
        // Arrange: Prepare mocks and inputs
        courseDTO.setLevelId(null);
        doNothing().when(validationService).assertCourseExists(course.getCode(), true);
        
        // Act & Assert: Call the method and verify exception
        assertThatThrownBy(() -> courseService.updateCourse(course.getCode(), courseDTO))
                .as("Should throw BadRequestException when level is not set")
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Course must have a level");
        
        verify(validationService).assertCourseExists(course.getCode(), true);
        verifyNoMoreInteractions(validationService, courseRepository);
    }
    
    /**
     * Unit test for {@link CourseService#updateCourse(String, CourseDTO)}.
     * <p>
     * <b>Scenario:</b> When updating a course with a non-existing level, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#getLevelByIdOrThrow(Long)}</li>
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
    @DisplayName("Should throw NotFoundException when updating course with non-existing level")
    void shouldThrowNotFoundException_whenUpdatingCourseWithNonExistingLevel() {
        // Arrange: Prepare mocks and inputs
        courseDTO.setLevelId(TestDataFactory.NON_EXISTENT_ID);
        doNothing().when(validationService).assertCourseExists(course.getCode(), true);
        
        // Act & Assert: Use helper to assert not found behavior
        TestAssertionUtils.assertLevelNotFound(() ->
                courseService.updateCourse(course.getCode(), courseDTO));
        
        verify(validationService).assertCourseExists(course.getCode(), true);
    }
    
    // Delete ============================================================
    
    /**
     * Unit test for {@link CourseService#deleteCourse(String)}.
     * <p>
     * <b>Scenario:</b> When deleting a course with a non-existing code, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#getCourseByCodeOrThrow(String)}</li>
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
    @DisplayName("Should throw NotFoundException when deleting course with non-existing code")
    void shouldThrowNotFoundException_whenDeletingNonExistingCourse() {
        // Arrange, Act & Assert: Use helper to assert not found behavior
        TestAssertionUtils.assertCourseNotFound(() -> courseService.deleteCourse(TestDataFactory.NON_EXISTENT_CODE));
    }
    
    /**
     * Unit test for {@link CourseService#deleteCourse(String)}.
     * <p>
     * <b>Scenario:</b> When deleting a course with associated enrollments, the service should:
     * <ul>
     *   <li>Fetch the course via {@link ValidationService#getCourseByCodeOrThrow(String)}</li>
     *   <li>Throw a {@link ConflictException}</li>
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
     * <b>Expected result:</b> ConflictException is thrown.
     */
    @Test
    @DisplayName("Should throw ConflictException when deleting course with associated enrollments")
    void shouldThrowConflictException_whenDeletingCourseWithEnrollments() {
        // Arrange: Prepare mocks and inputs
        when(validationService.getCourseByCodeOrThrow(course.getCode())).thenReturn(course);
        
        User user = TestDataFactory.buildUser(Role.STUDENT);
        Student student = TestDataFactory.buildStudent(user, department, level);
        Enrollment enrollment = TestDataFactory.buildEnrollment(student, course, 85.0);
        course.setEnrollments(List.of(enrollment));
        
        // Act & Assert: Verify exception is thrown
        assertThatThrownBy(() -> courseService.deleteCourse(course.getCode()))
                .as("Should throw ConflictException when Course is associated with any Enrollments")
                .isInstanceOf(ConflictException.class)
                .hasMessageContaining("Cannot delete course " + course.getCode() + " because it has enrollments");
        
        // Assert: Verify interactions
        verify(validationService).getCourseByCodeOrThrow(course.getCode());
        verifyNoInteractions(courseRepository);
        verifyNoMoreInteractions(validationService);
    }
}
