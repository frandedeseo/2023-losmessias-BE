package com.losmessias.leherer.controller;

import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.ProfessorSubject;
import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.domain.enumeration.SubjectStatus;
import com.losmessias.leherer.dto.SubjectRequestDto;
import com.losmessias.leherer.service.ProfessorService;
import com.losmessias.leherer.service.ProfessorSubjectService;
import com.losmessias.leherer.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/professor-subject")
@RequiredArgsConstructor
public class ProfessorSubjectController {

    private final ProfessorSubjectService professorSubjectService;
    private final ProfessorService professorService;
    private final SubjectService subjectService;

    @GetMapping
    public List<ProfessorSubject> getProfessorSubject() {
        return professorSubjectService.getAllProfessorSubjects();
    }

    @PostMapping("/createAssociation")
    public ProfessorSubject createProfessorSubject(Long professorId, Long subjectId) {
        System.out.println("professorId: " + professorId + " - subjectId: " + subjectId);
        Professor professor = professorService.getProfessorById(professorId);
        Subject subject = subjectService.getSubjectById(subjectId);
        System.out.println("professor: " + professor);
        System.out.println("subject: " + subject);
        return professorSubjectService.createAssociation(professor, subject);
    }

    @PostMapping("/approve") //refactor this to a single method
    public List<ProfessorSubject> approve(@RequestBody SubjectRequestDto subjectRequestDto) {
        List<ProfessorSubject> approvedProfessorSubjects = new ArrayList<>();
        Professor professor = professorService.getProfessorById(subjectRequestDto.getProfessorId());
        for (Long subjectId : subjectRequestDto.getSubjectIds()) {
            Subject subject = subjectService.getSubjectById(subjectId);
            ProfessorSubject professorSubject = professorSubjectService.findByProfessorAndSubject(professor, subject);
            ProfessorSubject approvedSubject = professorSubjectService
                    .changeStatusOf(
                            professorSubject.getId(),
                            SubjectStatus.APPROVED);
            approvedProfessorSubjects.add(approvedSubject);
        }
        return approvedProfessorSubjects;
    }

    @PostMapping("/reject")
    public List<ProfessorSubject> reject(@RequestBody SubjectRequestDto subjectRequestDto) {
        List<ProfessorSubject> rejectedProfessorSubjects = new ArrayList<>();
        Professor professor = professorService.getProfessorById(subjectRequestDto.getProfessorId());
        for (Long subjectId : subjectRequestDto.getSubjectIds()) {
            Subject subject = subjectService.getSubjectById(subjectId);
            ProfessorSubject professorSubject = professorSubjectService.findByProfessorAndSubject(professor, subject);
            ProfessorSubject rejectedSubject = professorSubjectService
                    .changeStatusOf(
                            professorSubject.getId(),
                            SubjectStatus.REJECTED);
            rejectedProfessorSubjects.add(rejectedSubject);
        }
        return rejectedProfessorSubjects;
    }

    @GetMapping("/findByProfessor")
    public List<ProfessorSubject> findByProfessor(Long professorId) {
        Professor professor = professorService.getProfessorById(professorId);
        return professorSubjectService.findByProfessor(professor);
    }

    @GetMapping("/findByStatus")
    public List<ProfessorSubject> findPendingValidation(SubjectStatus status){
        return professorSubjectService.findByStatus(status);
    }


}
