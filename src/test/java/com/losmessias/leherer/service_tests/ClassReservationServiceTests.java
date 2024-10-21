package com.losmessias.leherer.service_tests;

import com.losmessias.leherer.domain.*;
import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.domain.enumeration.ReservationStatus;
import com.losmessias.leherer.dto.ClassReservationCancelDto;
import com.losmessias.leherer.dto.ProfessorStaticsDto;
import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.repository.ClassReservationRepository;
import com.losmessias.leherer.repository.interfaces.ProfessorDailySummary;
import com.losmessias.leherer.service.ClassReservationService;
import com.losmessias.leherer.domain.enumeration.AppUserSex;
import com.losmessias.leherer.service.NotificationService;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClassReservationServiceTests {
    @Mock
    private ClassReservationRepository classReservationRepository;
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ClassReservationService classReservationService;

    @Test
    @DisplayName("Get reservation by id")
    void testGetReservationById() {
        ClassReservation classReservation = new ClassReservation();
        when(classReservationRepository.findById(1L)).thenReturn(java.util.Optional.of(classReservation));
        assertEquals(classReservation, classReservationService.getReservationById(1L));
    }

//    @Test
//    @DisplayName("Create reservation from student, professor and subject")
//    void testCreateReservationFromStudentAndProfessorSubjectWithDefaultStatus() {
//        Professor professor = new Professor("frandedeseo@gmail.com", "password1234", "Francisco", "de Deseo", "Recoleta", "3462663707", AppUserSex.MALE);;
//        Subject subject = new Subject();
//        Student student = new Student("frandedeseo@gmail.com","fran1234","John", "Doe",  "location", "123", AppUserSex.MALE);
//        ClassReservation classReservation = new ClassReservation(
//                professor,
//                subject,
//                student,
//                LocalDate.of(2024, 7, 1),
//                LocalTime.of(12, 0),
//                LocalTime.of(13, 0),
//                100.0);
//
//        when(classReservationRepository.save(any())).thenReturn(classReservation);
//        assertEquals(classReservation, classReservationService.createReservation(
//                professor,
//                subject,
//                student,
//                LocalDate.of(2024, 7, 1),
//                LocalTime.of(12, 0),
//                LocalTime.of(13, 0),
//                100.0));
//    }

//    @Test
//    @DisplayName("Create reservation from student, professor and subject")
//    void testCancelReservationFromStudentAndProfessorSubjectWithDefaultStatus() {
//        Professor professor = new Professor("frandedeseo@gmail.com", "password1234", "Francisco", "de Deseo", "Recoleta", "3462663707", AppUserSex.MALE);;
//        Subject subject = new Subject();
//        Student student = new Student("frandedeseo@gmail.com","fran1234","John", "Doe",  "location", "123", AppUserSex.MALE);
//        ClassReservation classReservation = new ClassReservation(
//                professor,
//                subject,
//                student,
//                LocalDate.of(2024, 7, 1),
//                LocalTime.of(12, 0),
//                LocalTime.of(13, 0),
//                100.0);
//
//        when(classReservationRepository.save(any())).thenReturn(classReservation);
//        assertEquals(classReservation, classReservationService.createReservation(
//                professor,
//                subject,
//                student,
//                LocalDate.of(2024, 7, 1),
//                LocalTime.of(12, 0),
//                LocalTime.of(13, 0),
//                100.0));
//    }

    @Test
    @DisplayName("Find reservation by professor id")
    void testFindReservationByProfessorId() {
        List<ClassReservation> classReservations = new ArrayList<>();
        classReservations.add(new ClassReservation());
        classReservations.add(new ClassReservation());
        when(classReservationRepository.findByStudentIdOrProfessorId(1L, 1L)).thenReturn(classReservations);
        assertEquals(classReservations, classReservationService.getReservationsByAppUserId(1L));
    }

    @Test
    @DisplayName("Find reservation by student id")
    void testFindReservationByStudentId() {
        List<ClassReservation> classReservations = new ArrayList<>();
        classReservations.add(new ClassReservation());
        classReservations.add(new ClassReservation());
        when(classReservationRepository.findByStudentIdOrProfessorId(1L, 1L)).thenReturn(classReservations);
        assertEquals(classReservations, classReservationService.getReservationsByAppUserId(1L));
    }

    @Test
    @DisplayName("Create unavailable reservation for professor")
    void testCreateUnavailableReservation() {
        Professor professor = new Professor("frandedeseo@gmail.com", "password1234", "Francisco", "de Deseo", "Recoleta", "3462663707", AppUserSex.MALE);;
        ClassReservation classReservation = new ClassReservation(professor, LocalDate.of(2023, 1, 1), LocalTime.of(12, 0), LocalTime.of(13, 0));
        when(classReservationRepository.save(any())).thenReturn(classReservation);
        assertEquals(classReservation, classReservationService.createUnavailableReservation(professor, LocalDate.of(2023, 1, 1), LocalTime.of(12, 0), LocalTime.of(13, 0)));
    }

//    @Test
//    @DisplayName("Creating reservation for professor with invalid time interval")
//    void testCreateReservationForProfessorWithInvalidTimeInterval() {
//        assertThrowsExactly(IllegalArgumentException.class, () -> {
//            Professor professor = new Professor("frandedeseo@gmail.com", "password1234", "Francisco", "de Deseo", "Recoleta", "3462663707", AppUserSex.MALE);;
//            Subject subject = new Subject();
//            Student student = new Student("frandedeseo@gmail.com","fran1234","John", "Doe",  "location", "123", AppUserSex.MALE);
//            classReservationService.createReservation(professor,
//                    subject,
//                    student,
//                    LocalDate.of(2023, 1, 1),
//                    LocalTime.of(12, 0),
//                    LocalTime.of(11, 0),
//                    100.0);
//        });
//    }

    @Test
    @DisplayName("Creating unavailable reservation for professor with invalid time interval")
    void testCreateUnavailableReservationForProfessorWithInvalidTimeInterval() {
        assertThrowsExactly(IllegalArgumentException.class, () -> {
            Professor professor = new Professor("frandedeseo@gmail.com", "password1234", "Francisco", "de Deseo", "Recoleta", "3462663707", AppUserSex.MALE);;
            classReservationService.createUnavailableReservation(professor, LocalDate.of(2023, 1, 1), LocalTime.of(12, 0), LocalTime.of(11, 0));
        });
    }

    @Test
    @DisplayName("Get today summary")
    void testGetTodaySummary() {
        List<ProfessorDailySummary> professorDailySummaries = new ArrayList<>();
        when(classReservationRepository.getProfessorDailySummaryByDay(LocalDate.of(2023, 1, 1))).thenReturn(professorDailySummaries);
        assertEquals(professorDailySummaries, classReservationService.getDailySummary(LocalDate.of(2023, 1, 1)));
    }

    @Test
    @DisplayName("Get reservations for professor on day and time interval finds reservation")
    void testGetReservationsForProfessorOnDayAndTime() {
        when(classReservationRepository.countOverlappingReservations(1L, LocalDate.of(2023, 1, 1), LocalTime.of(12, 0), LocalTime.of(13, 0))).thenReturn(1);
        assertTrue(classReservationService.existsReservationForProfessorOrStudentOnDayAndTime(1L, 1L, LocalDate.of(2023, 1, 1), LocalTime.of(12, 0), LocalTime.of(13, 0)));
    }

    @Test
    @DisplayName("Get reservations for professor on day and time interval finds none")
    void testGetReservationsForProfessorOnDayAndTimeReturnsFalse() {
        when(classReservationRepository.countOverlappingReservations(1L, LocalDate.of(2023, 1, 1), LocalTime.of(12, 0), LocalTime.of(13, 0))).thenReturn(0);
        assertFalse(classReservationService.existsReservationForProfessorOrStudentOnDayAndTime(1L, 1L, LocalDate.of(2023, 1, 1), LocalTime.of(12, 0), LocalTime.of(13, 0)));
    }

    @Test
    @DisplayName("get professor stadistics with three classes in this month")
    void testGetStatics() {
        List<ClassReservation> classes = new ArrayList<>();
        Professor professor = new Professor("frandedeseo@gmail.com", "password1234", "Francisco", "de Deseo", "Recoleta", "3462663707", AppUserSex.MALE);;
        Subject subject = new Subject("Biology", 100.0);
        Student student = new Student("frandedeseo@gmail.com","fran1234","John", "Doe",  "location", "123", AppUserSex.MALE);
        classes.add(
                new ClassReservation(
                        professor,
                        subject,
                        student,
                        LocalDate.of(2024, 10, 1),
                        LocalTime.of(12, 0),
                        LocalTime.of(13, 0),
                        100.0
                )
        );
        classes.add(
                new ClassReservation(
                        professor,
                        subject,
                        student,
                        LocalDate.of(2024, 10, 1),
                        LocalTime.of(12, 0),
                        LocalTime.of(13, 0),
                        100.0
                )
        );
        classes.add(
                new ClassReservation(
                    professor,
                    subject,
                    student,
                    LocalDate.of(2024, 10, 1),
                    LocalTime.of(12, 0),
                    LocalTime.of(13, 0),
                    100.0
                )
        );

        HashMap<String, Double> classesPerSubjectCurr = new HashMap<>();
        classesPerSubjectCurr.put("Biology", 3.0);


        ProfessorStaticsDto currMonthStatics = new ProfessorStaticsDto(
                3.0,
                classesPerSubjectCurr,
                300.0,
                0.0
        );
        ProfessorStaticsDto prevMonthStatics = new ProfessorStaticsDto(
                0.0,
                new HashMap<String, Double>(),
                0.0,
                0.0
        );
        ProfessorStaticsDto average_statics = new ProfessorStaticsDto(
                3.0,
                classesPerSubjectCurr,
                300.0,
                0.0
        );

        List<ProfessorStaticsDto> returnedList = new ArrayList<>();
        returnedList.add(currMonthStatics);
        returnedList.add(prevMonthStatics);
        returnedList.add(average_statics);

        when(classReservationRepository.getClassReservationByProfessorAndOrderByDate(any())).thenReturn(classes);
        assertEquals(returnedList , classReservationService.getStatics(1L));

    }
    @Test
    @DisplayName("get professor stadistics with one class in previous month and two in this month")
    void testGetStaticsOneClassPreviousMonthTwoThisMonth() {
        List<ClassReservation> classes = new ArrayList<>();
        Professor professor = new Professor("frandedeseo@gmail.com", "password1234", "Francisco", "de Deseo", "Recoleta", "3462663707", AppUserSex.MALE);;
        Subject subject = new Subject("Biology");
        Student student = new Student("frandedeseo@gmail.com","fran1234","John", "Doe",  "location", "123", AppUserSex.MALE);
        classes.add(
                new ClassReservation(
                        professor,
                        subject,
                        student,
                        LocalDate.of(2024, 9, 1),
                        LocalTime.of(12, 0),
                        LocalTime.of(13, 0),
                        100.0
                )
        );
        classes.add(
                new ClassReservation(
                        professor,
                        subject,
                        student,
                        LocalDate.of(2024, 10, 1),
                        LocalTime.of(12, 0),
                        LocalTime.of(13, 0),
                        100.0
                )
        );
        classes.add(
                new ClassReservation(
                        professor,
                        subject,
                        student,
                        LocalDate.of(2024, 10, 1),
                        LocalTime.of(12, 0),
                        LocalTime.of(13, 0),
                        100.0
                )
        );

        HashMap<String, Double> classesPerSubjectCurr = new HashMap<>();
        classesPerSubjectCurr.put("Biology", 2.0);

        HashMap<String, Double> classesPerSubjectPrev = new HashMap<>();
        classesPerSubjectPrev.put("Biology", 1.0);

        HashMap<String, Double> classesPerSubjectAvg = new HashMap<>();
        classesPerSubjectAvg.put("Biology", 1.5);

        ProfessorStaticsDto currMonthStatics = new ProfessorStaticsDto(
                2.0,
                classesPerSubjectCurr,
                200.0,
                0.0
        );
        ProfessorStaticsDto prevMonthStatics = new ProfessorStaticsDto(
                1.0,
                classesPerSubjectPrev,
                100.0,
                0.0
        );
        ProfessorStaticsDto average_statics = new ProfessorStaticsDto(
                1.5,
                classesPerSubjectAvg,
                150.0,
                0.0
        );

        List<ProfessorStaticsDto> returnedList = new ArrayList<>();
        returnedList.add(currMonthStatics);
        returnedList.add(prevMonthStatics);
        returnedList.add(average_statics);

        when(classReservationRepository.getClassReservationByProfessorAndOrderByDate(any())).thenReturn(classes);
        assertEquals(returnedList , classReservationService.getStatics(1L));

    }

    @Test
    @DisplayName("get professor stadistics with one class two month ago and one the previous month and where cancelled")
    void testGetStaticsOneClassTwoMonthAgoTwoPreviousMonthAndWhereCancelled() {
        List<ClassReservation> classes = new ArrayList<>();
        Professor professor = new Professor("frandedeseo@gmail.com", "password1234", "Francisco", "de Deseo", "Recoleta", "3462663707", AppUserSex.MALE);;
        Subject subject = new Subject("Biology");
        Student student = new Student("frandedeseo@gmail.com","fran1234","John", "Doe",  "location", "123", AppUserSex.MALE);
        ClassReservation class1 = new ClassReservation(
                        professor,
                        subject,
                        student,
                        LocalDate.of(2024, 8, 1),
                        LocalTime.of(12, 0),
                        LocalTime.of(13, 0),
                        100.0
                );
        class1.setStatus(ReservationStatus.CANCELLED);

        ClassReservation class2 = new ClassReservation(
                        professor,
                        subject,
                        student,
                        LocalDate.of(2024, 9, 1),
                        LocalTime.of(12, 0),
                        LocalTime.of(13, 0),
                        100.0
        );
        class2.setStatus(ReservationStatus.CANCELLED);

        ClassReservation class3 = new ClassReservation(
                        professor,
                        subject,
                        student,
                        LocalDate.of(2024, 9, 29),
                        LocalTime.of(12, 0),
                        LocalTime.of(13, 0),
                        100.0
        );
        class3.setStatus(ReservationStatus.CANCELLED);

        classes.add(class1);
        classes.add(class2);
        classes.add(class3);

        HashMap<String, Double> classesPerSubjectCurr = new HashMap<>();

        HashMap<String, Double> classesPerSubjectPrev = new HashMap<>();
        classesPerSubjectPrev.put("Biology", 2.0);

        HashMap<String, Double> classesPerSubjectAvg = new HashMap<>();
        classesPerSubjectAvg.put("Biology", 1.0);

        ProfessorStaticsDto currMonthStatics = new ProfessorStaticsDto(
                0.0,
                classesPerSubjectCurr,
                0.0,
                0.0
        );
        ProfessorStaticsDto prevMonthStatics = new ProfessorStaticsDto(
                2.0,
                classesPerSubjectPrev,
                200.0,
                2.0
        );
        ProfessorStaticsDto average_statics = new ProfessorStaticsDto(
                1.0,
                classesPerSubjectAvg,
                100.0,
                1.0
        );

        List<ProfessorStaticsDto> returnedList = new ArrayList<>();
        returnedList.add(currMonthStatics);
        returnedList.add(prevMonthStatics);
        returnedList.add(average_statics);

        when(classReservationRepository.getClassReservationByProfessorAndOrderByDate(any())).thenReturn(classes);
        assertEquals(returnedList , classReservationService.getStatics(1L));

    }
}
