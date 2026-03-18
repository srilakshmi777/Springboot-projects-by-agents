package com.example.student.service;

import com.example.student.entity.Student;
import com.example.student.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * StudentService - Business Logic Layer
 * Handles all CRUD operations for Student entity with transaction management
 */
@Service
@Transactional
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    /**
     * Get all students from database
     * @return List of all students
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * Get student by ID with validation
     * @param id Student ID
     * @return Optional containing student if found
     */
    public Optional<Student> getStudentById(Long id) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        return studentRepository.findById(id);
    }

    /**
     * Create a new student
     * @param student Student object to create
     * @return Created student with ID
     * @throws IllegalArgumentException if student is null
     */
    public Student createStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        return studentRepository.save(student);
    }

    /**
     * Update existing student
     * @param id Student ID to update
     * @param studentDetails New student details
     * @return Optional containing updated student if found
     * @throws IllegalArgumentException if studentDetails is null
     */
    public Optional<Student> updateStudent(Long id, Student studentDetails) {
        if (id == null || id <= 0) {
            return Optional.empty();
        }
        if (studentDetails == null) {
            throw new IllegalArgumentException("Student details cannot be null");
        }
        return studentRepository.findById(id).map(student -> {
            student.setName(studentDetails.getName());
            student.setEmail(studentDetails.getEmail());
            student.setAge(studentDetails.getAge());
            return studentRepository.save(student);
        });
    }

    /**
     * Delete student by ID
     * @param id Student ID to delete
     * @return true if deleted successfully, false if not found
     */
    public boolean deleteStudent(Long id) {
        if (id == null || id <= 0) {
            return false;
        }
        return studentRepository.findById(id).map(student -> {
            studentRepository.delete(student);
            return true;
        }).orElse(false);
    }

    /**
     * Get total count of students
     * @return Total number of students in database
     */
    public long getTotalStudents() {
        return studentRepository.count();
    }
}

