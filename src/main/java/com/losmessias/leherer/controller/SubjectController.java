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
@RequestMapping("/api/subject")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping("/all")
    public ResponseEntity<String> getSubject() throws JsonProcessingException {
        List<Subject> subjects = subjectService.getAllSubjects();
        if(subjects.isEmpty()) return new ResponseEntity<>("No subjects found", HttpStatus.NOT_FOUND);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return new ResponseEntity<>(converter.getObjectMapper().writeValueAsString(subjects), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getSubjectById(@PathVariable Long id) throws JsonProcessingException {
        Subject subject = subjectService.getSubjectById(id);
        if(subject == null) return new ResponseEntity<>("No subject found", HttpStatus.NOT_FOUND);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return new ResponseEntity<>(converter.getObjectMapper().writeValueAsString(subject), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<String> createSubject(@RequestBody Subject subject) throws JsonProcessingException {
        if(subject.getId() != null) return ResponseEntity.badRequest().body("Subject ID must be null");
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return new ResponseEntity<>(converter.getObjectMapper().writeValueAsString(subjectService.create(subject)), HttpStatus.CREATED);
    }
}