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
@CrossOrigin(origins = "*")
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/all")
    public ResponseEntity<String> getAllStudents() throws JsonProcessingException {
        List<Student> students = studentService.getAllStudents();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        if (students.isEmpty()) {
            return new ResponseEntity<>("No students found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(students));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getStudentById(@PathVariable Long id) throws JsonProcessingException {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            return new ResponseEntity<>("Student could not be found", HttpStatus.NOT_FOUND);
        }
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(student));
    }

}
