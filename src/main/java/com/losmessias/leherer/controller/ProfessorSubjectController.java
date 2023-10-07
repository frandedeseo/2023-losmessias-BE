package com.losmessias.leherer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.ProfessorSubject;
import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.domain.enumeration.SubjectStatus;
import com.losmessias.leherer.dto.SubjectRequestDto;
import com.losmessias.leherer.service.ProfessorService;
import com.losmessias.leherer.service.ProfessorSubjectService;
import com.losmessias.leherer.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
    public ResponseEntity<String> getProfessorSubject() throws JsonProcessingException {
        List<ProfessorSubject> professorSubjects = professorSubjectService.getAllProfessorSubjects();
        if (professorSubjects.isEmpty())
            return new ResponseEntity<>("No professor-subjects found", HttpStatus.NOT_FOUND);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return new ResponseEntity<>(converter.getObjectMapper().writeValueAsString(professorSubjects), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProfessorSubjectById(@PathVariable Long id) throws JsonProcessingException {
        ProfessorSubject professorSubject = professorSubjectService.findById(id);
        if (professorSubject == null) return new ResponseEntity<>("No professor-subject found", HttpStatus.NOT_FOUND);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return new ResponseEntity<>(converter.getObjectMapper().writeValueAsString(professorSubject), HttpStatus.OK);
    }

    @PostMapping("/createAssociation")
    public ResponseEntity<String> createProfessorSubject(Long professorId, Long subjectId) {
        Professor professor = professorService.getProfessorById(professorId);
        if (professor == null) return new ResponseEntity<>("No professor found", HttpStatus.NOT_FOUND);
        Subject subject = subjectService.getSubjectById(subjectId);
        if (subject == null) return new ResponseEntity<>("No subject found", HttpStatus.NOT_FOUND);
        ProfessorSubject professorSubject = professorSubjectService.createAssociation(professor, subject);
        return new ResponseEntity<>("Professor-subject created", HttpStatus.CREATED);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/approve")
    public ResponseEntity<String> approve(@RequestBody SubjectRequestDto subjectRequestDto) throws JsonProcessingException {
        Professor professor = professorService.getProfessorById(subjectRequestDto.getProfessorId());
        if (professor == null) return new ResponseEntity<>("No professor found", HttpStatus.NOT_FOUND);

        List<Subject> subjects = new ArrayList<>();
        for (Long subjectId : subjectRequestDto.getSubjectIds()) {
            Subject subject = subjectService.getSubjectById(subjectId);
            if (subject != null) subjects.add(subject);
        }
        if (subjects.isEmpty()) return new ResponseEntity<>("No subjects found", HttpStatus.NOT_FOUND);

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        List<ProfessorSubject> approvedSubjects = new ArrayList<>();
        for (Subject subject : subjects) {
            ProfessorSubject professorSubject = professorSubjectService.findByProfessorAndSubject(professor, subject);
            if (professorSubject != null)
                approvedSubjects.add(professorSubjectService.changeStatusOf(professorSubject.getId(), SubjectStatus.APPROVED));
        }
        if (approvedSubjects.isEmpty())
            return new ResponseEntity<>("No professor-subjects found", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(converter.getObjectMapper().writeValueAsString(approvedSubjects), HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/reject")
    public ResponseEntity<String> reject(@RequestBody SubjectRequestDto subjectRequestDto) throws JsonProcessingException {
        Professor professor = professorService.getProfessorById(subjectRequestDto.getProfessorId());
        if (professor == null) return new ResponseEntity<>("No professor found", HttpStatus.NOT_FOUND);

        List<Subject> subjects = new ArrayList<>();
        for (Long subjectId : subjectRequestDto.getSubjectIds()) {
            Subject subject = subjectService.getSubjectById(subjectId);
            if (subject != null) subjects.add(subject);
        }
        if (subjects.isEmpty()) return new ResponseEntity<>("No subjects found", HttpStatus.NOT_FOUND);

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        List<ProfessorSubject> approvedSubjects = new ArrayList<>();
        for (Subject subject : subjects) {
            ProfessorSubject professorSubject = professorSubjectService.findByProfessorAndSubject(professor, subject);
            if (professorSubject != null)
                approvedSubjects.add(professorSubjectService.changeStatusOf(professorSubject.getId(), SubjectStatus.REJECTED));
        }
        if (approvedSubjects.isEmpty())
            return new ResponseEntity<>("No professor-subjects found", HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(converter.getObjectMapper().writeValueAsString(approvedSubjects), HttpStatus.OK);
    }

    @GetMapping("/findByProfessor/{id}")
    public ResponseEntity<String> findByProfessor(@PathVariable Long id) throws JsonProcessingException {
        Professor professor = professorService.getProfessorById(id);
        if (professor == null) return new ResponseEntity<>("No professor found", HttpStatus.NOT_FOUND);
        List<ProfessorSubject> professorSubjects = professorSubjectService.findByProfessor(professor);
        if (professorSubjects.isEmpty())
            return new ResponseEntity<>("No professor-subjects found", HttpStatus.NOT_FOUND);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return new ResponseEntity<>(converter.getObjectMapper().writeValueAsString(professorSubjects), HttpStatus.OK);
    }

    @GetMapping("/findByStatus")
    public ResponseEntity<String> findPendingValidation(SubjectStatus status) throws JsonProcessingException {
        List<ProfessorSubject> professorSubjects = professorSubjectService.findByStatus(status);
        if (professorSubjects.isEmpty())
            return new ResponseEntity<>("No professor-subjects found", HttpStatus.NOT_FOUND);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return new ResponseEntity<>(converter.getObjectMapper().writeValueAsString(professorSubjects), HttpStatus.OK);
    }


}
