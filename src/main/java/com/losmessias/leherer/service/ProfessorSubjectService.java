package com.losmessias.leherer.service;

import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.ProfessorSubject;
import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.domain.enumeration.SubjectStatus;
import com.losmessias.leherer.dto.SubjectRequestDto;
import com.losmessias.leherer.repository.ProfessorSubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessorSubjectService {

    private final ProfessorSubjectRepository professorSubjectRepository;
    private final SubjectService subjectService;
    private final ProfessorService professorService;
    private final NotificationService notificationService;

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

    public ProfessorSubject findByProfessorAndSubject(Professor professor, Subject subject) {
        return professorSubjectRepository.findByProfessorIdAndSubject_Id(professor.getId(), subject.getId());
    }

    public List<ProfessorSubject> findByStatus(SubjectStatus status) {
        return professorSubjectRepository.findByStatus(status);
    }

    public List<ProfessorSubject> changeStatus(SubjectRequestDto subjectRequestDto, SubjectStatus status) {

        Professor professor = professorService.getProfessorById(subjectRequestDto.getProfessorId());
        List<ProfessorSubject> subjects = new ArrayList<>();

        for (Long subjectId : subjectRequestDto.getSubjectIds()) {
            Subject subject = subjectService.getSubjectById(subjectId);
            ProfessorSubject professorSubject = findByProfessorAndSubject(professor, subject);
            subjects.add(changeStatusOf(professorSubject.getId(), status));
        }
        if (status==SubjectStatus.APPROVED) {
            notificationService.lecturedApprovedByAdminNotification(subjects);
        } else {
            notificationService.lecturedRejectedByAdminNotification(subjects);
        }

        return subjects;
    }
}
