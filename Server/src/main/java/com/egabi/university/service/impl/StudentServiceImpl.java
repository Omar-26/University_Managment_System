package com.egabi.university.service.impl;

import com.egabi.university.dto.StudentDTO;
import com.egabi.university.entity.Level;
import com.egabi.university.entity.Student;
import com.egabi.university.exception.NotFoundException;
import com.egabi.university.mapper.StudentMapper;
import com.egabi.university.repository.DepartmentRepository;
import com.egabi.university.repository.LevelRepository;
import com.egabi.university.repository.StudentRepository;
import com.egabi.university.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final DepartmentRepository departmentRepository;
    private final LevelRepository levelRepository;
    
    @Override
    public List<StudentDTO> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return studentMapper.toDTOs(students);
    }
    
    @Override
    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Student with id " + id + " not found", "STUDENT_NOT_FOUND"));
        return studentMapper.toDTO(student);
    }
    
    @Override
    public StudentDTO createStudent(StudentDTO studentDTO) {
        // Map the DTO to the entity
        Student student = studentMapper.toEntity(studentDTO);
        
        // Validate department - level and save student
        student = vaildateAndSaveStudent(student);
        
        // Return the saved student as a DTO
        return studentMapper.toDTO(student);
    }
    
    @Override
    public StudentDTO updateStudent(Long studentId, StudentDTO studentDTO) {
        // Check if the student exists
        if (!studentRepository.existsById(studentId))
            throw new NotFoundException("Student with id " + studentDTO.getId() + " not found", "STUDENT_NOT_FOUND");
        
        // Map the DTO to the entity
        Student updatedStudent = studentMapper.toEntity(studentDTO);
        
        // Validate department - level and update student
        updatedStudent = vaildateAndSaveStudent(updatedStudent);
        
        // Return the saved student as a DTO
        return studentMapper.toDTO(updatedStudent);
    }
    
    @Override
    public void deleteStudent(Long id) {
        // Check if the student exists
        if (!studentRepository.existsById(id))
            throw new NotFoundException("Student with id " + id + " not found", "STUDENT_NOT_FOUND");
        
        // Delete the student
        studentRepository.deleteById(id);
    }
    
    private Student vaildateAndSaveStudent(Student student) {
        // Validate level
        var levelId = Optional.ofNullable(student.getLevel()).map(Level::getId)
                .orElseThrow(() -> new NotFoundException("Student must have a level", "LEVEL_NOT_FOUND"));
        var level = levelRepository.findById(levelId)
                .orElseThrow(() -> new NotFoundException("Level with id " + levelId + " not found", "LEVEL_NOT_FOUND"));
        student.setLevel(level);
        
        // Validate department
        if (student.getDepartment() != null) {
            var departmentId = student.getDepartment().getId();
            var department = departmentRepository.findById(departmentId)
                    .orElseThrow(() -> new NotFoundException("Department with id " + departmentId + " not found", "DEPARTMENT_NOT_FOUND"));
            student.setDepartment(department);
        } else
            student.setDepartment(null);
        
        // Save the student
        return studentRepository.save(student);
    }
    
}
