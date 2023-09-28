package com.losmessias.leherer.controller;

import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.service.ClassReservationService;
import com.losmessias.leherer.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final ClassReservationService classReservationService;

    @GetMapping("/all")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @PostMapping("/create")
    public Student addStudent(@RequestBody Student student) {
        return studentService.create(student);
    }

    @PostMapping("/addReservation")
    public Student addReservationToStudent(Long studentId, Long reservationId) {
        Student student = studentService.getStudentById(studentId);
        ClassReservation reservation = classReservationService.getReservationById(reservationId);
        return studentService.addReservationTo(student, reservation);
    }
}
