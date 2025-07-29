package com.egabi.university.util;

import com.egabi.university.dto.*;
import com.egabi.university.dto.authentication.request.AuthenticationRequest;
import com.egabi.university.dto.authentication.request.RegistrationRequest;
import com.egabi.university.dto.authentication.response.AuthenticationResponse;
import com.egabi.university.entity.*;
import com.egabi.university.entity.authentication.Role;
import com.egabi.university.entity.authentication.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * <h2>TestDataFactory</h2>
 * <p>
 * Central utility for creating reusable test Entities and DTOs.
 * </p>
 * Keeps test data consistent and easy to maintain across unit tests.
 * </p>
 *
 * <h3>Usage:</h3>
 * <pre>{@code
 * Faculty faculty = TestDataFactory.buildFaculty();
 * Department department = TestDataFactory.buildDepartment(faculty);
 * DepartmentDTO departmentDTO = TestDataFactory.buildDepartmentDTO();
 * }</pre>
 *
 * <p>
 * <b>Note:</b> This class should only be used in test sources.
 * </p>
 */
public final class TestDataFactory {
    
    /**
     * Prevent instantiation.
     */
    private TestDataFactory() {
    }
    
    /**
     * Shared ID to represent an entity that does NOT exist.
     * Use for negative test scenarios.
     */
    public static final long NON_EXISTENT_ID = Long.MAX_VALUE;
    public static final String NON_EXISTENT_CODE = "NON_EXISTENT_CODE";
    
    // ================================================================
    // Faculty
    // ================================================================
    
    /**
     * Builds a default Faculty entity with typical test values.
     *
     * @return Faculty { id : 1, name : "Engineering", departments : [] }
     */
    public static Faculty buildFaculty() {
        return Faculty.builder()
                .id(1L)
                .name("Engineering")
                .departments(List.of())
                .build();
    }
    
    /**
     * Builds a Faculty entity with a custom ID and name.
     *
     * @param id   ID to use.
     * @param name Name to use.
     * @return Faculty { id : id, name : name, departments : [] }
     */
    public static Faculty buildFaculty(Long id, String name) {
        return Faculty.builder()
                .id(id)
                .name(name)
                .departments(List.of())
                .build();
    }
    
    /**
     * Builds a default FacultyDTO.
     *
     * @return FacultyDTO { id : 1, name : "Engineering" }
     */
    public static FacultyDTO buildFacultyDTO() {
        return new FacultyDTO(1L, "Engineering");
    }
    
    /**
     * Builds a FacultyDTO with custom values.
     *
     * @param id   ID to use.
     * @param name Name to use.
     * @return FacultyDTO { id : id, name : name }
     */
    public static FacultyDTO buildFacultyDTO(Long id, String name) {
        return new FacultyDTO(id, name);
    }
    
    // ================================================================
    // Department
    // ================================================================
    
    /**
     * Builds a default Department entity with given Faculty.
     *
     * @param faculty Faculty to associate.
     * @return Department { id : 1, name : "Computer Engineering", faculty : faculty, students : [], courses : [] }
     */
    public static Department buildDepartment(Faculty faculty) {
        return Department.builder()
                .id(1L)
                .name("Computer Engineering")
                .faculty(faculty)
                .students(List.of())
                .courses(List.of())
                .build();
    }
    
    /**
     * Builds a Department entity with custom ID and name.
     *
     * @param id      ID to use.
     * @param name    Name to use.
     * @param faculty Faculty to associate.
     * @return Department { id : id, name : name, faculty : faculty, students : [], courses : [] }
     */
    public static Department buildDepartment(Long id, String name, Faculty faculty) {
        return Department.builder()
                .id(id)
                .name(name)
                .faculty(faculty)
                .students(List.of())
                .courses(List.of())
                .build();
    }
    
    /**
     * Builds a default DepartmentDTO.
     *
     * @return DepartmentDTO { id : 1, name : "Computer Engineering", facultyId : 1 }
     */
    public static DepartmentDTO buildDepartmentDTO() {
        return new DepartmentDTO(1L, "Computer Engineering", 1L);
    }
    
    /**
     * Builds a DepartmentDTO with custom values.
     *
     * @param id        ID to use.
     * @param name      Name to use.
     * @param facultyId Faculty ID to associate.
     * @return DepartmentDTO { id : id, name : name, facultyId : facultyId }
     */
    public static DepartmentDTO buildDepartmentDTO(Long id, String name, Long facultyId) {
        return new DepartmentDTO(id, name, facultyId);
    }
    
    // ================================================================
    // Level
    // ================================================================
    
    /**
     * Builds a default Level entity with typical test values.
     *
     * @return Level { id : 1, name : "Freshman", faculty : faculty, students : [], courses : [] }
     */
    public static Level buildLevel(Faculty faculty) {
        return Level.builder()
                .id(1L)
                .name("Freshman")
                .faculty(faculty)
                .students(List.of())
                .courses(List.of())
                .build();
    }
    
    /**
     * Builds a Level entity with custom ID, name, and faculty.
     *
     * @param id      ID to use.
     * @param name    Name to use.
     * @param faculty Faculty to associate.
     * @return Level { id : id, name : name, faculty : faculty, students : [], courses : [] }
     */
    public static Level buildLevel(Long id, String name, Faculty faculty) {
        return Level.builder()
                .id(id)
                .name(name)
                .faculty(faculty)
                .students(List.of())
                .courses(List.of())
                .build();
    }
    
    /**
     * Builds a default LevelDTO.
     *
     * @return LevelDTO { id : 1, name : "Freshman", facultyId : 1 }
     */
    public static LevelDTO buildLevelDTO() {
        return new LevelDTO(1L, "Freshman", 1L);
    }
    
    /**
     * Builds a LevelDTO with custom values.
     *
     * @param id        ID to use.
     * @param name      Name to use.
     * @param facultyId Faculty ID to associate.
     * @return LevelDTO { id : id, name : name, facultyId : facultyId }
     */
    public static LevelDTO buildLevelDTO(Long id, String name, Long facultyId) {
        return new LevelDTO(id, name, facultyId);
    }
    
    // ================================================================
    // Student
    // ================================================================
    
    /**
     * Builds a default Student entity for a given User, Department and Level.
     *
     * @param department Department to associate.
     * @return Student { id : 1, user : user, firstName : "Student", lastName : "One", phoneNumber : "1234567890", dateOfBirth : "2002-02-02", gender : "Male", department : department, level : level, enrollments : [] }
     */
    public static Student buildStudent(User user, Department department, Level level) {
        return Student.builder()
                .id(1L)
                .user(user)
                .firstName("Student")
                .lastName("One")
                .phoneNumber("1234567890")
                .dateOfBirth(LocalDate.of(2002, 2, 2))
                .gender("Male")
                .department(department)
                .level(level)
                .enrollments(List.of())
                .build();
    }
    
    /**
     * Builds a Student entity with custom values for a given User, Department and Level.
     *
     * @param id         ID to use.
     * @param firstName  First Name to use.
     * @param lastName   Last Name to use.
     * @param department Department to associate.
     * @return Student { id : id, firstName : firstName, lastName : lastName, phoneNumber : phoneNumber , dateOfBirth : dateOfBirth, gender : gender, user : user, department : department, level : level , enrollments : [] }
     */
    public static Student buildStudent(Long id, String firstName, String lastName,
                                       String phoneNumber, LocalDate dateOfBirth, String gender,
                                       User user, Department department, Level level) {
        return Student.builder()
                .id(id)
                .user(user)
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .dateOfBirth(dateOfBirth)
                .gender(gender)
                .department(department)
                .level(level)
                .enrollments(List.of())
                .build();
    }
    
    /**
     * Builds a default StudentDTO.
     *
     * @return StudentDTO { id : 1, userId : 1, firstName : "Student", lastName : "One", phoneNumber : "1234567890", dateOfBirth : "2002-02-02", gender : "Male", , facultyId : 1, facultyName : "Engineering", departmentId : 1, departmentName : "Computer Engineering", levelId : 1, levelName : "Freshman"}
     */
    public static StudentDTO buildStudentDTO() {
        return new StudentDTO(
                1L, 1L, "Student", "One", "1234567890",
                LocalDate.of(2002, 2, 2), "Male",
                1L, "Engineering", 1L, "Computer Engineering", 1L,
                "Freshman"
        );
    }
    
    /**
     * Builds a StudentDTO with custom values.
     *
     * @param id             ID to use.
     * @param userId         User ID to use.
     * @param firstName      First Name to use.
     * @param lastName       Last Name to use.
     * @param phoneNumber    Phone Number to use.
     * @param dateOfBirth    Date of Birth to use.
     * @param gender         Gender to use.
     * @param facultyId      Faculty ID to use.
     * @param facultyName    Faculty Name to use.
     * @param departmentId   Department ID to use.
     * @param departmentName Department Name to use.
     * @param levelId        Level ID to use.
     * @param levelName      Level Name to use.
     * @return StudentDTO { id : id, userId : userId, firstName : firstName, lastName : lastName, phoneNumber : phoneNumber, dateOfBirth : dateOfBirth, gender : gender, , facultyId : facultyId, facultyName : facultyName,  departmentId : departmentId, departmentName : departmentName, levelId : levelId, levelName : levelName}
     */
    public static StudentDTO buildStudentDTO(Long id, Long userId, String firstName, String lastName,
                                             String phoneNumber, LocalDate dateOfBirth, String gender,
                                             Long facultyId, String facultyName,
                                             Long departmentId, String departmentName,
                                             Long levelId, String levelName) {
        return new StudentDTO(id, userId, firstName, lastName,
                phoneNumber, dateOfBirth, gender,
                facultyId, facultyName,
                departmentId, departmentName,
                levelId, levelName);
    }
    
    // ================================================================
    // Instructor
    // ================================================================
    
    /**
     * Builds a default Instructor entity for a given Department.
     *
     * @param department Department to associate.
     * @return Instructor { id : 1L, user : user, firstName : "Instructor", lastName : "One", phoneNumber : "1234567890", dateOfBirth : "1990-09-09", gender : "Male", department : department, courses : [] }
     */
    public static Instructor buildInstructor(User user, Department department) {
        return Instructor.builder()
                .id(1L)
                .user(user)
                .firstName("Instructor")
                .lastName("One")
                .phoneNumber("1234567890")
                .gender("Male")
                .dateOfBirth(LocalDate.of(1990, 9, 9))
                .department(department)
                .courses(new ArrayList<>())
                .build();
    }
    
    /**
     * Builds an Instructor entity with custom values.
     *
     * @param id          ID to use.
     * @param firstName   First Name to use.
     * @param lastName    Last Name to use.
     * @param phoneNumber Phone Number to use.
     * @param dateOfBirth Date of Birth to use.
     * @param gender      Gender to use.
     * @param user        User to associate.
     * @param department  Department to associate.
     * @return Instructor { id : id, user : user, firstName : firstName, lastName : lastName, phoneNumber : phoneNumber, dateOfBirth : dateOfBirth, gender : gender, department : department, courses : [] }
     */
    public static Instructor buildInstructor(Long id, String firstName, String lastName,
                                             String phoneNumber, LocalDate dateOfBirth, String gender,
                                             User user, Department department) {
        return Instructor.builder()
                .id(id)
                .user(user)
                .firstName(firstName)
                .lastName(lastName)
                .phoneNumber(phoneNumber)
                .dateOfBirth(dateOfBirth)
                .gender(gender)
                .department(department)
                .build();
    }
    
    /**
     * Builds a default InstructorDTO.
     *
     * @return InstructorDTO { id : 1, userId : 1, firstName : "John", lastName : "Doe", phoneNumber : "+1234567890", dateOfBirth : "1980-01-01", gender : "Male", departmentId : 1, departmentName : "Computer Engineering", facultyId : 1, facultyName : "Engineering", courseCodes : ["CS101", "CS102"] }
     */
    public static InstructorDTO buildInstructorDTO() {
        return new InstructorDTO(
                1L, 2L, "Instructor", "One", "1234567890",
                LocalDate.of(1990, 9, 9), "Male",
                1L, "Computer Engineering", 1L, "Engineering",
                List.of("CS101", "CS102")
        );
    }
    
    /**
     * Builds an InstructorDTO with custom values.
     *
     * @param id             ID to use.
     * @param userId         User ID to use.
     * @param firstName      First Name to use.
     * @param lastName       Last Name to use.
     * @param phoneNumber    Phone Number to use.
     * @param dateOfBirth    Date of Birth to use.
     * @param gender         Gender to use.
     * @param departmentId   Department ID to use.
     * @param departmentName Department Name to use.
     * @param facultyId      Faculty ID to use.
     * @param facultyName    Faculty Name to use.
     * @param courseCodes    Course Codes to use.
     * @return InstructorDTO { id : id, userId : userId, firstName : firstName, lastName : lastName, phoneNumber : phoneNumber, dateOfBirth : dateOfBirth, gender : gender, departmentId : departmentId, departmentName : departmentName, facultyId : facultyId, facultyName : facultyName, courseCodes : courseCodes }
     */
    public static InstructorDTO buildInstructorDTO(Long id, Long userId, String firstName, String lastName,
                                                   String phoneNumber, LocalDate dateOfBirth, String gender,
                                                   Long departmentId, String departmentName,
                                                   Long facultyId, String facultyName,
                                                   List<String> courseCodes) {
        return new InstructorDTO(id, userId, firstName, lastName, phoneNumber, dateOfBirth, gender,
                departmentId, departmentName, facultyId, facultyName, courseCodes);
    }
    
    // ================================================================
    // Course
    // ================================================================
    
    /**
     * Builds a default Course entity for a given Department.
     *
     * @param department Department to associate.
     * @return Course { code : "CS101", name : "Algorithms", credits : 3, department : department }
     */
    public static Course buildCourse(Department department, Level level) {
        return Course.builder()
                .code("C1")
                .name("Course One")
                .credits(3)
                .department(department)
                .level(level)
                .enrollments(List.of())
                .instructors(List.of())
                .build();
    }
    
    /**
     * Builds a Course entity with custom code and name.
     *
     * @param code       Code to use.
     * @param name       Name to use.
     * @param department Department to associate.
     * @return Course { code : code, name : name, credits : 3, department : department }
     */
    public static Course buildCourse(String code, String name, Department department) {
        return Course.builder()
                .code(code)
                .name(name)
                .credits(3)
                .department(department)
                .build();
    }
    
    /**
     * Builds a default CourseDTO.
     *
     * @return CourseDTO { code : "CS101", name : "Algorithms", credits : 3, departmentId : 1, levelId : 1 }
     */
    public static CourseDTO buildCourseDTO() {
        return new CourseDTO("CS101", "Algorithms", 3, 1L, 1L);
    }
    
    /**
     * Builds a CourseDTO with custom values.
     *
     * @param code         Code to use.
     * @param name         Name to use.
     * @param credits      Credits to use.
     * @param departmentId Department ID to use.
     * @param levelId      Level ID to use.
     * @return CourseDTO { code : code, name : name, credits : credits, departmentId : departmentId, levelId : levelId }
     */
    public static CourseDTO buildCourseDTO(String code, String name, Integer credits, Long departmentId, Long levelId) {
        return new CourseDTO(code, name, credits, departmentId, levelId);
    }
    
    // ================================================================
    // Enrollment
    // ================================================================
    
    /**
     * Builds a default Enrollment entity for given Student and Course.
     *
     * @param student Student to associate.
     * @param course  Course to associate.
     * @return Enrollment { id : { studentId : student.id, courseCode : course.code }, student : student, course : course, grade : 85.0 }
     */
    public static Enrollment buildEnrollment(Student student, Course course, double grade) {
        EnrollmentId enrollmentId = new EnrollmentId(student.getId(), course.getCode());
        return Enrollment.builder()
                .id(enrollmentId)
                .student(student)
                .course(course)
                .grade(grade)
                .build();
    }
    
    /**
     * Builds an Enrollment entity with custom values.
     *
     * @param studentId  Student ID to use.
     * @param courseCode Course Code to use.
     * @param grade      Grade to use.
     * @param student    Student to associate.
     * @param course     Course to associate.
     * @return Enrollment { id : { studentId : studentId, courseCode : courseCode }, student : student, course : course, grade : grade }
     */
    public static Enrollment buildEnrollment(Long studentId, String courseCode, Double grade,
                                             Student student, Course course) {
        Enrollment enrollment = new Enrollment();
        enrollment.setId(new EnrollmentId(studentId, courseCode));
        enrollment.setStudent(student);
        enrollment.setCourse(course);
        enrollment.setGrade(grade);
        return enrollment;
    }
    
    /**
     * Builds a default EnrollmentDTO.
     *
     * @return EnrollmentDTO { studentId : 1, courseCode : "CS101", grade : 85.0 }
     */
    public static EnrollmentDTO buildEnrollmentDTO() {
        return new EnrollmentDTO(1L, "C1", 85.0);
    }
    
    /**
     * Builds an EnrollmentDTO with custom values.
     *
     * @param studentId  Student ID to use.
     * @param courseCode Course Code to use.
     * @param grade      Grade to use.
     * @return EnrollmentDTO { studentId : studentId, courseCode : courseCode, grade : grade }
     */
    public static EnrollmentDTO buildEnrollmentDTO(Long studentId, String courseCode, Double grade) {
        return new EnrollmentDTO(studentId, courseCode, grade);
    }
    
    // ================================================================
    // User (Authentication)
    // ================================================================
    
    /**
     * Builds a default {@link User} entity for the specified {@link Role}.
     * <p>
     * The returned user will have a unique ID, email, and password based on the role:
     * <ul>
     *   <li>ADMIN: id = 1L, email = "admin@egabi.com", password = "admin1"</li>
     *   <li>STUDENT: id = 2L, email = "student@egabi.com", password = "student2"</li>
     *   <li>INSTRUCTOR: id = 3L, email = "instructor@egabi.com", password = "instructor3"</li>
     * </ul>
     * The user will be enabled and not locked by default.
     * </p>
     *
     * @param role the {@link Role} to assign to the user (ADMIN, STUDENT, or INSTRUCTOR)
     * @return a new {@link User} instance with default values for the given role
     * @throws IllegalArgumentException if the role is not supported
     */
    public static User buildUser(Role role) {
        long id;
        String email;
        String password;
        
        switch (role) {
            case ADMIN -> {
                id = 1L;
                email = "admin@egabi.com";
                password = "admin1";
            }
            case STUDENT -> {
                id = 2L;
                email = "student@egabi.com";
                password = "student2";
            }
            case INSTRUCTOR -> {
                id = 3L;
                email = "instructor@egabi.com";
                password = "instructor3";
            }
            default -> throw new IllegalArgumentException("Unsupported role: " + role);
        }
        
        return User.builder()
                .id(id)
                .email(email)
                .password(password)
                .role(role)
                .locked(false)
                .enabled(true)
                .build();
    }
    
    /**
     * Builds a User entity with custom values.
     *
     * @param id       ID to use.
     * @param email    Email to use.
     * @param password Password to use.
     * @param role     Role to use.
     * @return User { id : id, email : email, password : password, role : role, locked : false, enabled : true, student : null, instructor : null }
     */
    public static User buildUser(Long id, String email, String password, Role role) {
        return User.builder()
                .id(id)
                .email(email)
                .password(password)
                .role(role)
                .locked(false)
                .enabled(true)
                .build();
    }
    
    // ================================================================
    // Authentication DTOs
    // ================================================================
    
    /**
     * Builds a default AuthenticationRequest.
     *
     * @return AuthenticationRequest { email : "test@example.com", password : "password123" }
     */
    public static AuthenticationRequest buildAuthenticationRequest() {
        return AuthenticationRequest.builder()
                .email("test@example.com")
                .password("password123")
                .build();
    }
    
    /**
     * Builds an AuthenticationRequest with custom values.
     *
     * @param email    Email to use.
     * @param password Password to use.
     * @return AuthenticationRequest { email : email, password : password }
     */
    public static AuthenticationRequest buildAuthenticationRequest(String email, String password) {
        return AuthenticationRequest.builder()
                .email(email)
                .password(password)
                .build();
    }
    
    /**
     * Builds a default RegistrationRequest for a Student.
     *
     * @return RegistrationRequest { email : "student@example.com", password : "password123", role : STUDENT, studentData : StudentDTO, instructorData : null }
     */
    public static RegistrationRequest buildStudentRegistrationRequest() {
        return RegistrationRequest.builder()
                .email("student@example.com")
                .password("password123")
                .role(Role.STUDENT)
                .studentData(buildStudentDTO())
                .build();
    }
    
    /**
     * Builds a default RegistrationRequest for an Instructor.
     *
     * @return RegistrationRequest { email : "instructor@example.com", password : "password123", role : INSTRUCTOR, studentData : null, instructorData : InstructorDTO }
     */
    public static RegistrationRequest buildInstructorRegistrationRequest() {
        return RegistrationRequest.builder()
                .email("instructor@example.com")
                .password("password123")
                .role(Role.INSTRUCTOR)
                .instructorData(buildInstructorDTO())
                .build();
    }
    
    /**
     * Builds a RegistrationRequest with custom values.
     *
     * @param email          Email to use.
     * @param password       Password to use.
     * @param role           Role to use.
     * @param studentData    Student data to use (can be null).
     * @param instructorData Instructor data to use (can be null).
     * @return RegistrationRequest { email : email, password : password, role : role, studentData : studentData, instructorData : instructorData }
     */
    public static RegistrationRequest buildRegistrationRequest(String email, String password, Role role,
                                                               StudentDTO studentData, InstructorDTO instructorData) {
        return RegistrationRequest.builder()
                .email(email)
                .password(password)
                .role(role)
                .studentData(studentData)
                .instructorData(instructorData)
                .build();
    }
    
    /**
     * Builds a default AuthenticationResponse.
     *
     * @return AuthenticationResponse { token : "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c" }
     */
    public static AuthenticationResponse buildAuthenticationResponse() {
        return AuthenticationResponse.builder()
                .token("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
                .build();
    }
    
    /**
     * Builds an AuthenticationResponse with custom token.
     *
     * @param token Token to use.
     * @return AuthenticationResponse { token : token }
     */
    public static AuthenticationResponse buildAuthenticationResponse(String token) {
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }
}
