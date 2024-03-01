package com.losmessias.leherer.repository_tests;

import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.domain.enumeration.ReservationStatus;
import com.losmessias.leherer.repository.ClassReservationRepository;
import com.losmessias.leherer.repository.ProfessorRepository;
import com.losmessias.leherer.repository.StudentRepository;
import com.losmessias.leherer.repository.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
public class ClassReservationRepositoryTests {

    @Autowired
    private ClassReservationRepository classReservationRepository;
    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private StudentRepository studentRepository;

    private ClassReservation classReservation;
    private Professor professor;
    private Subject subject;
    private Student student;

    @BeforeEach
    public void setupData() {
        professor = new Professor();
        subject = Subject.builder()
                .name("Math")
                .build();
        student = new Student();
        professorRepository.save(professor);
        subjectRepository.save(subject);
        studentRepository.save(student);
        classReservation = new ClassReservation (
                professor,
                subject,
                student,
                LocalDate.of(2023, 1, 1),
                LocalTime.of(9, 0),
                LocalTime.of(14, 0),
                100.0
        );
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Should save a class reservation")
    public void shouldSaveAClassReservation() {
        classReservationRepository.save(classReservation);
        assertEquals(1, classReservationRepository.findAll().size());
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Should find by professorId")
    public void shouldFindByProfessorId() {
        classReservationRepository.save(classReservation);
        assertEquals(1, classReservationRepository.findByStudentIdOrProfessorId(student.getId(), professor.getId()).size());
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Should find by studentId")
    public void shouldFindByStudentId() {
        classReservationRepository.save(classReservation);
        assertEquals(1, classReservationRepository.findByStudentIdOrProfessorId(student.getId(), professor.getId()).size());
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Should make simple daily summary")
    public void shouldMakeDailySummary() {
        classReservationRepository.save(classReservation);
        assertEquals(1, classReservationRepository.getProfessorDailySummaryByDay(classReservation.getDate()).size());
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Should make daily summary with multiple subjects")
    public void shouldMakeDailySummaryWithMultipleSubjects() {
        Subject subject2 = Subject.builder()
                .name("Portuguese")
                .build();
        subjectRepository.save(subject2);

        ClassReservation classReservation2 = new ClassReservation (
                professor,
                subject2,
                student,
                LocalDate.of(2023, 1, 1),
                LocalTime.of(9, 0),
                LocalTime.of(14, 0),
                100.0
        );
        classReservationRepository.save(classReservation);
        classReservationRepository.save(classReservation2);
        assertEquals(2, classReservationRepository.getProfessorDailySummaryByDay(classReservation.getDate()).size());
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Find by professor and subject id")
    public void shouldFindByProfessorAndSubject() {
        classReservationRepository.save(classReservation);
        assertEquals(1, classReservationRepository.findByProfessorAndSubject(professor, subject).size());
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Find by overlapping reservations")
    public void shouldCountOverlappingReservations() {
        classReservationRepository.save(classReservation);
        ClassReservation classReservation2 = new ClassReservation (
                professor,
                subject,
                student,
                LocalDate.of(2023, 1, 1),
                LocalTime.of(9, 0),
                LocalTime.of(14, 0),
                100.0
        );
        classReservationRepository.save(classReservation2);
        assertEquals(2, classReservationRepository.countOverlappingReservations(
                professor.getId(),
                classReservation.getDate(),
                classReservation.getStartingHour(),
                classReservation.getEndingHour()));
    }
}
