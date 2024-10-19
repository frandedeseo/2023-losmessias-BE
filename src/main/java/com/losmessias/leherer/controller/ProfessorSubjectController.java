package com.losmessias.leherer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.ProfessorSubject;
import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.domain.enumeration.SubjectStatus;
import com.losmessias.leherer.dto.SubjectRequestDto;
import com.losmessias.leherer.service.*;
import com.losmessias.leherer.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/professor-subject")
@RequiredArgsConstructor
public class ProfessorSubjectController {

    private final ProfessorSubjectService professorSubjectService;
    private final ProfessorService professorService;
    private final SubjectService subjectService;
    private final NotificationService notificationService;
    private final JwtService jwtService;

    @PostMapping("/createAssociation")
    public ResponseEntity<String> createProfessorSubject(Long professorId, Long subjectId, Double price) {
        Professor professor = professorService.getProfessorById(professorId);
        if (professor == null) return new ResponseEntity<>("No professor found", HttpStatus.NOT_FOUND);
        Subject subject = subjectService.getSubjectById(subjectId);
        if (subject == null) return new ResponseEntity<>("No subject found", HttpStatus.NOT_FOUND);
        try {
            professorSubjectService.createAssociation(professor, subject, price);
            return new ResponseEntity<>("Professor-subject created", HttpStatus.CREATED);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/edit-price")
    public ResponseEntity<String> editPrice(HttpServletRequest request, @RequestParam("id") Long id, @RequestParam("price") Double price) throws JsonProcessingException {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        try {
            ResponseEntity<Long> userIdResponse = JwtUtil.extractUserIdFromRequest(request, jwtService);
            if (!userIdResponse.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(userIdResponse.getStatusCode()).body("Invalid token or user ID not found");
            }
            Long userId = userIdResponse.getBody();
            return new ResponseEntity<>(converter.getObjectMapper().writeValueAsString(professorSubjectService.editPrice(userId, id, price)), HttpStatus.OK);
        }catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //TODO Hacer bienn las validaciones
    @PostMapping("/approve")
    public ResponseEntity<String> approve(@RequestBody SubjectRequestDto subjectRequestDto) throws JsonProcessingException {
        Professor professor = professorService.getProfessorById(subjectRequestDto.getProfessorId());
        if (professor == null) return new ResponseEntity<>("No professor found", HttpStatus.NOT_FOUND);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return new ResponseEntity<>(converter.getObjectMapper().writeValueAsString(professorSubjectService.changeStatus(subjectRequestDto, SubjectStatus.APPROVED)), HttpStatus.OK);
    }

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

        List<ProfessorSubject> rejectedSubjects = new ArrayList<>();
        for (Subject subject : subjects) {
            ProfessorSubject professorSubject = professorSubjectService.findByProfessorAndSubject(professor, subject);
            if (professorSubject != null)
                rejectedSubjects.add(professorSubjectService.changeStatusOf(professorSubject.getId(), SubjectStatus.REJECTED));
        }
        if (rejectedSubjects.isEmpty())
            return new ResponseEntity<>("No professor-subjects found", HttpStatus.NOT_FOUND);

        notificationService.lecturedRejectedByAdminNotification(rejectedSubjects);

        return new ResponseEntity<>(converter.getObjectMapper().writeValueAsString(rejectedSubjects), HttpStatus.OK);
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
