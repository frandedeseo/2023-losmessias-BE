package com.losmessias.leherer.service;

import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.Student;
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

    public Professor saveProfessor(Professor professor) {
        return professorRepository.save(professor);
    }

    public Professor getProfessorById(Long id) {
        return professorRepository.findById(id).orElse(null);
    }

    public void removeFeedbackFromConcludedClass(Long professorId, List<ClassReservation> classes) {
        Professor professor = getProfessorById(professorId);
        professor.removePendingClassFeedback(classes);
        professorRepository.save(professor);
    }

}