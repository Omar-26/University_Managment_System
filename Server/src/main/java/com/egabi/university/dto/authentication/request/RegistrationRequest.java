package com.egabi.university.dto.authentication.request;

import com.egabi.university.dto.InstructorDTO;
import com.egabi.university.dto.StudentDTO;
import com.egabi.university.entity.authentication.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a request for user registration.
 * Contains the user's email, password, role, and additional data based on the role.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {
    
    @NotBlank(message = "Email is required")
    private String email;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    @NotNull(message = "Role is required")
    private Role role;
    
    private StudentDTO studentData;
    private InstructorDTO instructorData;
//    private AdminData adminData;
}
