package com.example.student.controller;

import com.example.student.entity.Student;
import com.example.student.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * StudentController - REST API Endpoints for Student Management
 * Provides endpoints for CRUD operations with proper HTTP status codes
 */
@RestController
@RequestMapping("/api/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * GET /api/students - Retrieve all students
     * @return List of students with 200 OK or 204 No Content if empty
     */
    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(students);
    }

    /**
     * GET /api/students/{id} - Retrieve student by ID
     * @param id Student ID
     * @return Student with 200 OK, 400 Bad Request if invalid ID, or 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        return studentService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * POST /api/students - Create new student
     * @param student Student object to create
     * @return Created student with 201 Created or 400 Bad Request if null
     */
    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        if (student == null) {
            return ResponseEntity.badRequest().build();
        }
        Student createdStudent = studentService.createStudent(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    /**
     * PUT /api/students/{id} - Update existing student
     * @param id Student ID
     * @param student Updated student details
     * @return Updated student with 200 OK, 400 Bad Request if invalid, or 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        if (id == null || id <= 0 || student == null) {
            return ResponseEntity.badRequest().build();
        }
        return studentService.updateStudent(id, student)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/students/{id} - Delete student
     * @param id Student ID
     * @return 204 No Content if deleted, 400 Bad Request if invalid ID, or 404 Not Found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        if (id == null || id <= 0) {
            return ResponseEntity.badRequest().build();
        }
        return studentService.deleteStudent(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    /**
     * GET /api/students/count - Get total student count
     * @return Total number of students with 200 OK
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalStudents() {
        long count = studentService.getTotalStudents();
        return ResponseEntity.ok(count);
    }
}

