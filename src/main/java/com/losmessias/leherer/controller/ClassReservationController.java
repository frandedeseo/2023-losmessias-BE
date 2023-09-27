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
    public List<ClassReservation> getAllReservations() {
        return classReservationService.getAllReservations();
    }

    @GetMapping("/{id}")
    public ClassReservation getReservationById(@PathVariable Long id) {
        return classReservationService.getReservationById(id);
    }

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
                classReservationDto.getStartingTime(),
                classReservationDto.getEndingTime(),
                classReservationDto.getPrice());
    }
}
