package com.losmessias.leherer.tasks;

import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.domain.enumeration.ReservationStatus;
import com.losmessias.leherer.repository.ClassReservationRepository;
import com.losmessias.leherer.repository.ProfessorRepository;
import com.losmessias.leherer.repository.StudentRepository;
import com.losmessias.leherer.service.ClassReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class ClassReservationsOrchestrator {

    private static final Logger log = Logger.getLogger(ClassReservationsOrchestrator.class.getName());
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy");
    private final ClassReservationService classReservationService;
    private final ClassReservationRepository classReservationRepository;
    private final StudentRepository studentRepository;
    private final ProfessorRepository professorRepository;

        @Scheduled(cron = "0 0/30 * * * *")
    public void reserveClasses() {
        // LocalDate -3 due to de Render Hosting time zone
        LocalDateTime endingTimeInGMT = LocalDateTime.now();
        LocalDateTime endingTimeInGMTMinus3 = convertToGMTMinus3(endingTimeInGMT);
        LocalDate endingDate = endingTimeInGMTMinus3.toLocalDate();
        LocalTime endingTime = LocalTime.of(endingTimeInGMTMinus3.toLocalTime().getHour(), endingTimeInGMTMinus3.toLocalTime().getMinute());
        log.info("Executing on: " + endingDate + " - " + endingTime);
        List<ClassReservation> classReservations = classReservationService.getReservationsByDateAndEndingTime(endingDate, endingTime);
        classReservations.forEach(classReservation -> {
            log.info("Found class reservation: " + classReservation.getId() + " at " + dateFormat.format(classReservation.getDate()));
            Student student = classReservation.getStudent();
            Professor professor = classReservation.getProfessor();

            log.info("Adding pending feedback to student \""
                    + student.getFirstName() + " " + student.getLastName() +
                    "\" and professor \"" + professor.getFirstName() + " " + professor.getLastName() + "\"");

            student.addPendingClassFeedback(classReservation.getId());
            professor.addPendingClassFeedback(classReservation.getId());
            classReservation.setStatus(ReservationStatus.CONCLUDED);
            studentRepository.save(student);
            professorRepository.save(professor);
            classReservationRepository.save(classReservation);
        });
    }

    private static LocalDateTime convertToGMTMinus3(LocalDateTime gmtDateTime) {
        // Set the time zone to GMT
        ZoneId gmtZone = ZoneId.of("GMT");
        // Convert the GMT time to GMT-3
        return gmtDateTime.atZone(gmtZone)
                .withZoneSameInstant(ZoneId.of("GMT-3"))
                .toLocalDateTime();
    }

}