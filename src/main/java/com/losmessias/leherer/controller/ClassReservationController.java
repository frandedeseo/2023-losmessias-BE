package com.losmessias.leherer.controller;

import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.dto.ClassReservationDto;
import com.losmessias.leherer.service.ClassReservationService;
import com.losmessias.leherer.service.ProfessorService;
import com.losmessias.leherer.service.StudentService;
import com.losmessias.leherer.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ClassReservationController {

    private final ClassReservationService classReservationService;
    private final StudentService studentService;
    private final SubjectService subjectService;
    private final ProfessorService professorService;

    @GetMapping("/all")
    public List<ClassReservationDto> getAllReservations2() {
        List<ClassReservation> classReservations = classReservationService.getAllReservations();
        return classReservations
                .stream()
                .map(classReservation -> new ClassReservationDto(
                        classReservation.getProfessor().getId(),
                        classReservation.getSubject().getId(),
                        classReservation.getStudent().getId(),
                        classReservation.getDate(),
                        classReservation.getStartingHour(),
                        classReservation.getEndingHour(),
                        classReservation.getPrice()
                )).toList();
    }

    @GetMapping("/{id}")
    public ClassReservationDto getReservationById(@PathVariable Long id) {
        ClassReservation classReservation = classReservationService.getReservationById(id);
        return new ClassReservationDto(
                classReservation.getProfessor().getId(),
                classReservation.getSubject().getId(),
                classReservation.getStudent().getId(),
                classReservation.getDate(),
                classReservation.getStartingHour(),
                classReservation.getEndingHour(),
                classReservation.getPrice()
        );
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/create")
    public ClassReservation createReservation(@RequestBody ClassReservationDto classReservationDto) {
        Professor professor = professorService.getProfessorById(classReservationDto.getProfessorId());
        Subject subject = subjectService.getSubjectById(classReservationDto.getSubjectId());
        Student student = studentService.getStudentById(classReservationDto.getStudentId());

        return classReservationService.createReservation(
                professor,
                subject,
                student,
                classReservationDto.getDay(),
                classReservationDto.getStartingHour(),
                classReservationDto.getEndingHour(),
                classReservationDto.getPrice());
    }
}
