package com.egabi.university.service.validation.impl;

import com.egabi.university.entity.Department;
import com.egabi.university.entity.Faculty;
import com.egabi.university.entity.Level;
import com.egabi.university.exception.NotFoundException;
import com.egabi.university.repository.DepartmentRepository;
import com.egabi.university.repository.FacultyRepository;
import com.egabi.university.repository.LevelRepository;
import com.egabi.university.service.validation.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationServiceImpl implements ValidationService {
    
    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final LevelRepository levelRepository;
    
    @Override
    public Faculty validateFaculty(Long facultyId) {
        return facultyRepository.findById(facultyId)
                .orElseThrow(() -> new NotFoundException(
                        "Faculty with id " + facultyId + " not found", "FACULTY_NOT_FOUND"));
    }
    
    @Override
    public Department validateDepartment(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new NotFoundException(
                        "Department with id " + departmentId + " not found", "DEPARTMENT_NOT_FOUND"));
    }
    
    @Override
    public Level validateLevel(Long levelId) {
        return levelRepository.findById(levelId)
                .orElseThrow(() -> new NotFoundException(
                        "Level with id " + levelId + " not found", "LEVEL_NOT_FOUND"));
    }
}


// after modular refactoring
//Long facultyId = extractIdOrThrow(
//        level.getFaculty(),
//        Faculty::getId,
//        "Level must be linked to a faculty",
//        "FACULTY_NOT_FOUND"
//);

//Faculty faculty = validateFaculty(facultyId);
//level.
//setFaculty(faculty);

//before
//var facultyId = Optional.ofNullable(level.getFaculty()).map(Faculty::getId)
//        .orElseThrow(...);
// after
//var facultyId = validationService.extractIdOrThrow(level.getFaculty(), Faculty::getId, ...);


/// **
// * Safely extracts an ID from an entity reference.
// * Throws NotFoundException with the given message if the entity is null.
// *
// * @param entity the entity reference
// * @param extractor function to extract ID
// * @param message error message
// * @param errorCode custom error code
// * @return the extracted ID
// * @param <E> entity type
// * @param <ID> ID type
// */
//default <E, ID> ID extractIdOrThrow(E entity, java.util.function.Function<E, ID> extractor,
//                                    String message, String errorCode) {
//    return Optional.ofNullable(entity)
//            .map(extractor)
//            .orElseThrow(() -> new NotFoundException(message, errorCode));
//}
