package com.losmessias.leherer.service;

import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.ProfessorSubject;
import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.repository.ProfessorSubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorSubjectService {

    private final ProfessorSubjectRepository professorSubjectRepository;
    private final ProfessorService professorService;
    private final SubjectService subjectService;

    public List<ProfessorSubject> getAllProfessorSubjects() {
        return professorSubjectRepository.findAll();
    }

    public ProfessorSubject createAssociation(Professor professor, Subject subject) {
        ProfessorSubject professorSubject = new ProfessorSubject(professor, subject);
        return professorSubjectRepository.save(professorSubject);
    }
}
