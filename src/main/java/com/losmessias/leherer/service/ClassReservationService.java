package com.losmessias.leherer.service;

import com.losmessias.leherer.domain.*;
import com.losmessias.leherer.repository.ClassReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
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

}
