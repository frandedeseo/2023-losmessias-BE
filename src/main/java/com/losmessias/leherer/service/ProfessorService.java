package com.losmessias.leherer.service;

import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorService {

    private final ProfessorRepository professorRepository;

    public List<Professor> getAllProfessors() {
        return professorRepository.findAll();
    }

    public Professor addSubjectTo(Professor professor, Subject subject) {
        professor.addSubject(subject);
        return professorRepository.save(professor);
    }

}