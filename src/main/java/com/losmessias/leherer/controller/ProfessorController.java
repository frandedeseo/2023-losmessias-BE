package com.losmessias.leherer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.service.ClassReservationService;
import com.losmessias.leherer.service.JwtService;
import com.losmessias.leherer.service.ProfessorService;
import com.losmessias.leherer.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.json.Jackson2CodecSupport;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/professor")
@RequiredArgsConstructor
public class ProfessorController {

    private final ProfessorService professorService;
    private final ClassReservationService classReservationService;
    private final JwtService jwtService;

    @GetMapping("/all")
    public ResponseEntity<String> getProfessor() throws JsonProcessingException {
        List<Professor> professors = professorService.getAllProfessors();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        if (professors.isEmpty()) {
            return new ResponseEntity<>("No professors found", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(professors));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getProfessorById(@PathVariable Long id) throws JsonProcessingException {
        Professor professor = professorService.getProfessorById(id);
        if (professor == null) {
            return new ResponseEntity<>("Professor could not be found", HttpStatus.NOT_FOUND);
        }
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(professor));
    }

    @PostMapping("/removeFeedback/professor={professorId}&student={studentId}")
    public ResponseEntity<String>  removeFeedbackFromConcludedClass(HttpServletRequest request, @PathVariable Long professorId, @PathVariable Long studentId) throws JsonProcessingException {
        ResponseEntity<Long> userIdResponse = JwtUtil.extractUserIdFromRequest(request, jwtService);
        if (!userIdResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(userIdResponse.getStatusCode()).body("Invalid token or user ID not found");
        }
        Long userId = userIdResponse.getBody();
        if (!Objects.equals(userId, professorId)){
            return ResponseEntity.status(userIdResponse.getStatusCode()).body("User doesn't match with professor");
        }
        classReservationService.removeFeedbackFromConcludedClass(professorId, studentId);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString("Status: 200"));

    }

}