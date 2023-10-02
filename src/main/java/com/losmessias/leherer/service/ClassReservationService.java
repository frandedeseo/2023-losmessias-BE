package com.losmessias.leherer.service;

import com.losmessias.leherer.domain.*;
import com.losmessias.leherer.repository.ClassReservationRepository;
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

    public List<ClassReservation> getAllReservations() {
        return classReservationRepository.findAll();
    }

    public ClassReservation getReservationById(Long id) {
        return classReservationRepository.findById(id).orElse(null);
    }

    public ClassReservation createReservationFrom(ProfessorSubject professorSubject,
                                                  Student student,
                                                  LocalDate day,
                                                  LocalTime startingTime,
                                                  LocalTime endingTime,
                                                  Integer price) {
        if(startingTime.isAfter(endingTime)) throw new IllegalArgumentException("Starting time must be before ending time");
        ClassReservation classReservation = this.createReservation(
                professorSubject.getProfessor(),
                professorSubject.getSubject(),
                student,
                day,
                startingTime,
                endingTime,
                price);
        return classReservationRepository.save(classReservation);
    }

    public ClassReservation createReservation(Professor professor,
                                              Subject subject,
                                              Student student,
                                              LocalDate day,
                                              LocalTime startingTime,
                                              LocalTime endingTime,
                                              Integer price) {
        if(startingTime.isAfter(endingTime)) throw new IllegalArgumentException("Starting time must be before ending time");
        ClassReservation classReservation = new ClassReservation(
                professor,
                subject,
                student,
                day,
                startingTime,
                endingTime,
                price);
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
        ClassReservation classReservation = new ClassReservation(professor, day, startingTime, endingTime);
        return classReservationRepository.save(classReservation);
    }

    public List<ClassReservation> createMultipleUnavailableReservationsFor(Professor professor, LocalDate day, LocalTime startingTime, LocalTime endingTime) {
        if (startingTime.isAfter(endingTime)) throw new IllegalArgumentException("Starting time must be before ending time");
        List<LocalTime> intervals = generateTimeIntervals(startingTime, endingTime);
        List<ClassReservation> unavailableReservations = new ArrayList<>();
        for (LocalTime interval : intervals) {
            ClassReservation classReservation = new ClassReservation(professor, day, interval, interval.plusMinutes(30));
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

}
