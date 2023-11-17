package com.losmessias.leherer.service;

import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.ProfessorSubject;
import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.domain.enumeration.SubjectStatus;
import com.losmessias.leherer.repository.ProfessorSubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorSubjectService {

    private final ProfessorSubjectRepository professorSubjectRepository;

    public List<ProfessorSubject> getAllProfessorSubjects() {
        return professorSubjectRepository.findAll();
    }

    public ProfessorSubject createAssociation(Professor professor, Subject subject) {
        ProfessorSubject professorSubject = new ProfessorSubject(professor, subject);
        return professorSubjectRepository.save(professorSubject);
    }

    public ProfessorSubject changeStatusOf(Long id, SubjectStatus status) {
        ProfessorSubject professorSubject = professorSubjectRepository.findById(id).orElse(null);
        if (professorSubject == null) {
            throw new RuntimeException("ProfessorSubject with id " + id + " not found");
        }
        professorSubject.setStatus(status);
        return professorSubjectRepository.save(professorSubject);
    }

    public ProfessorSubject findById(Long id) {
        return professorSubjectRepository.findById(id).orElse(null);
    }

    public List<ProfessorSubject> findByProfessor(Professor professor) {
        return professorSubjectRepository.findByProfessorId(professor.getId());
    }

    public ProfessorSubject findByProfessorAndSubject(Professor professor, Subject subject) {
        return professorSubjectRepository.findByProfessorIdAndSubject_Id(professor.getId(), subject.getId());
    }

    public List<ProfessorSubject> findByStatus(SubjectStatus status) {
        return professorSubjectRepository.findByStatus(status);
    }

    public List<ProfessorSubject> findByProfessorIdAndStatus(Long professorId, SubjectStatus status) {
        return professorSubjectRepository.findByProfessorIdAndStatus(professorId, status);
    }
}
