package com.egabi.university.mapper;

import com.egabi.university.dto.EnrollmentDTO;
import com.egabi.university.entity.Enrollment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {
    
    EnrollmentDTO toDTO(Enrollment enrollment);
    
    Enrollment toEntity(EnrollmentDTO dto);
    
    List<EnrollmentDTO> toDTOs(List<Enrollment> enrollments);
    
}