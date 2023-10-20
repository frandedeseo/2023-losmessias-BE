package com.losmessias.leherer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.losmessias.leherer.domain.Professor;
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

    @PostMapping("/register")
    public ResponseEntity<String> registerProfessor(@RequestBody Professor professor) throws JsonProcessingException {
        if (professor.getId() != null) {
            return ResponseEntity.badRequest().body("Professor ID must be null");
        }
        Professor professorSaved = professorService.saveProfessor(professor);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return new ResponseEntity<>(converter.getObjectMapper().writeValueAsString(professorSaved), HttpStatus.CREATED);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<String> updateProfessor(@PathVariable Long id, @RequestBody Professor professor) throws JsonProcessingException {
        if (id == null) {
            return ResponseEntity.badRequest().body("Professor ID not registered");
        } else if (professorService.getProfessorById(id) == null) {
            return ResponseEntity.badRequest().body("Professor not found");
        }
        Professor professorSaved = professorService.updateProfessor(id, professor);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(professorSaved));
    }
}