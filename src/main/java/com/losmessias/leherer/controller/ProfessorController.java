package com.losmessias.leherer.controller;

import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.service.ProfessorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/professor")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;

    @GetMapping
    public List<Professor> getProfessor() { // retornar un DTO
        return professorService.getAllProfessors();
    }

    @PostMapping("/register")
    public Professor registerProfessor(@RequestBody Professor professor) {
        System.out.println("Professor: " + professor.toString());
        return professorService.saveProfessor(professor);
    }
}