package com.egabi.university.service.authentication;

import com.egabi.university.entity.authentication.RegistrationRequest;
import com.egabi.university.entity.authentication.Role;
import com.egabi.university.entity.authentication.User;

public interface RegistrationHandler {
    boolean supports(Role role);
    
    void handleRegistration(RegistrationRequest registrationRequest, User user);
}
