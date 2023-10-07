package com.losmessias.leherer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.dto.ClassReservationDto;
import com.losmessias.leherer.dto.ClassReservationResponseDto;
import com.losmessias.leherer.dto.UnavailableClassReservationDto;
import com.losmessias.leherer.repository.interfaces.ProfessorDailySummary;
import com.losmessias.leherer.service.ClassReservationService;
import com.losmessias.leherer.service.ProfessorService;
import com.losmessias.leherer.service.StudentService;
import com.losmessias.leherer.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    public ResponseEntity<String> getAllReservations() throws JsonProcessingException {
        List<ClassReservation> classReservations = classReservationService.getAllReservations();
        if (classReservations.isEmpty())
            return new ResponseEntity<>("No reservations found", HttpStatus.NOT_FOUND);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        List<ClassReservationResponseDto> classReservationResponseDtos = classReservations
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
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(classReservationResponseDtos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getReservationById(@PathVariable Long id) throws JsonProcessingException {
        ClassReservation classReservation = classReservationService.getReservationById(id);
        if (classReservation == null)
            return new ResponseEntity<>("Reservation could not be found", HttpStatus.NOT_FOUND);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(new ClassReservationResponseDto(
                classReservation.getId(),
                classReservation.getProfessor().getId(),
                classReservation.getSubject() == null ? null : classReservation.getSubject().getId(),
                classReservation.getStudent() == null ? null : classReservation.getStudent().getId(),
                classReservation.getDate(),
                classReservation.getStartingHour(),
                classReservation.getEndingHour(),
                classReservation.getPrice() == null ? null : classReservation.getPrice(),
                classReservation.getStatus().toString()
        )));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/create")
    public ResponseEntity<String> createReservation(@RequestBody ClassReservationDto classReservationDto) throws JsonProcessingException {
        if (classReservationDto.getProfessorId() == null)
            return ResponseEntity.badRequest().body("Professor id must be provided");
        if (classReservationDto.getSubjectId() == null)
            return ResponseEntity.badRequest().body("Subject id must be provided");
        if (classReservationDto.getStudentId() == null)
            return ResponseEntity.badRequest().body("Student id must be provided");
        Professor professor = professorService.getProfessorById(classReservationDto.getProfessorId());
        Subject subject = subjectService.getSubjectById(classReservationDto.getSubjectId());
        Student student = studentService.getStudentById(classReservationDto.getStudentId());
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(classReservationService.createReservation(
                    professor,
                    subject,
                    student,
                    classReservationDto.getDay(),
                    classReservationDto.getStartingHour(),
                    classReservationDto.getEndingHour(),
                    classReservationDto.getDuration(),
                    classReservationDto.getPrice())));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/createUnavailable")
    public ResponseEntity<String> createUnavailableReservation(@RequestBody UnavailableClassReservationDto classReservationDto) throws JsonProcessingException {
        if (classReservationDto.getProfessorId() == null)
            return ResponseEntity.badRequest().body("Professor id must be provided");
        Professor professor = professorService.getProfessorById(classReservationDto.getProfessorId());
        if (professor == null) return new ResponseEntity<>("Professor could not be found", HttpStatus.NOT_FOUND);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(classReservationService.createUnavailableReservation(
                professor,
                classReservationDto.getDay(),
                classReservationDto.getStartingHour(),
                classReservationDto.getEndingHour())));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/createMultipleUnavailable")
    public ResponseEntity<String> createMultipleUnavailableReservations(@RequestBody UnavailableClassReservationDto classReservationDtos) throws JsonProcessingException {
        if (classReservationDtos.getProfessorId() == null) return ResponseEntity.badRequest().body("Professor id must be provided");
        if (classReservationDtos.getDay() == null) return ResponseEntity.badRequest().body("Day must be provided");
        if (classReservationDtos.getStartingHour() == null) return ResponseEntity.badRequest().body("Starting hour must be provided");
        if (classReservationDtos.getEndingHour() == null) return ResponseEntity.badRequest().body("Ending hour must be provided");
        if (classReservationDtos.getDuration() == null) return ResponseEntity.badRequest().body("Duration must be provided");
        Professor professor = professorService.getProfessorById(classReservationDtos.getProfessorId());
        if (professor == null) return new ResponseEntity<>("Professor could not be found", HttpStatus.NOT_FOUND);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(classReservationService.createMultipleUnavailableReservationsFor(
                professor,
                classReservationDtos.getDay(),
                classReservationDtos.getStartingHour(),
                classReservationDtos.getEndingHour(),
                classReservationDtos.getDuration())));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/customDaySummary")
    public ResponseEntity<String> getTodaySummary(LocalDate day) throws JsonProcessingException {
        if (day == null) {
            return ResponseEntity.badRequest().body("Day must be provided");
        }
        List<ProfessorDailySummary> professorDailySummaries = classReservationService.getDailySummary(day);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(professorDailySummaries));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/todaySummary")
    public ResponseEntity<String> getTodaySummary() throws JsonProcessingException {
        List<ProfessorDailySummary> professorDailySummaries = classReservationService.getDailySummary(LocalDate.now());
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(professorDailySummaries));
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/findByProfessorAndSubject")
    public ResponseEntity<String> getReservationByProfessorAndSubject(@RequestParam Long professorId, @RequestParam Long subjectId) throws JsonProcessingException {
        if(professorId == null) return ResponseEntity.badRequest().body("Professor id must be provided");
        if(subjectId == null) return ResponseEntity.badRequest().body("Subject id must be provided");
        List<ClassReservation> classReservations = classReservationService.getByProfessorAndSubject(professorId, subjectId);
        if (classReservations.isEmpty())
            return new ResponseEntity<>("No reservations found", HttpStatus.NOT_FOUND);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        List<ClassReservationResponseDto> classReservationResponseDtos = classReservations
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
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(classReservationResponseDtos));
    }
}
