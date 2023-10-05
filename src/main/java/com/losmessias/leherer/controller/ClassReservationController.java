package com.losmessias.leherer.controller;

import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.dto.ClassReservationDto;
import com.losmessias.leherer.dto.ClassReservationResponseDto;
import com.losmessias.leherer.dto.UnavailableClassReservationDto;
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
    public List<ClassReservationResponseDto> getAllReservations() {
        List<ClassReservation> classReservations = classReservationService.getAllReservations();
        return classReservations
                .stream()
                .map(classReservation -> new ClassReservationResponseDto(
                        classReservation.getId(),
                        classReservation.getProfessor().getId(),
                        classReservation.getSubject() == null ? null : classReservation.getSubject().getId(),
                        classReservation.getStudent() == null ? null : classReservation.getStudent().getId(),
                        classReservation.getDate(),
                        classReservation.getStartingHour(),
                        classReservation.getEndingHour(),
                        classReservation.getPrice() == null ? null : classReservation.getPrice(),
                        classReservation.getStatus().toString()
                )).toList();
    }

    @GetMapping("/{id}")
    public ClassReservationResponseDto getReservationById(@PathVariable Long id) {
        ClassReservation classReservation = classReservationService.getReservationById(id);
        return new ClassReservationResponseDto(
                classReservation.getId(),
                classReservation.getProfessor().getId(),
                classReservation.getSubject() == null ? null : classReservation.getSubject().getId(),
                classReservation.getStudent() == null ? null : classReservation.getStudent().getId(),
                classReservation.getDate(),
                classReservation.getStartingHour(),
                classReservation.getEndingHour(),
                classReservation.getPrice() == null ? null : classReservation.getPrice(),
                classReservation.getStatus().toString()
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

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/createUnavailable")
    public ClassReservation createUnavailableReservation(@RequestBody UnavailableClassReservationDto classReservationDto) {
        Professor professor = professorService.getProfessorById(classReservationDto.getProfessorId());
        return classReservationService.createUnavailableReservation(
                professor,
                classReservationDto.getDay(),
                classReservationDto.getStartingHour(),
                classReservationDto.getEndingHour());
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/createMultipleUnavailable")
    public List<ClassReservation> createMultipleUnavailableReservations(@RequestBody UnavailableClassReservationDto classReservationDtos) {
        Professor professor = professorService.getProfessorById(classReservationDtos.getProfessorId());
        return classReservationService.createMultipleUnavailableReservationsFor(
                professor,
                classReservationDtos.getDay(),
                classReservationDtos.getStartingHour(),
                classReservationDtos.getEndingHour());
    }
}
