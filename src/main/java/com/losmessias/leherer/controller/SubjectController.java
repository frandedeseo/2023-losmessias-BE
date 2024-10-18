package com.losmessias.leherer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/subject")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping("/all")
    public ResponseEntity<String> getSubject() throws JsonProcessingException {
        List<Subject> subjects = subjectService.getAllSubjects();
        if (subjects.isEmpty()) return new ResponseEntity<>("No subjects found", HttpStatus.NOT_FOUND);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return new ResponseEntity<>(converter.getObjectMapper().writeValueAsString(subjects), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createSubject(@RequestBody Subject subject) throws JsonProcessingException {
        if (subject.getId() != null) return ResponseEntity.badRequest().body("Subject ID must be null");
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return new ResponseEntity<>(converter.getObjectMapper().writeValueAsString(subjectService.create(subject)), HttpStatus.CREATED);
    }

    @PostMapping("/edit-price")
    public ResponseEntity<String> editPrice(@RequestParam("id") Long id, @RequestParam("price") Double price) throws JsonProcessingException {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        try {
            return new ResponseEntity<>(converter.getObjectMapper().writeValueAsString(subjectService.editPrice(id, price)), HttpStatus.OK);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}