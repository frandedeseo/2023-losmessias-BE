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

    public Professor saveProfessor(Professor professor) {
        return professorRepository.save(professor);
    }

    public Professor getProfessorById(Long id) {
        return professorRepository.findById(id).orElse(null);
    }

    public Professor updateProfessor(Long id, Professor professor) {
        Professor professorToUpdate = professorRepository.findById(id).orElse(null);
        professorToUpdate.setFirstName(professor.getFirstName() != null ? professor.getFirstName() : professorToUpdate.getFirstName());
        professorToUpdate.setLastName(professor.getLastName() != null ? professor.getLastName() : professorToUpdate.getLastName());
        professorToUpdate.setSubjects(professor.getSubjects() != null ? professor.getSubjects() : professorToUpdate.getSubjects());
        professorToUpdate.setEmail(professor.getEmail() != null ? professor.getEmail() : professorToUpdate.getEmail());
        professorToUpdate.setPhone(professor.getPhone() != null ? professor.getPhone() : professorToUpdate.getPhone());
        professorToUpdate.setLocation(professor.getLocation() != null ? professor.getLocation() : professorToUpdate.getLocation());
        return professorRepository.save(professorToUpdate);
    }
}