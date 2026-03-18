package com.example.student.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for Student Entity
 * Tests all constructors, getters, setters, and Lombok-generated methods
 */
class StudentTest {

    @Test
    void testStudentBuilderCreation() {
        Student student = Student.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .age(21)
                .build();

        assertNotNull(student);
        assertEquals(1L, student.getId());
        assertEquals("John Doe", student.getName());
        assertEquals("john@example.com", student.getEmail());
        assertEquals(21, student.getAge());
    }

    @Test
    void testStudentNoArgsConstructor() {
        Student student = new Student();

        assertNotNull(student);
        assertNull(student.getId());
        assertNull(student.getName());
        assertNull(student.getEmail());
        assertNull(student.getAge());
    }

    @Test
    void testStudentAllArgsConstructor() {
        Student student = new Student(1L, "Jane Doe", "jane@example.com", 22);

        assertEquals(1L, student.getId());
        assertEquals("Jane Doe", student.getName());
        assertEquals("jane@example.com", student.getEmail());
        assertEquals(22, student.getAge());
    }

    @Test
    void testStudentSettersAndGetters() {
        Student student = new Student();
        student.setId(2L);
        student.setName("Bob Smith");
        student.setEmail("bob@example.com");
        student.setAge(25);

        assertEquals(2L, student.getId());
        assertEquals("Bob Smith", student.getName());
        assertEquals("bob@example.com", student.getEmail());
        assertEquals(25, student.getAge());
    }

    @Test
    void testStudentEquality() {
        Student student1 = Student.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .age(21)
                .build();

        Student student2 = Student.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .age(21)
                .build();

        assertEquals(student1, student2);
    }

    @Test
    void testStudentToString() {
        Student student = Student.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .age(21)
                .build();

        String toString = student.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("John Doe"));
        assertTrue(toString.contains("john@example.com"));
    }

    @Test
    void testStudentHashCode() {
        Student student1 = Student.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .age(21)
                .build();

        Student student2 = Student.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .age(21)
                .build();

        assertEquals(student1.hashCode(), student2.hashCode());
    }

    @Test
    void testStudentDifferentEquality() {
        Student student1 = Student.builder()
                .id(1L)
                .name("John Doe")
                .email("john@example.com")
                .age(21)
                .build();

        Student student2 = Student.builder()
                .id(2L)
                .name("Jane Doe")
                .email("jane@example.com")
                .age(22)
                .build();

        assertNotEquals(student1, student2);
    }
}

