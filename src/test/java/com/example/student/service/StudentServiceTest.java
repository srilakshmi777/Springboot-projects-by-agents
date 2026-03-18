package com.example.student.service;

import com.example.student.entity.Student;
import com.example.student.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit Tests for StudentService
 * Uses Mockito to mock StudentRepository
 * Tests all business logic methods with different scenarios
 * Ensures 90%+ code coverage and all decision paths are tested
 */
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student1;
    private Student student2;

    @BeforeEach
    void setUp() {
        student1 = Student.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .age(21)
                .build();

        student2 = Student.builder()
                .id(2L)
                .name("Jane Doe")
                .email("jane@example.com")
                .age(22)
                .build();
    }

    // ============ getAllStudents Tests ============

    @Test
    void testGetAllStudents_Success() {
        List<Student> students = Arrays.asList(student1, student2);
        when(studentRepository.findAll()).thenReturn(students);

        List<Student> result = studentService.getAllStudents();

        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    void testGetAllStudents_Empty() {
        when(studentRepository.findAll()).thenReturn(Arrays.asList());

        List<Student> result = studentService.getAllStudents();

        assertTrue(result.isEmpty());
        verify(studentRepository, times(1)).findAll();
    }

    // ============ getStudentById Tests ============

    @Test
    void testGetStudentById_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));

        Optional<Student> result = studentService.getStudentById(1L);

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetStudentById_NotFound() {
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Student> result = studentService.getStudentById(999L);

        assertFalse(result.isPresent());
        verify(studentRepository, times(1)).findById(999L);
    }

    @Test
    void testGetStudentById_WithNullId() {
        Optional<Student> result = studentService.getStudentById(null);

        assertFalse(result.isPresent());
        verify(studentRepository, never()).findById(any());
    }

    @Test
    void testGetStudentById_WithNegativeId() {
        Optional<Student> result = studentService.getStudentById(-1L);

        assertFalse(result.isPresent());
        verify(studentRepository, never()).findById(any());
    }

    @Test
    void testGetStudentById_WithZeroId() {
        Optional<Student> result = studentService.getStudentById(0L);

        assertFalse(result.isPresent());
        verify(studentRepository, never()).findById(any());
    }

    // ============ createStudent Tests ============

    @Test
    void testCreateStudent_Success() {
        when(studentRepository.save(any(Student.class))).thenReturn(student1);

        Student result = studentService.createStudent(student1);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(studentRepository, times(1)).save(student1);
    }

    @Test
    void testCreateStudent_WithNullStudent() {
        assertThrows(IllegalArgumentException.class, () -> {
            studentService.createStudent(null);
        });
        verify(studentRepository, never()).save(any());
    }

    // ============ updateStudent Tests ============

    @Test
    void testUpdateStudent_Success() {
        Student updatedDetails = Student.builder()
                .name("John Updated")
                .email("john.updated@example.com")
                .age(25)
                .build();

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));
        when(studentRepository.save(any(Student.class))).thenReturn(student1);

        Optional<Student> result = studentService.updateStudent(1L, updatedDetails);

        assertTrue(result.isPresent());
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    void testUpdateStudent_NotFound() {
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Student> result = studentService.updateStudent(999L, student2);

        assertFalse(result.isPresent());
        verify(studentRepository, times(1)).findById(999L);
        verify(studentRepository, never()).save(any());
    }

    @Test
    void testUpdateStudent_WithNullId() {
        Optional<Student> result = studentService.updateStudent(null, student2);

        assertFalse(result.isPresent());
        verify(studentRepository, never()).findById(any());
    }

    @Test
    void testUpdateStudent_WithNegativeId() {
        Optional<Student> result = studentService.updateStudent(-1L, student2);

        assertFalse(result.isPresent());
        verify(studentRepository, never()).findById(any());
    }

    @Test
    void testUpdateStudent_WithNullDetails() {
        assertThrows(IllegalArgumentException.class, () -> {
            studentService.updateStudent(1L, null);
        });
        verify(studentRepository, never()).save(any());
    }

    // ============ deleteStudent Tests ============

    @Test
    void testDeleteStudent_Success() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student1));

        boolean result = studentService.deleteStudent(1L);

        assertTrue(result);
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).delete(student1);
    }

    @Test
    void testDeleteStudent_NotFound() {
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        boolean result = studentService.deleteStudent(999L);

        assertFalse(result);
        verify(studentRepository, times(1)).findById(999L);
        verify(studentRepository, never()).delete(any());
    }

    @Test
    void testDeleteStudent_WithNullId() {
        boolean result = studentService.deleteStudent(null);

        assertFalse(result);
        verify(studentRepository, never()).findById(any());
        verify(studentRepository, never()).delete(any());
    }

    @Test
    void testDeleteStudent_WithNegativeId() {
        boolean result = studentService.deleteStudent(-1L);

        assertFalse(result);
        verify(studentRepository, never()).findById(any());
    }

    @Test
    void testDeleteStudent_WithZeroId() {
        boolean result = studentService.deleteStudent(0L);

        assertFalse(result);
        verify(studentRepository, never()).findById(any());
    }

    // ============ getTotalStudents Tests ============

    @Test
    void testGetTotalStudents_WithData() {
        when(studentRepository.count()).thenReturn(5L);

        long count = studentService.getTotalStudents();

        assertEquals(5L, count);
        verify(studentRepository, times(1)).count();
    }

    @Test
    void testGetTotalStudents_Empty() {
        when(studentRepository.count()).thenReturn(0L);

        long count = studentService.getTotalStudents();

        assertEquals(0L, count);
        verify(studentRepository, times(1)).count();
    }
}

