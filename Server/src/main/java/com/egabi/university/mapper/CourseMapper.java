package com.egabi.university.mapper;

import com.egabi.university.dto.CourseDTO;
import com.egabi.university.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseMapper {
    
    @Mappings({
            @Mapping(source = "department.id", target = "departmentId"),
            @Mapping(source = "level.id", target = "levelId")
    })
    CourseDTO toDTO(Course course);
    
    @Mappings({
            @Mapping(source = "departmentId", target = "department.id"),
            @Mapping(source = "levelId", target = "level.id")
    })
    Course toEntity(CourseDTO dto);
    
    List<CourseDTO> toDTOs(List<Course> courses);
    
}


//    @Mappings({
//            @Mapping(target = "department", expression = "java(new Department(dto.getDepartmentId()))"),
//            @Mapping(target = "level", expression = "java(new Level(dto.getLevelId()))")
//    })