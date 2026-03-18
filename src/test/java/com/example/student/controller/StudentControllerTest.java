package com.example.student.controller;

import com.example.student.entity.Student;
import com.example.student.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration Tests for StudentController
 * Uses MockMvc to test REST API endpoints
 * Tests all request methods and response status codes
 * Ensures 90%+ code coverage and all decision paths are tested
 */
@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Student student1;
    private Student student2;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
        objectMapper = new ObjectMapper();

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

    // ============ GET /api/students Tests ============

    @Test
    void testGetAllStudents_Success() throws Exception {
        when(studentService.getAllStudents()).thenReturn(Arrays.asList(student1, student2));

        mockMvc.perform(get("/api/students")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"));

        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    void testGetAllStudents_Empty() throws Exception {
        when(studentService.getAllStudents()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/students")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(studentService, times(1)).getAllStudents();
    }

    // ============ GET /api/students/{id} Tests ============

    @Test
    void testGetStudentById_Success() throws Exception {
        when(studentService.getStudentById(1L)).thenReturn(Optional.of(student1));

        mockMvc.perform(get("/api/students/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(studentService, times(1)).getStudentById(1L);
    }

    @Test
    void testGetStudentById_NotFound() throws Exception {
        when(studentService.getStudentById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/students/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(studentService, times(1)).getStudentById(999L);
    }

    @Test
    void testGetStudentById_InvalidNegativeId() throws Exception {
        mockMvc.perform(get("/api/students/-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(studentService, never()).getStudentById(any());
    }

    @Test
    void testGetStudentById_InvalidZeroId() throws Exception {
        mockMvc.perform(get("/api/students/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(studentService, never()).getStudentById(any());
    }

    // ============ POST /api/students Tests ============

    @Test
    void testCreateStudent_Success() throws Exception {
        when(studentService.createStudent(any(Student.class))).thenReturn(student1);

        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(studentService, times(1)).createStudent(any(Student.class));
    }

    @Test
    void testCreateStudent_WithNullBody() throws Exception {
        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());

        verify(studentService, never()).createStudent(any());
    }

    // ============ PUT /api/students/{id} Tests ============

    @Test
    void testUpdateStudent_Success() throws Exception {
        when(studentService.updateStudent(eq(1L), any(Student.class)))
                .thenReturn(Optional.of(student1));

        mockMvc.perform(put("/api/students/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));

        verify(studentService, times(1)).updateStudent(eq(1L), any(Student.class));
    }

    @Test
    void testUpdateStudent_NotFound() throws Exception {
        when(studentService.updateStudent(eq(999L), any(Student.class)))
                .thenReturn(Optional.empty());

        mockMvc.perform(put("/api/students/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student1)))
                .andExpect(status().isNotFound());

        verify(studentService, times(1)).updateStudent(eq(999L), any(Student.class));
    }

    @Test
    void testUpdateStudent_InvalidNegativeId() throws Exception {
        mockMvc.perform(put("/api/students/-1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student1)))
                .andExpect(status().isBadRequest());

        verify(studentService, never()).updateStudent(any(), any());
    }

    @Test
    void testUpdateStudent_WithNullBody() throws Exception {
        mockMvc.perform(put("/api/students/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("null"))
                .andExpect(status().isBadRequest());

        verify(studentService, never()).updateStudent(any(), any());
    }

    // ============ DELETE /api/students/{id} Tests ============

    @Test
    void testDeleteStudent_Success() throws Exception {
        when(studentService.deleteStudent(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/students/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(studentService, times(1)).deleteStudent(1L);
    }

    @Test
    void testDeleteStudent_NotFound() throws Exception {
        when(studentService.deleteStudent(999L)).thenReturn(false);

        mockMvc.perform(delete("/api/students/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(studentService, times(1)).deleteStudent(999L);
    }

    @Test
    void testDeleteStudent_InvalidNegativeId() throws Exception {
        mockMvc.perform(delete("/api/students/-1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(studentService, never()).deleteStudent(any());
    }

    @Test
    void testDeleteStudent_InvalidZeroId() throws Exception {
        mockMvc.perform(delete("/api/students/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(studentService, never()).deleteStudent(any());
    }

    // ============ GET /api/students/count Tests ============

    @Test
    void testGetTotalStudents_WithData() throws Exception {
        when(studentService.getTotalStudents()).thenReturn(5L);

        mockMvc.perform(get("/api/students/count")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(5L));

        verify(studentService, times(1)).getTotalStudents();
    }

    @Test
    void testGetTotalStudents_Empty() throws Exception {
        when(studentService.getTotalStudents()).thenReturn(0L);

        mockMvc.perform(get("/api/students/count")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(0L));

        verify(studentService, times(1)).getTotalStudents();
    }
}

