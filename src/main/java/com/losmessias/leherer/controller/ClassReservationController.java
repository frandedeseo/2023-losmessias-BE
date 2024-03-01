package com.losmessias.leherer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.dto.ClassReservationCancelDto;
import com.losmessias.leherer.dto.ClassReservationDto;
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
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ClassReservationController {

    private final ClassReservationService classReservationService;
    private final StudentService studentService;
    private final SubjectService subjectService;
    private final ProfessorService professorService;

    @GetMapping("/{id}")
    public ResponseEntity<String> getReservationById(@PathVariable Long id) throws JsonProcessingException {
        ClassReservation classReservation = classReservationService.getReservationById(id);
        if (classReservation == null)
            return new ResponseEntity<>("Reservation could not be found", HttpStatus.NOT_FOUND);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(classReservation));
    }

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
        if (!student.canMakeAReservation())
            return new ResponseEntity<>("Student must give feedback before making a reservation", HttpStatus.BAD_REQUEST);
        //TODO Esto podría ir en la Class Reservation Entity?
        if (classReservationService.existsReservationForProfessorOnDayAndTime(
                classReservationDto.getProfessorId(),
                classReservationDto.getDay(),
                classReservationDto.getStartingHour(),
                classReservationDto.getEndingHour()))
            return ResponseEntity.badRequest().body("There is already a class reserved for this professor at this time");

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(
            converter.getObjectMapper().writeValueAsString(classReservationService.createReservation(
                    professor,
                    subject,
                    student,
                    classReservationDto.getDay(),
                    classReservationDto.getStartingHour(),
                    classReservationDto.getEndingHour(),
                    classReservationDto.getPrice()
                )
            )
        );
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelReservation(@RequestBody ClassReservationCancelDto classReservationCancelDto) throws JsonProcessingException {
        if (classReservationCancelDto.getId() == null) return ResponseEntity.badRequest().body("Class Reservation id must be provided");
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(classReservationService.cancelReservation(classReservationCancelDto)));
    }

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

/*    @PostMapping("/createMultipleUnavailable")
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
                classReservationDtos.getEndingHour())));
    }*/

    @GetMapping("/todaySummary")
    public ResponseEntity<String> getTodaySummary() throws JsonProcessingException {
        List<ProfessorDailySummary> professorDailySummaries = classReservationService.getDailySummary(LocalDate.now());
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(professorDailySummaries));
    }


    @GetMapping("/findByAppUserId")
    public ResponseEntity<String> getReservationsByAppUser(@RequestParam Long appUserId) throws JsonProcessingException {
        if (appUserId == null) return ResponseEntity.badRequest().body("AppUser id must be provided");
        List<ClassReservation> classReservations = classReservationService.getReservationsByAppUserId(appUserId);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(classReservations));
    }

    @GetMapping("/getStatistics")
    public ResponseEntity<String> getStatistics(@RequestParam Long professorId) throws JsonProcessingException {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(classReservationService.getStatics(professorId)));
    }

}
