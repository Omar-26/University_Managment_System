package com.egabi.university.service.authentication.registration;

import com.egabi.university.dto.authentication.request.RegistrationRequest;
import com.egabi.university.entity.authentication.Role;
import com.egabi.university.entity.authentication.User;

public interface RegistrationHandler {
    boolean supports(Role role);
    
    void handleRegistration(RegistrationRequest registrationRequest, User user);
}
