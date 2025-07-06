package com.egabi.university.mapper;

import com.egabi.university.dto.StudentDTO;
import com.egabi.university.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentMapper {
    
    @Mappings({
            @Mapping(source = "department.id", target = "departmentId"),
            @Mapping(source = "level.id", target = "levelId")
    })
    StudentDTO toDTO(Student student);
    
    @Mappings({
            @Mapping(source = "departmentId", target = "department.id"),
            @Mapping(source = "levelId", target = "level.id")
    })
    Student toEntity(StudentDTO studentDTO);
    
    List<StudentDTO> toDTOs(List<Student> students);
    
}
