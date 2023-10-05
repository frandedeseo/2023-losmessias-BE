package com.losmessias.leherer.controller;

import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.service.ClassReservationService;
import com.losmessias.leherer.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final ClassReservationService classReservationService;

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/all")
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/create")
    public Student addStudent(@RequestBody Student student) {
        return studentService.create(student);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/addReservation")
    public Student addReservationToStudent(Long studentId, Long reservationId) {
        Student student = studentService.getStudentById(studentId);
        ClassReservation reservation = classReservationService.getReservationById(reservationId);
        return studentService.addReservationTo(student, reservation);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PatchMapping("/update/{id}")
    public ResponseEntity<String> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        if (id == null) {
            return new ResponseEntity<>("Student ID not registered", org.springframework.http.HttpStatus.BAD_REQUEST);
        } else if (studentService.getStudentById(id) == null) {
            return new ResponseEntity<>("Student not found", org.springframework.http.HttpStatus.NOT_FOUND);
        }

        Student studentSaved = studentService.updateStudent(id, student);
        return new ResponseEntity<>(studentSaved.toJson(), org.springframework.http.HttpStatus.OK);
    }
}
