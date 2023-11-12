package com.losmessias.leherer.service_tests;

import com.losmessias.leherer.domain.Feedback;
import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.dto.FeedbackDto;
import com.losmessias.leherer.repository.FeedbackRepository;
import com.losmessias.leherer.repository.ProfessorRepository;
import com.losmessias.leherer.repository.StudentRepository;
import com.losmessias.leherer.service.FeedbackService;
import com.losmessias.leherer.service.ProfessorService;
import com.losmessias.leherer.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FeedbackServiceTests {

    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private FeedbackService feedbackService;
    @Mock
    private StudentService studentService;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private ProfessorService professorService;
    @Mock
    private ProfessorRepository professorRepository;
    @Mock
    private Student studentTest;
    @Mock
    private Professor professorTest;

    @BeforeEach
    void setUp() {
        studentTest = new Student();
        studentTest.setId(1L);
        studentTest.setPendingClassesFeedbacks(new ArrayList<>());
        studentTest.setAvgRating(0.0);
        studentTest.setAmountOfRatings(0);
        professorTest = new Professor();
        professorTest.setId(1L);
        professorTest.setAvgRating(0.0);
        professorTest.setAmountOfRatings(0);
        professorTest.setPendingClassesFeedbacks(new ArrayList<>());
    }

    @Test
    @DisplayName("Get all feedbacks")
    void testGetAllFeedbacks() {
        List<Feedback> feedbacks = List.of(new Feedback(), new Feedback());
        when(feedbackRepository.findAll()).thenReturn(feedbacks);
        assert (feedbacks.equals(feedbackService.getAllFeedbacks()));
    }

    @Test
    @DisplayName("Give feedback from dto returns created feedback to professor")
    void testGiveFeedbackFromDtoReturnsCreatedFeedbackToProfessor() {
        Feedback feedback = new Feedback(studentTest, professorTest, AppUserRole.PROFESSOR, new HashSet<>(), 2.0);
        FeedbackDto feedbackDto = new FeedbackDto(1L, 1L, AppUserRole.PROFESSOR, 1L, 2.0, null, null, null);
        when(studentService.getStudentById(feedbackDto.getStudentId())).thenReturn(studentTest);
        when(professorService.getProfessorById(feedbackDto.getProfessorId())).thenReturn(professorTest);

        Feedback feedbackResult = feedbackService.giveFeedback(feedbackDto);
        assert (feedback.getProfessor().equals(feedbackResult.getProfessor()));
        assert (feedback.getStudent().equals(feedbackResult.getStudent()));
        assert (feedback.getRating().equals(feedbackResult.getRating()));
        assert (feedback.getFeedbackOptions().equals(feedbackResult.getFeedbackOptions()));
        assert (feedback.getReceptorRole().equals(feedbackResult.getReceptorRole()));
    }

    @Test
    @DisplayName("Give feedback from dto returns created feedback to student")
    void testGiveFeedbackFromDtoReturnsCreatedFeedbackToStudent() {
        Feedback feedback = new Feedback(studentTest, professorTest, AppUserRole.STUDENT, new HashSet<>(), 2.0);
        FeedbackDto feedbackDto = new FeedbackDto(1L, 1L, AppUserRole.STUDENT, 1L, 2.0, null, null, null);
        when(studentService.getStudentById(feedbackDto.getStudentId())).thenReturn(studentTest);
        when(professorService.getProfessorById(feedbackDto.getProfessorId())).thenReturn(professorTest);

        Feedback feedbackResult = feedbackService.giveFeedback(feedbackDto);
        assert (feedback.getProfessor().equals(feedbackResult.getProfessor()));
        assert (feedback.getStudent().equals(feedbackResult.getStudent()));
        assert (feedback.getRating().equals(feedbackResult.getRating()));
        assert (feedback.getFeedbackOptions().equals(feedbackResult.getFeedbackOptions()));
        assert (feedback.getReceptorRole().equals(feedbackResult.getReceptorRole()));
    }

    @Test
    @DisplayName("Request feedback from concluded class adds class to pending feedbacks")
    void testRequestFeedbackFromConcludedClassAddsClassToPendingFeedbacks() {
        assert studentTest.getPendingClassesFeedbacks().isEmpty();
        assert professorTest.getPendingClassesFeedbacks().isEmpty();

        feedbackService.requestFeedbackFromConcludedClass(studentTest, professorTest, 1L);

        assert studentTest.getPendingClassesFeedbacks().contains(1L);
        assert professorTest.getPendingClassesFeedbacks().contains(1L);
    }
}
