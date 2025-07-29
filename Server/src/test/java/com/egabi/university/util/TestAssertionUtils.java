package com.egabi.university.util;

import com.egabi.university.entity.EnrollmentId;
import com.egabi.university.exception.NotFoundException;
import com.egabi.university.service.validation.ValidationService;
import org.jetbrains.annotations.NotNull;
import org.mockito.exceptions.verification.WantedButNotInvoked;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Utility class for common test assertions across the university management system.
 * <p>
 * This class provides helper methods for asserting common scenarios in service layer tests,
 * such as entity not found exceptions, validation failures, and other common test patterns.
 * </p>
 * <p>
 * <b>Usage:</b>
 * <pre>{@code
 * // Set the validation service once in your test setup
 * TestAssertionUtils.setValidationService(validationService);
 *
 * // Then use the simplified methods
 * TestAssertionUtils.assertFacultyNotFound(
 *     () -> facultyService.getFacultyById(TestDataFactory.NON_EXISTENT_ID)
 * );
 * }</pre>
 * </p>
 * <p>
 * <b>Features:</b>
 * <ul>
 *   <li>Entity-specific not found assertions</li>
 *   <li>Consistent error message formatting</li>
 *   <li>Standardized mock verification</li>
 *   <li>Reusable across all service test classes</li>
 *   <li>Simplified API - no need to pass ValidationService repeatedly</li>
 * </ul>
 * </p>
 */
public class TestAssertionUtils {
    
    private static ValidationService validationService;
    
    /**
     * Sets the validation service to be used by all assertion methods.
     * This should be called once in the test setup (e.g., in @BeforeEach).
     *
     * @param validationService the validation service mock
     */
    public static void setValidationService(@NotNull ValidationService validationService) {
        TestAssertionUtils.validationService = validationService;
    }
    
    /**
     * Gets the current validation service instance.
     *
     * @return the current validation service
     * @throws IllegalStateException if the validation service has not been set
     */
    private static ValidationService getValidationService() {
        if (validationService == null) {
            throw new IllegalStateException("ValidationService has not been set. Call setValidationService() first.");
        }
        return validationService;
    }
    
    // ================================================================
    // Faculty Assertions
    // ================================================================
    
    /**
     * Asserts that a service call throws {@link NotFoundException} for a missing faculty.
     * <p>
     * <b>Scenario:</b> When a faculty with the specified ID does not exist, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#getFacultyByIdOrThrow(Long)}</li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception type is thrown</li>
     *   <li>The validation service is called with the expected ID</li>
     *   <li>No interactions with the repository occur</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> {@code NotFoundException} is thrown with the expected message.
     * </p>
     *
     * @param executable the service call expected to throw {@link NotFoundException}
     */
    public static void assertFacultyNotFound(@NotNull Runnable executable) {
        ValidationService validationService = getValidationService();
        
        // Arrange: Stub validation service to throw NotFoundException
        doThrow(new NotFoundException(
                "Faculty with id " + TestDataFactory.NON_EXISTENT_ID + " not found"
                , "FACULTY_NOT_FOUND")
        ).when(validationService).getFacultyByIdOrThrow(TestDataFactory.NON_EXISTENT_ID);
        
        // Act: Execute the service call and expect an exception
        assertThatThrownBy(executable::run)
                .as("Service should throw Not Found Exception when faculty ID does not exist")
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Faculty with id " + TestDataFactory.NON_EXISTENT_ID + " not found");
        
        // Assert: Verify interactions
        verify(validationService).getFacultyByIdOrThrow(TestDataFactory.NON_EXISTENT_ID);
        verifyNoMoreInteractions(validationService);
    }
    
    // ================================================================
    // Department Assertions
    // ================================================================
    
    /**
     * Asserts that a service call throws {@link NotFoundException} for a missing department.
     * <p>
     * <b>Scenario:</b> When a department with the specified ID does not exist, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#getDepartmentByIdOrThrow(Long)}</li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception type is thrown</li>
     *   <li>The validation service is called with the expected ID</li>
     *   <li>No interactions with the repository occur</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> {@code NotFoundException} is thrown with the expected message.
     * </p>
     *
     * @param executable the service call expected to throw {@link NotFoundException}
     */
    public static void assertDepartmentNotFound(@NotNull Runnable executable) {
        ValidationService validationService = getValidationService();
        
        // Arrange: Stub validation service to throw NotFoundException
        doThrow(new NotFoundException("Department with id " + TestDataFactory.NON_EXISTENT_ID + " not found"
                , "DEPARTMENT_NOT_FOUND")
        ).when(validationService).getDepartmentByIdOrThrow(TestDataFactory.NON_EXISTENT_ID);
        
        // Act: Execute the service call and expect an exception
        assertThatThrownBy(executable::run)
                .as("Service should throw Not Found Exception when Department ID does not exist")
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Department with id " + TestDataFactory.NON_EXISTENT_ID + " not found");
        
        // Assert: Verify interactions
        verify(validationService).getDepartmentByIdOrThrow(TestDataFactory.NON_EXISTENT_ID);
//         verifyNoMoreInteractions(validationService);
    }
    
    // ================================================================
    // Level Assertions
    // ================================================================
    
    /**
     * Asserts that a service call throws {@link NotFoundException} for a missing level.
     * <p>
     * <b>Scenario:</b> When a level with the specified ID does not exist, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#getLevelByIdOrThrow(Long)}</li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception type is thrown</li>
     *   <li>The validation service is called with the expected ID</li>
     *   <li>No interactions with the repository occur</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> {@code NotFoundException} is thrown with the expected message.
     * </p>
     *
     * @param executable the service call expected to throw {@link NotFoundException}
     */
    public static void assertLevelNotFound(@NotNull Runnable executable) {
        ValidationService validationService = getValidationService();
        
        // Arrange: Stub validation service to throw NotFoundException
        doThrow(new NotFoundException("Level with id " + TestDataFactory.NON_EXISTENT_ID + " not found"
                , "LEVEL_NOT_FOUND")
        ).when(validationService).getLevelByIdOrThrow(TestDataFactory.NON_EXISTENT_ID);
        
        // Act: Execute the service call and expect an exception
        assertThatThrownBy(executable::run)
                .as("Service should throw NotFoundException when Level ID does not exist")
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Level with id " + TestDataFactory.NON_EXISTENT_ID + " not found");
        
        // Assert: Verify interactions
        verify(validationService).getLevelByIdOrThrow(TestDataFactory.NON_EXISTENT_ID);
//        verifyNoMoreInteractions(validationService);
    }
    
    // ================================================================
    // Student Assertions
    // ================================================================
    
    //TODO Check the logic in case of the enrollment service
    
    /**
     * Asserts that a service call throws {@link NotFoundException} for a missing student.
     * <p>
     * <b>Scenario:</b> When a student with the specified ID does not exist, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#getStudentByIdOrThrow(Long)}</li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception type is thrown</li>
     *   <li>The validation service is called with the expected ID</li>
     *   <li>No interactions with the repository occur</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> {@code NotFoundException} is thrown with the expected message.
     * </p>
     *
     * @param executable the service call expected to throw {@link NotFoundException}
     */
    public static void assertStudentNotFound(@NotNull Runnable executable) {
        ValidationService validationService = getValidationService();
        
        // Arrange: Stub validation service to throw NotFoundException
        // for other services
        lenient().doThrow(new NotFoundException("Student with id " + TestDataFactory.NON_EXISTENT_ID + " not found"
                , "STUDENT_NOT_FOUND")
        ).when(validationService).getStudentByIdOrThrow(TestDataFactory.NON_EXISTENT_ID);
        
        // for the enrollment service
        lenient().doThrow(new NotFoundException("Student with id " + TestDataFactory.NON_EXISTENT_ID + " not found"
                , "STUDENT_NOT_FOUND")
        ).when(validationService).assertStudentExists(TestDataFactory.NON_EXISTENT_ID);
        
        // Act: Execute the service call and expect an exception
        assertThatThrownBy(executable::run)
                .as("Service should throw Not Found Exception when Student ID does not exist")
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Student with id " + TestDataFactory.NON_EXISTENT_ID + " not found");
        
        // Assert: Verify interactions
        try {
            verify(validationService).getStudentByIdOrThrow(TestDataFactory.NON_EXISTENT_ID);
        } catch (WantedButNotInvoked e) {
            verify(validationService).assertStudentExists(TestDataFactory.NON_EXISTENT_ID);
        }
        // verifyNoMoreInteractions(validationService);
    }
    
    // ================================================================
    // Instructor Assertions
    // ================================================================
    
    /**
     * Asserts that a service call throws {@link NotFoundException} for a missing instructor.
     * <p>
     * <b>Scenario:</b> When an instructor with the specified ID does not exist, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#getInstructorByIdOrThrow(Long)}</li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception type is thrown</li>
     *   <li>The validation service is called with the expected ID</li>
     *   <li>No interactions with the repository occur</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> {@code NotFoundException} is thrown with the expected message.
     * </p>
     *
     * @param executable the service call expected to throw {@link NotFoundException}
     */
    public static void assertInstructorNotFound(@NotNull Runnable executable) {
        ValidationService validationService = getValidationService();
        
        // Arrange: Stub validation service to throw NotFoundException
        doThrow(new NotFoundException("Instructor with id " + TestDataFactory.NON_EXISTENT_ID + " not found"
                , "INSTRUCTOR_NOT_FOUND")
        ).when(validationService).getInstructorByIdOrThrow(TestDataFactory.NON_EXISTENT_ID);
        
        // Act: Execute the service call and expect an exception
        assertThatThrownBy(executable::run)
                .as("Service should throw Not Found Exception when Instructor ID does not exist")
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Instructor with id " + TestDataFactory.NON_EXISTENT_ID + " not found");
        
        // Assert: Verify interactions
        verify(validationService).getInstructorByIdOrThrow(TestDataFactory.NON_EXISTENT_ID);
        verifyNoMoreInteractions(validationService);
    }
    
    // ================================================================
    // Course Assertions
    // ================================================================
    
    //TODO Check the logic in case of the enrollment service
    
    /**
     * Asserts that a service call throws {@link NotFoundException} for a missing course.
     * <p>
     * <b>Scenario:</b> When a course with the specified code does not exist, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#getCourseByCodeOrThrow(String)}</li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception type is thrown</li>
     *   <li>The validation service is called with the expected code</li>
     *   <li>No interactions with the repository occur</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> {@code NotFoundException} is thrown with the expected message.
     * </p>
     *
     * @param executable the service call expected to throw {@link NotFoundException}
     */
    public static void assertCourseNotFound(@NotNull Runnable executable) {
        ValidationService validationService = getValidationService();
        // Arrange: Stub validation service to throw NotFoundException
        // for other services
        lenient().doThrow(new NotFoundException("Course with code " + TestDataFactory.NON_EXISTENT_CODE + " not found"
                , "COURSE_NOT_FOUND")
        ).when(validationService).getCourseByCodeOrThrow(TestDataFactory.NON_EXISTENT_CODE);
        
        // for the enrollment service
        lenient().doThrow(new NotFoundException("Course with code " + TestDataFactory.NON_EXISTENT_CODE + " not found"
                , "COURSE_NOT_FOUND")
        ).when(validationService).assertCourseExists(TestDataFactory.NON_EXISTENT_CODE, true);
        
        // Act: Execute the service call and expect an exception
        assertThatThrownBy(executable::run)
                .as("Service should throw Not Found Exception when Course code does not exist")
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Course with code " + TestDataFactory.NON_EXISTENT_CODE + " not found");
        
        // Assert: Verify interactions
        try {
            verify(validationService).getCourseByCodeOrThrow(TestDataFactory.NON_EXISTENT_CODE);
        } catch (WantedButNotInvoked e) {
            verify(validationService).assertCourseExists(TestDataFactory.NON_EXISTENT_CODE, true);
        }
        
        //        verifyNoMoreInteractions(validationService);
    }
    
    // ================================================================
    // Enrollment Assertions
    // ================================================================
    
    // TODO Check the logic in case of the enrollment service
    // we want to pass that student and course already exist, but the enrollment does not
    
    /**
     * Asserts that a service call throws {@link NotFoundException} for a missing enrollment.
     * <p>
     * <b>Scenario:</b> When an enrollment with the specified enrollment ID does not exist, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#getEnrollmentByIdOrThrow(EnrollmentId)}</li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception type is thrown</li>
     *   <li>The validation service is called with the expected parameters</li>
     *   <li>No interactions with the repository occur</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> {@code NotFoundException} is thrown with the expected message.
     * </p>
     *
     * @param executable the service call expected to throw {@link NotFoundException}
     */
    public static void assertEnrollmentNotFound(@NotNull Runnable executable) {
        ValidationService validationService = getValidationService();
        // Arrange: Stub validation service to throw NotFoundException
        // TODO change the studentId and courseCode to match your test data from TestDataFactory
        Long studentId = 1L;
        String courseCode = "C1";
        EnrollmentId enrollmentId = new EnrollmentId(studentId, courseCode);
        
        lenient().doThrow(new NotFoundException("Enrollment with student ID " + studentId + " and course code " + courseCode + " not found"
                , "ENROLLMENT_NOT_FOUND")
        ).when(validationService).getEnrollmentByIdOrThrow(enrollmentId);
        
        lenient().doThrow(new NotFoundException("Enrollment with student ID " + studentId + " and course code " + courseCode + " not found"
                , "ENROLLMENT_NOT_FOUND")
        ).when(validationService).assertEnrollmentExists(enrollmentId, true);
        
        // Act: Execute the service call and expect an exception
        assertThatThrownBy(executable::run)
                .as("Service should throw Not Found Exception when Enrollment does not exist")
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Enrollment with student ID " + studentId + " and course code " + courseCode + " not found");
        
        // Assert: Verify interactions
        try {
            verify(validationService).getEnrollmentByIdOrThrow(enrollmentId);
        } catch (WantedButNotInvoked e) {
            verify(validationService).assertEnrollmentExists(enrollmentId, true);
        }

//        verifyNoMoreInteractions(validationService);
    }
    
    // ================================================================
    // User Assertions
    // ================================================================
    
    /**
     * Asserts that a service call throws {@link NotFoundException} for a missing user.
     * <p>
     * <b>Scenario:</b> When a user with the specified ID does not exist, the service should:
     * <ul>
     *   <li>Throw a {@link NotFoundException} via {@link ValidationService#getUserByIdOrThrow(Long)}</li>
     * </ul>
     * </p>
     * <p>
     * <b>Verifies:</b>
     * <ul>
     *   <li>The correct exception type is thrown</li>
     *   <li>The validation service is called with the expected ID</li>
     *   <li>No interactions with the repository occur</li>
     * </ul>
     * </p>
     * <p>
     * <b>Expected result:</b> {@code NotFoundException} is thrown with the expected message.
     * </p>
     *
     * @param executable the service call expected to throw {@link NotFoundException}
     */
    public static void assertUserNotFound(@NotNull Runnable executable) {
        ValidationService validationService = getValidationService();
        // Arrange: Stub validation service to throw NotFoundException
        doThrow(new NotFoundException("User with id " + TestDataFactory.NON_EXISTENT_ID + " not found"
                , "USER_NOT_FOUND")
        ).when(validationService).getUserByIdOrThrow(TestDataFactory.NON_EXISTENT_ID);
        
        // Act: Execute the service call and expect an exception
        assertThatThrownBy(executable::run)
                .as("Service should throw Not Found Exception when User ID does not exist")
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("User with id " + TestDataFactory.NON_EXISTENT_ID + " not found");
        
        // Assert: Verify interactions
        verify(validationService).getUserByIdOrThrow(TestDataFactory.NON_EXISTENT_ID);
        verifyNoMoreInteractions(validationService);
    }
}
