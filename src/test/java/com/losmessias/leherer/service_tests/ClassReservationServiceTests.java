package com.losmessias.leherer.service_tests;

import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.repository.ClassReservationRepository;
import com.losmessias.leherer.repository.interfaces.ProfessorDailySummary;
import com.losmessias.leherer.service.ClassReservationService;
import com.losmessias.leherer.service.NotificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
    @DisplayName("Get all reservations")
    void testGetAllReservations() {
        List<ClassReservation> classReservations = new ArrayList<>();
        classReservations.add(new ClassReservation());
        classReservations.add(new ClassReservation());

        when(classReservationRepository.findAll()).thenReturn(classReservations);
        assertEquals(classReservations, classReservationService.getAllReservations());
    }

    @Test
    @DisplayName("Get reservation by id")
    void testGetReservationById() {
        ClassReservation classReservation = new ClassReservation();
        when(classReservationRepository.findById(1L)).thenReturn(java.util.Optional.of(classReservation));
        assertEquals(classReservation, classReservationService.getReservationById(1L));
    }

    @Test
    @DisplayName("Create reservation from student, professor and subject")
    void testCreateReservationFromStudentAndProfessorSubjectWithDefaultStatus() {
        Professor professor = new Professor();
        Subject subject = new Subject();
        Student student = new Student();
        ClassReservation classReservation = new ClassReservation(
                professor,
                subject,
                student,
                LocalDate.of(2023, 1, 1),
                LocalTime.of(12, 0),
                LocalTime.of(13, 0),
                0.0,
                100);

        when(classReservationRepository.save(any())).thenReturn(classReservation);
        assertEquals(classReservation, classReservationService.createReservation(
                professor,
                subject,
                student,
                LocalDate.of(2023, 1, 1),
                LocalTime.of(12, 0),
                LocalTime.of(13, 0),
                0.0,
                100));
    }

    @Test
    @DisplayName("Create reservation from student, professor and subject")
    void testCancelReservationFromStudentAndProfessorSubjectWithDefaultStatus() {
        Professor professor = new Professor();
        Subject subject = new Subject();
        Student student = new Student();
        ClassReservation classReservation = new ClassReservation(
                professor,
                subject,
                student,
                LocalDate.of(2023, 1, 1),
                LocalTime.of(12, 0),
                LocalTime.of(13, 0),
                0.0,
                100);

        when(classReservationRepository.save(any())).thenReturn(classReservation);
        assertEquals(classReservation, classReservationService.createReservation(
                professor,
                subject,
                student,
                LocalDate.of(2023, 1, 1),
                LocalTime.of(12, 0),
                LocalTime.of(13, 0),
                0.0,
                100));
    }

    @Test
    @DisplayName("Find reservation by professor id")
    void testFindReservationByProfessorId() {
        List<ClassReservation> classReservations = new ArrayList<>();
        classReservations.add(new ClassReservation());
        classReservations.add(new ClassReservation());
        when(classReservationRepository.findByProfessorId(1L)).thenReturn(classReservations);
        assertEquals(classReservations, classReservationService.getReservationsByProfessorId(1L));
    }

    @Test
    @DisplayName("Find reservation by student id")
    void testFindReservationByStudentId() {
        List<ClassReservation> classReservations = new ArrayList<>();
        classReservations.add(new ClassReservation());
        classReservations.add(new ClassReservation());
        when(classReservationRepository.findByStudentId(1L)).thenReturn(classReservations);
        assertEquals(classReservations, classReservationService.getReservationsByStudentId(1L));
    }

    @Test
    @DisplayName("Find reservation by subject id")
    void testFindReservationBySubjectId() {
        List<ClassReservation> classReservations = new ArrayList<>();
        classReservations.add(new ClassReservation());
        classReservations.add(new ClassReservation());
        when(classReservationRepository.findBySubjectId(1L)).thenReturn(classReservations);
        assertEquals(classReservations, classReservationService.getReservationsBySubjectId(1L));
    }

    @Test
    @DisplayName("Create unavailable reservation for professor")
    void testCreateUnavailableReservation() {
        Professor professor = new Professor();
        ClassReservation classReservation = new ClassReservation(professor, LocalDate.of(2023, 1, 1), LocalTime.of(12, 0), LocalTime.of(13, 0), 1.0);
        when(classReservationRepository.save(any())).thenReturn(classReservation);
        assertEquals(classReservation, classReservationService.createUnavailableReservation(professor, LocalDate.of(2023, 1, 1), LocalTime.of(12, 0), LocalTime.of(13, 0)));
    }

    @Test
    @DisplayName("Create multiple unavailable reservations for professor")
    void testCreateUnavailableReservationsForProfessor() {
        Professor professor = new Professor();
        List<ClassReservation> classReservations = new ArrayList<>();
        classReservations.add(new ClassReservation(professor, LocalDate.of(2023, 1, 1), LocalTime.of(12, 0), LocalTime.of(12, 30), 0.5));
        classReservations.add(new ClassReservation(professor, LocalDate.of(2023, 1, 1), LocalTime.of(12, 30), LocalTime.of(13, 0), 0.5));
        when(classReservationRepository.saveAll(any())).thenReturn(classReservations);
        assertEquals(classReservations, classReservationService.createMultipleUnavailableReservationsFor(professor, LocalDate.of(2023, 1, 1), LocalTime.of(12, 0), LocalTime.of(13, 0)));
    }

    @Test
    @DisplayName("Creating reservation for professor with invalid time interval")
    void testCreateReservationForProfessorWithInvalidTimeInterval() {
        assertThrowsExactly(IllegalArgumentException.class, () -> {
            Professor professor = new Professor();
            Subject subject = new Subject();
            Student student = new Student();
            classReservationService.createReservation(professor,
                    subject,
                    student,
                    LocalDate.of(2023, 1, 1),
                    LocalTime.of(12, 0),
                    LocalTime.of(11, 0),
                    1.0,
                    100);
        });
    }

    @Test
    @DisplayName("Creating unavailable reservation for professor with invalid time interval")
    void testCreateUnavailableReservationForProfessorWithInvalidTimeInterval() {
        assertThrowsExactly(IllegalArgumentException.class, () -> {
            Professor professor = new Professor();
            classReservationService.createUnavailableReservation(professor, LocalDate.of(2023, 1, 1), LocalTime.of(12, 0), LocalTime.of(11, 0));
        });
    }

    @Test
    @DisplayName("Creating unavailable reservations for professor with invalid time interval")
    void testCreateUnavailableReservationsForProfessorWithInvalidTimeInterval() {
        assertThrowsExactly(IllegalArgumentException.class, () -> {
            Professor professor = new Professor();
            classReservationService.createMultipleUnavailableReservationsFor(professor, LocalDate.of(2023, 1, 1), LocalTime.of(12, 0), LocalTime.of(11, 0));
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
        assertTrue(classReservationService.existsReservationForProfessorOnDayAndTime(1L, LocalDate.of(2023, 1, 1), LocalTime.of(12, 0), LocalTime.of(13, 0)));
    }

    @Test
    @DisplayName("Get reservations for professor on day and time interval finds none")
    void testGetReservationsForProfessorOnDayAndTimeReturnsFalse() {
        when(classReservationRepository.countOverlappingReservations(1L, LocalDate.of(2023, 1, 1), LocalTime.of(12, 0), LocalTime.of(13, 0))).thenReturn(0);
        assertFalse(classReservationService.existsReservationForProfessorOnDayAndTime(1L, LocalDate.of(2023, 1, 1), LocalTime.of(12, 0), LocalTime.of(13, 0)));
    }
}
