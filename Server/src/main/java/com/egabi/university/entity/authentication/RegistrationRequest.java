package com.egabi.university.entity.authentication;

import com.egabi.university.dto.InstructorDTO;
import com.egabi.university.dto.StudentDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
