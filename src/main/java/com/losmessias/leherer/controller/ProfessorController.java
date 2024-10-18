package com.losmessias.leherer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.service.ClassReservationService;
import com.losmessias.leherer.service.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/professor")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;
    private final ClassReservationService classReservationService;

    @GetMapping("/all")
    public ResponseEntity<String> getProfessor() throws JsonProcessingException {
        List<Professor> professors = professorService.getAllProfessors();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        if (professors.isEmpty()) {
            return new ResponseEntity<>("No professors found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(professors));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProfessorById(@PathVariable Long id) throws JsonProcessingException {
        Professor professor = professorService.getProfessorById(id);
        if (professor == null) {
            return new ResponseEntity<>("Professor could not be found", HttpStatus.NOT_FOUND);
        }
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(professor));
    }

    @PostMapping("/removeFeedback/professor={professorId}&student={studentId}")
    public void removeFeedbackFromConcludedClass(@PathVariable Long professorId, @PathVariable Long studentId) {
        classReservationService.removeFeedbackFromConcludedClass(professorId, studentId);
    }

}