package com.losmessias.leherer.service;

import com.losmessias.leherer.domain.*;
import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.domain.enumeration.ReservationStatus;
import com.losmessias.leherer.dto.ClassReservationCancel;
import com.losmessias.leherer.repository.ClassReservationRepository;
import com.losmessias.leherer.repository.interfaces.ProfessorDailySummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClassReservationService {

    private final ClassReservationRepository classReservationRepository;
    private final ProfessorService professorService;
    private final SubjectService subjectService;
    private final NotificationService notificationService;

    public List<ClassReservation> getAllReservations() {
        return classReservationRepository.findAll();
    }

    public ClassReservation getReservationById(Long id) {
        return classReservationRepository.findById(id).orElse(null);
    }

    public ClassReservation cancelReservation(ClassReservationCancel classReservationCancel){
        ClassReservation classReservation = getReservationById(classReservationCancel.getId());
        classReservation.setStatus(ReservationStatus.CANCELLED);
        if (checkIfIsBetween48hsBefore(classReservation)){
            classReservation.setPrice(classReservation.getPrice()/2);
        }else{
            classReservation.setPrice(0);
        }
        notificationService.cancelClassReservedNotification(classReservation, classReservationCancel.getRole());
        return classReservationRepository.save(classReservation);

    }

    private boolean checkIfIsBetween48hsBefore(ClassReservation classReservation){
        return classReservation.getDate().minusDays(2).isBefore(LocalDate.now()) || classReservation.getDate().minusDays(2).isEqual(LocalDate.now()) && classReservation.getStartingHour().isBefore(LocalTime.now());
    }

    public ClassReservation createReservation(Professor professor,
                                              Subject subject,
                                              Student student,
                                              LocalDate day,
                                              LocalTime startingTime,
                                              LocalTime endingTime,
                                              Double duration,
                                              Integer price) {
        if(startingTime.isAfter(endingTime)) throw new IllegalArgumentException("Starting time must be before ending time");
        ClassReservation classReservation = new ClassReservation(
                professor,
                subject,
                student,
                day,
                startingTime,
                endingTime,
                duration,
                price
        );
        notificationService.generateClassReservedNotification(classReservation);

        return classReservationRepository.save(classReservation);
    }

    public List<ClassReservation> getReservationsByProfessorId(Long id) {
        return classReservationRepository.findByProfessorId(id);
    }

    public List<ClassReservation> getReservationsByStudentId(Long id) {
        return classReservationRepository.findByStudentId(id);
    }

    public List<ClassReservation> getReservationsBySubjectId(Long id) {
        return classReservationRepository.findBySubjectId(id);
    }

    public ClassReservation createUnavailableReservation(Professor professor, LocalDate day, LocalTime startingTime, LocalTime endingTime) {
        if (startingTime.isAfter(endingTime)) throw new IllegalArgumentException("Starting time must be before ending time");
        Double duration = (endingTime.getHour() - startingTime.getHour()) + ((endingTime.getMinute() - startingTime.getMinute()) / 60.0);
        ClassReservation classReservation = new ClassReservation(professor, day, startingTime, endingTime, duration);
        return classReservationRepository.save(classReservation);
    }

    public List<ClassReservation> createMultipleUnavailableReservationsFor(Professor professor, LocalDate day, LocalTime startingTime, LocalTime endingTime, Double duration) {
        if (startingTime.isAfter(endingTime)) throw new IllegalArgumentException("Starting time must be before ending time");
        List<LocalTime> intervals = generateTimeIntervals(startingTime, endingTime);
        List<ClassReservation> unavailableReservations = new ArrayList<>();
        for (LocalTime interval : intervals) {
            ClassReservation classReservation = new ClassReservation(professor, day, interval, interval.plusMinutes(30), 0.5);
            unavailableReservations.add(classReservation);
        }
        return classReservationRepository.saveAll(unavailableReservations);
    }

    private List<LocalTime> generateTimeIntervals(LocalTime startTime, LocalTime endTime) {
        List<LocalTime> intervals = new ArrayList<>();
        while (startTime.isBefore(endTime)) {
            intervals.add(startTime);
            startTime = startTime.plusMinutes(30);
        }
        return intervals;
    }

    public List<ProfessorDailySummary> getDailySummary(LocalDate day) {
        return classReservationRepository.getProfessorDailySummaryByDay(day);
    }

    public List<ClassReservation> getByProfessorAndSubject(Long professorId, Long subjectId) {
        Professor professor = professorService.getProfessorById(professorId);
        Subject subject = subjectService.getSubjectById(subjectId);
        return classReservationRepository.findByProfessorAndSubject(professor, subject);
    }
}
