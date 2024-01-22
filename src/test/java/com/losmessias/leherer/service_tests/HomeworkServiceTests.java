package com.losmessias.leherer.service_tests;

import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.Comment;
import com.losmessias.leherer.domain.Homework;
import com.losmessias.leherer.repository.HomeworkRepository;
import com.losmessias.leherer.service.HomeworkService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HomeworkServiceTests {

    @Mock
    private HomeworkRepository homeworkRepository;

    @InjectMocks
    private HomeworkService homeworkService;

    @Test
    @DisplayName("Get all homeworks")
    void testGetAllHomeworks() {
        List<Homework> homeworks = homeworkService.getAllHomeworks();
        homeworks.add(new Homework());
        homeworks.add(new Homework());

        when(homeworkRepository.findAll()).thenReturn(homeworks);
        assert (homeworkService.getAllHomeworks().size() == 2);
    }

    @Test
    @DisplayName("Get homeworkById")
    void testGetHomeworkById() {
        Homework homework = new Homework();

        when(homeworkRepository.findById(1L)).thenReturn(java.util.Optional.of(homework));
        assertEquals(homeworkService.getHomeworkById(1L), homework);
    }

    @Test
    @DisplayName("Create homework with valid date")
    void testCreateHomeworkWithValidDate() {
        Homework homework = new Homework();
        homework.setId(1L);
        homework.setClassReservation(new ClassReservation());
        homework.setAssignment(new Comment());
        homework.setDeadline(java.time.LocalDateTime.now().plusDays(1));

        when(homeworkRepository.save(homework)).thenReturn(homework);
        assertEquals(homeworkService.createHomework(homework), homework);
    }

    @Test
    @DisplayName("Create homework with invalid date")
    void testCreateHomeworkWithInvalidDate() {
        Homework homework = new Homework();
        homework.setId(1L);
        homework.setClassReservation(new ClassReservation());
        homework.setAssignment(new Comment());
        homework.setDeadline(LocalDateTime.now().minusDays(1));

        assertThrowsExactly(IllegalArgumentException.class, () -> homeworkService.createHomework(homework), "Deadline must be in the future");
    }

    @Test
    @DisplayName("Create homework with null deadline")
    void testCreateHomeworkWithNullDeadline() {
        Homework homework = new Homework();
        homework.setId(1L);
        homework.setClassReservation(new ClassReservation());
        homework.setAssignment(new Comment());
        homework.setDeadline(null);

        assertThrowsExactly(IllegalArgumentException.class, () -> homeworkService.createHomework(homework), "Deadline must not be null");
    }

    @Test
    @DisplayName("Create homework with null assignment")
    void testCreateHomeworkWithNullAssignment() {
        Homework homework = new Homework();
        homework.setId(1L);
        homework.setClassReservation(new ClassReservation());
        homework.setAssignment(null);
        homework.setDeadline(LocalDateTime.now().plusDays(1));

        assertThrowsExactly(IllegalArgumentException.class, () -> homeworkService.createHomework(homework), "Assignment must not be null");
    }

    @Test
    @DisplayName("Create homework with null class reservation")
    void testCreateHomeworkWithNullClassReservation() {
        Homework homework = new Homework();
        homework.setId(1L);
        homework.setClassReservation(null);
        homework.setAssignment(new Comment());
        homework.setDeadline(LocalDateTime.now().plusDays(1));

        assertThrowsExactly(IllegalArgumentException.class, () -> homeworkService.createHomework(homework), "Class reservation must not be null");
    }

    @Test
    @DisplayName("Create homework with null homework")
    void testCreateHomeworkWithNullHomework() {
        assertThrowsExactly(IllegalArgumentException.class, () -> homeworkService.createHomework(null), "Homework must not be null");
    }
}
