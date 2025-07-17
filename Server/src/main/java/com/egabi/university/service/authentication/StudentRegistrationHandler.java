package com.egabi.university.service.authentication;

import com.egabi.university.entity.authentication.RegistrationRequest;
import com.egabi.university.entity.authentication.Role;
import com.egabi.university.entity.authentication.User;
import com.egabi.university.exception.BadRequestException;
import com.egabi.university.service.domain.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentRegistrationHandler implements RegistrationHandler {
    
    private final StudentService studentService;
    
    @Override
    public boolean supports(Role role) {
        return Role.STUDENT.equals(role);
    }
    
    @Override
    public void handleRegistration(RegistrationRequest request, User user) {
        var studentDTO = request.getStudentData();
        if (studentDTO == null) {
            throw new BadRequestException("Student data is required for registration",
                    "STUDENT_DATA_REQUIRED");
        }
        studentService.createStudent(studentDTO, user);
    }
}
