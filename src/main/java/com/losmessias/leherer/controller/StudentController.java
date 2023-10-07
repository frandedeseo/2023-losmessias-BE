package com.losmessias.leherer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.service.ClassReservationService;
import com.losmessias.leherer.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
    public ResponseEntity<String> getAllStudents() throws JsonProcessingException {
        List<Student> students = studentService.getAllStudents();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        if (students.isEmpty()) {
            return new ResponseEntity<>("No students found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(students));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{id}")
    public ResponseEntity<String> getStudentById(@PathVariable Long id) throws JsonProcessingException {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            return new ResponseEntity<>("Student could not be found", HttpStatus.NOT_FOUND);
        }
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(student));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/create")
    public ResponseEntity<String> addStudent(@RequestBody Student student) throws JsonProcessingException {
        if (student.getId() != null) {
            return ResponseEntity.badRequest().body("Student ID must be null");
        }
        Student studentSaved = studentService.create(student);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return new ResponseEntity<>(converter.getObjectMapper().writeValueAsString(studentSaved), HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/addReservation")
    public ResponseEntity<String> addReservationToStudent(Long studentId, Long reservationId) throws JsonProcessingException {
        Student student = studentService.getStudentById(studentId);
        if (student == null) return ResponseEntity.badRequest().body("Student not found");
        ClassReservation reservation = classReservationService.getReservationById(reservationId);
        if (reservation == null) return ResponseEntity.badRequest().body("Reservation not found");
        Student studentSaved = studentService.addReservationTo(student, reservation);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(studentSaved));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PatchMapping("/update/{id}")
    public ResponseEntity<String> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        if (id == null) {
            return ResponseEntity.badRequest().body("Student ID not registered");
        } else if (studentService.getStudentById(id) == null) {
            return ResponseEntity.badRequest().body("Student not found");
        }
        Student studentSaved = studentService.updateStudent(id, student);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().valueToTree(studentSaved).toString());
    }
}
