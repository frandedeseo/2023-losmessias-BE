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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/professor-subject")
@RequiredArgsConstructor
public class ProfessorSubjectController {

    private final ProfessorSubjectService professorSubjectService;
    private final ProfessorService professorService;
    private final SubjectService subjectService;

    @GetMapping("/all")
    public List<ProfessorSubject> getProfessorSubject() {
        return professorSubjectService.getAllProfessorSubjects();
    }

    @GetMapping("/{id}")
    public ProfessorSubject getProfessorSubjectById(@PathVariable Long id) {
        return professorSubjectService.findById(id);
    }

    @PostMapping("/createAssociation")
    public ProfessorSubject createProfessorSubject(Long professorId, Long subjectId) {
        Professor professor = professorService.getProfessorById(professorId);
        Subject subject = subjectService.getSubjectById(subjectId);
        return professorSubjectService.createAssociation(professor, subject);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/approve") //refactor this to a single method
    public List<ProfessorSubject> approve(@RequestBody SubjectRequestDto subjectRequestDto) {
        return subjectRequestDto
                .getSubjectIds()
                .stream()
                .map(subjectId -> {
                    Professor professor = professorService.getProfessorById(subjectRequestDto.getProfessorId());
                    Subject subject = subjectService.getSubjectById(subjectId);
                    ProfessorSubject professorSubject = professorSubjectService.findByProfessorAndSubject(professor, subject);
                    return professorSubjectService.changeStatusOf(professorSubject.getId(), SubjectStatus.APPROVED);
                })
                .collect(Collectors.toList());
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/reject")
    public List<ProfessorSubject> reject(@RequestBody SubjectRequestDto subjectRequestDto) {
        return subjectRequestDto
                .getSubjectIds()
                .stream()
                .map(subjectId -> {
                    Professor professor = professorService.getProfessorById(subjectRequestDto.getProfessorId());
                    Subject subject = subjectService.getSubjectById(subjectId);
                    ProfessorSubject professorSubject = professorSubjectService.findByProfessorAndSubject(professor, subject);
                    return professorSubjectService.changeStatusOf(professorSubject.getId(), SubjectStatus.REJECTED);
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/findByProfessor/{id}")
    public List<ProfessorSubject> findByProfessor(@PathVariable Long id) {
        Professor professor = professorService.getProfessorById(id);
        return professorSubjectService.findByProfessor(professor);
    }

    @GetMapping("/findByStatus")
    public List<ProfessorSubject> findPendingValidation(SubjectStatus status) {
        return professorSubjectService.findByStatus(status);
    }


}
