package com.egabi.university.service.authentication;

import com.egabi.university.entity.authentication.RegistrationRequest;
import com.egabi.university.entity.authentication.Role;
import com.egabi.university.entity.authentication.User;
import com.egabi.university.exception.BadRequestException;
import com.egabi.university.service.domain.InstructorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstructorRegistrationHandler implements RegistrationHandler {
    
    private final InstructorService instructorService;
    
    @Override
    public boolean supports(Role role) {
        return Role.INSTRUCTOR.equals(role);
    }
    
    @Override
    public void handleRegistration(RegistrationRequest request, User user) {
        var instructorDTO = request.getInstructorData();
        if (instructorDTO == null) {
            throw new BadRequestException("Instructor data is required for role INSTRUCTOR",
                    "INSTRUCTOR_DATA_REQUIRED");
        }
        instructorService.createInstructor(instructorDTO, user);
    }
}

