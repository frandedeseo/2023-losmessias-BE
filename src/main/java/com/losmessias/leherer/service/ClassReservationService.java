package com.losmessias.leherer.service;

import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.domain.enumeration.ReservationStatus;
import com.losmessias.leherer.dto.ClassReservationCancelDto;
import com.losmessias.leherer.dto.ProfessorStaticsDto;
import com.losmessias.leherer.repository.ClassReservationRepository;
import com.losmessias.leherer.repository.interfaces.ProfessorDailySummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashMap;
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

    public ClassReservation cancelReservation(ClassReservationCancelDto classReservationCancelDto) {
        ClassReservation classReservation = getReservationById(classReservationCancelDto.getId());
        classReservation.setStatus(ReservationStatus.CANCELLED);
        if (checkIfIsBetween48hsBefore(classReservation)) {
            classReservation.setPrice(classReservation.getPrice() / 2);
        } else {
            classReservation.setPrice(0);
        }
        notificationService.cancelClassReservedNotification(classReservation, classReservationCancelDto.getRole());
        return classReservationRepository.save(classReservation);

    }

    private boolean checkIfIsBetween48hsBefore(ClassReservation classReservation) {
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
        if (startingTime.isAfter(endingTime))
            throw new IllegalArgumentException("Starting time must be before ending time");
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

    public boolean existsReservationForProfessorOnDayAndTime(Long professor,
                                                             LocalDate day,
                                                             LocalTime startingTime,
                                                             LocalTime endingTime) {
        int overlapping = classReservationRepository.countOverlappingReservations(
                professor,
                day,
                startingTime,
                endingTime);
        return overlapping > 0;
    }

    public List<ClassReservation> getReservationsByProfessorId(Long id) {
        return getUnCancelledReservation(classReservationRepository.findByProfessorId(id));
    }

    private List<ClassReservation> getUnCancelledReservation(List<ClassReservation> classes) {
        List<ClassReservation> classesUnCancelled = new ArrayList<>();
        classes
                .stream()
                .filter(clase -> clase.getStatus() != ReservationStatus.CANCELLED)
                .forEach(classesUnCancelled::add);
        return classesUnCancelled;
    }

    public List<ClassReservation> getReservationsByStudentId(Long id) {
        return getUnCancelledReservation(classReservationRepository.findByStudentId(id));
    }

    public List<ClassReservation> getReservationsBySubjectId(Long id) {
        return classReservationRepository.findBySubjectId(id);
    }

    public ClassReservation createUnavailableReservation(Professor professor, LocalDate day, LocalTime startingTime, LocalTime endingTime) {
        if (startingTime.isAfter(endingTime))
            throw new IllegalArgumentException("Starting time must be before ending time");
        Double duration = (endingTime.getHour() - startingTime.getHour()) + ((endingTime.getMinute() - startingTime.getMinute()) / 60.0);
        ClassReservation classReservation = new ClassReservation(professor, day, startingTime, endingTime, duration);
        return classReservationRepository.save(classReservation);
    }

    public List<ClassReservation> createMultipleUnavailableReservationsFor(Professor professor, LocalDate day, LocalTime startingTime, LocalTime endingTime) {
        if (startingTime.isAfter(endingTime))
            throw new IllegalArgumentException("Starting time must be before ending time");
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

    public List<ProfessorStaticsDto> getStatics(Long id) {

        List<ClassReservation> classes = classReservationRepository.getClassReservationByProfessorAndOrderByDate(id);

        Integer amountOfMonths = Period.between(classes.get(0).getDate(), classes.get(classes.size() - 1).getDate()).getMonths() + 1;

        List<ClassReservation> currentMonth = new ArrayList<>();
        List<ClassReservation> prevMonth = new ArrayList<>();
        for (ClassReservation res : classes) {
            if (res.getDate().getYear() == LocalDate.now().getYear()) {
                if (res.getDate().getMonthValue() == LocalDate.now().getMonthValue()) {
                    currentMonth.add(res);
                } else if (res.getDate().getMonthValue() == LocalDate.now().getMonthValue() - 1) {
                    prevMonth.add(res);
                }
            }
        }

        ProfessorStaticsDto average = getProfessorStatic(classes);

        ProfessorStaticsDto currMonthStatics = getProfessorStatic(currentMonth);
        ProfessorStaticsDto prevMonthStatics = getProfessorStatic(prevMonth);

        average.getClassesPerSubject().replaceAll((k, v) -> v / amountOfMonths);
        ProfessorStaticsDto average_statics = new ProfessorStaticsDto(
                (double) average.getTotalClasses() / amountOfMonths,
                average.getClassesPerSubject(),
                average.getIncomes() / amountOfMonths,
                average.getCancelledClasses() / amountOfMonths
        );
        List<ProfessorStaticsDto> returnedList = new ArrayList<>();
        returnedList.add(currMonthStatics);
        returnedList.add(prevMonthStatics);
        returnedList.add(average_statics);
        return returnedList;
    }

    private ProfessorStaticsDto getProfessorStatic(List<ClassReservation> classes) {
        HashMap<String, Double> classesPerSubject = new HashMap<>();
        Double amountOfClasses = (double) classes.size();
        Double incomes = 0.0;
        Double amountOfCancelledClasses = 0.0;
        for (ClassReservation res : classes) {
            incomes += res.getPrice();

            if (res.getStatus() == ReservationStatus.CANCELLED) {
                amountOfCancelledClasses += 1;
            }

            if (classesPerSubject.get(res.getSubject().getName()) == null) {
                classesPerSubject.put(res.getSubject().getName(), 1.0);
            } else {
                classesPerSubject.put(res.getSubject().getName(), (Double) classesPerSubject.get(res.getSubject().getName()) + 1);
            }
        }
        return new ProfessorStaticsDto(
                amountOfClasses,
                classesPerSubject,
                incomes,
                amountOfCancelledClasses
        );
    }

    public List<ClassReservation> getReservationsByDateAndEndingTime(LocalDate date, LocalTime endingHour) {
        System.out.println("date: "+date+" endingHour: "+endingHour);
        return classReservationRepository.findByDateAndEndingHour(date,endingHour);
    }
}
