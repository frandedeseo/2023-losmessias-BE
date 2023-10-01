package com.losmessias.leherer.service_tests;

import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.repository.StudentRepository;
import com.losmessias.leherer.service.StudentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTests {
    @Mock
    private StudentRepository studentRepository;
    @InjectMocks
    private StudentService studentService;

    @Test
    @DisplayName("Get all students")
    void testGetAllStudents() {
        List<Student> students = new ArrayList<>();
        students.add(new Student());
        students.add(new Student());
        when(studentRepository.findAll()).thenReturn(students);
        assertEquals(students, studentService.getAllStudents());
    }

    @Test
    @DisplayName("Get student by id")
    void testGetStudentById() {
        Student student = new Student();
        when(studentRepository.findById(1L)).thenReturn(java.util.Optional.of(student));
        assertEquals(student, studentService.getStudentById(1L));
    }

    @Test
    @DisplayName("Create student")
    void testCreateStudent() {
        Student student = new Student("John", "Doe", "mail", "ubication");
        when(studentRepository.save(student)).thenReturn(student);
        assertEquals(student, studentService.create(student));
    }

    @Test
    @DisplayName("Add reservation to student")
    void testGetStudentSubjects() {
        Student student = new Student("John", "Doe", "mail", "ubication");
        ClassReservation reservation = new ClassReservation();

        when(studentRepository.save(student)).thenReturn(student);
        assertEquals(studentService.addReservationTo(student, reservation), student);
    }

}
