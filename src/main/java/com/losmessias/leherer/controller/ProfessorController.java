package com.losmessias.leherer.controller;

import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.service.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professor")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;

    @GetMapping("/all")
    public List<Professor> getProfessor() {
        return professorService.getAllProfessors();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{id}")
    public Professor getProfessorById(@PathVariable Long id) {
        return professorService.getProfessorById(id);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/register")
    public ResponseEntity<String> registerProfessor(@RequestBody Professor professor) {
        if (professor.getId() != null) {
            return new ResponseEntity<>("Professor already registered", org.springframework.http.HttpStatus.BAD_REQUEST);
        }
        professorService.saveProfessor(professor);
        String response = "Professor " + professor.getFirstName() + " " + professor.getLastName() + " registered";
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PatchMapping("/update/{id}")
    public ResponseEntity<String> updateProfessor(@PathVariable Long id, @RequestBody Professor professor) {
        if (id == null) {
            return new ResponseEntity<>("Professor ID not registered", org.springframework.http.HttpStatus.BAD_REQUEST);
        } else if (professorService.getProfessorById(id) == null) {
            return new ResponseEntity<>("Professor not found", org.springframework.http.HttpStatus.NOT_FOUND);
        }

        Professor professorSaved = professorService.updateProfessor(id, professor);
        return new ResponseEntity<>(professorSaved.toJson(), HttpStatus.OK);
    }
}