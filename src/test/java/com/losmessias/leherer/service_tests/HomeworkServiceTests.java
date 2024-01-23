package com.losmessias.leherer.service_tests;

import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.Comment;
import com.losmessias.leherer.domain.Homework;
import com.losmessias.leherer.repository.HomeworkRepository;
import com.losmessias.leherer.service.ClassReservationService;
import com.losmessias.leherer.service.CommentService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HomeworkServiceTests {

    @Mock
    private HomeworkRepository homeworkRepository;
    @Mock
    private ClassReservationService classReservationService;
    @Mock
    private ClassReservation classReservation;
    @Mock
    private CommentService commentService;

    @InjectMocks
    private HomeworkService homeworkService;

    @Test
    @DisplayName("Succeeds to get all homeworks")
    void testGetAllHomeworks() {
        List<Homework> homeworks = homeworkService.getAllHomeworks();
        homeworks.add(new Homework());
        homeworks.add(new Homework());

        when(homeworkRepository.findAll()).thenReturn(homeworks);
        assert (homeworkService.getAllHomeworks().size() == 2);
    }

    @Test
    @DisplayName("Suceeds to get homeworkById")
    void testGetHomeworkById() {
        Homework homework = new Homework();

        when(homeworkRepository.findById(1L)).thenReturn(java.util.Optional.of(homework));
        assertEquals(homeworkService.getHomeworkById(1L), homework);
    }

    @Test
    @DisplayName("Succeeds to create homework with valid date")
    void testCreateHomeworkWithValidDate() {
        Homework homework = new Homework();
        homework.setId(1L);
        homework.setClassReservation(new ClassReservation());
        homework.setAssignment(new Comment());
        homework.setDeadline(java.time.LocalDateTime.now().plusDays(1));

        when(classReservationService.getReservationById(1L)).thenReturn(classReservation);
        when(homeworkRepository.save(any())).thenReturn(homework);
//        when()
        assertEquals(homeworkService.createHomework(
                LocalDateTime.now().plusDays(1),
                "Assignment",
                1L,
                1L,
                null
        ), homework);
    }

    @Test
    @DisplayName("Fails to create homework with invalid deadline (in the past)")
    void testCreateHomeworkWithInvalidDate() {
        Homework homework = new Homework();
        homework.setId(1L);
        homework.setClassReservation(new ClassReservation());
        homework.setAssignment(new Comment());
        homework.setDeadline(LocalDateTime.now().minusDays(1));

        assertThrowsExactly(
                IllegalArgumentException.class,
                () -> homeworkService.createHomework(
                        LocalDateTime.now().minusDays(1),
                        "Assignment",
                        1L,
                        1L,
                        null
                ),
                "Deadline must be in the future"
        );
    }

    @Test
    @DisplayName("Fails to create homework with null deadline")
    void testCreateHomeworkWithNullDeadline() {
        Homework homework = new Homework();
        homework.setId(1L);
        homework.setClassReservation(new ClassReservation());
        homework.setAssignment(new Comment());
        homework.setDeadline(null);

        assertThrowsExactly(NullPointerException.class,
                () -> homeworkService.createHomework(
                        null,
                        "Assignment",
                        1L,
                        1L,
                        null
                ),
                "Deadline must not be null"
        );
    }

    @Test
    @DisplayName("Fails to create homework with null assignment")
    void testCreateHomeworkWithNullAssignment() {
        Homework homework = new Homework();
        homework.setId(1L);
        homework.setClassReservation(new ClassReservation());
        homework.setAssignment(null);
        homework.setDeadline(LocalDateTime.now().plusDays(1));
        assertThrowsExactly(IllegalArgumentException.class,
                () -> homeworkService.createHomework(
                        LocalDateTime.now().plusDays(1),
                        null,
                        1L,
                        1L,
                        null
                ),
                "Assignment must not be null"
        );
    }

    @Test
    @DisplayName("Fails to create homework with null class reservation")
    void testCreateHomeworkWithNullClassReservation() {
        Homework homework = new Homework();
        homework.setId(1L);
        homework.setClassReservation(null);
        homework.setAssignment(new Comment());
        homework.setDeadline(LocalDateTime.now().plusDays(1));

        assertThrowsExactly(IllegalArgumentException.class, () -> homeworkService.createHomework(
                        LocalDateTime.now().plusDays(1),
                        "Assignment",
                        null,
                        1L,
                        null
                ), "Class reservation must not be null"
        );
    }

}
