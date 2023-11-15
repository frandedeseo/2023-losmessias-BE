package com.losmessias.leherer.controller_tests;

import com.losmessias.leherer.controller.FeedbackController;
import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.domain.enumeration.ReservationStatus;
import com.losmessias.leherer.service.ClassReservationService;
import com.losmessias.leherer.service.FeedbackService;
import com.losmessias.leherer.service.JwtService;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(FeedbackController.class)
public class FeedbackControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private FeedbackService feedbackService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private ClassReservationService classReservationService;

    @Mock
    private ClassReservation classReservation;
    @Mock
    private Student student;
    @Mock
    private Professor professor;

    @Test
    @DisplayName("Get feedback without authentication returns bad request")
    public void getFeedbackWithoutAuthenticationReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/feedback/getAllFeedbacks"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Get feedback with authentication returns ok")
    public void getFeedbackWithAuthenticationReturnsOk() throws Exception {
        mockMvc.perform(get("/api/feedback/getAllFeedbacks"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Give feedback to a class not found returns bad request")
    public void giveFeedbackToAClassNotFoundReturnsBadRequest() throws Exception {
        JSONObject feedback = new JSONObject();
        feedback.put("studentId", 1);
        feedback.put("professorId", 1);
        feedback.put("roleReceptor", "STUDENT");
        feedback.put("classId", 1L);
        feedback.put("rating", 3);
        when(classReservationService.getReservationById(1L)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/feedback/giveFeedback")
                        .contentType("application/json")
                        .content(feedback.toString())
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Give feedback to a class not concluded returns bad request")
    public void giveFeedbackToAClassNotConcludedReturnsBadRequest() throws Exception {
        JSONObject feedback = new JSONObject();
        feedback.put("studentId", 1);
        feedback.put("professorId", 1);
        feedback.put("roleReceptor", "STUDENT");
        feedback.put("classId", 1L);
        feedback.put("rating", 3);
        when(classReservationService.getReservationById(1L)).thenReturn(classReservation);
        when(classReservation.getStatus()).thenReturn(ReservationStatus.CONFIRMED);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/feedback/giveFeedback")
                        .contentType("application/json")
                        .content(feedback.toString())
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Give feedback to a class not taken by the student returns bad request")
    public void giveFeedbackToAClassNotTakenByTheStudentReturnsBadRequest() throws Exception {
        JSONObject feedback = new JSONObject();
        feedback.put("studentId", 1);
        feedback.put("professorId", 1);
        feedback.put("roleReceptor", "STUDENT");
        feedback.put("classId", 1L);
        feedback.put("rating", 3);
        when(classReservationService.getReservationById(1L)).thenReturn(classReservation);
        when(classReservation.getStatus()).thenReturn(ReservationStatus.CONCLUDED);
        when(classReservation.getStudent()).thenReturn(student);
        when(student.getId()).thenReturn(2L);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/feedback/giveFeedback")
                        .contentType("application/json")
                        .content(feedback.toString())
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Give feedback to a class not given by the professor returns bad request")
    void giveFeedbackToAClassNotGivenByTheProfessorReturnsBadRequest() throws Exception {
        JSONObject feedback = new JSONObject();
        feedback.put("studentId", 1);
        feedback.put("professorId", 1);
        feedback.put("roleReceptor", "STUDENT");
        feedback.put("classId", 1L);
        feedback.put("rating", 3);
        when(classReservationService.getReservationById(1L)).thenReturn(classReservation);
        when(classReservation.getStatus()).thenReturn(ReservationStatus.CONCLUDED);
        when(classReservation.getStudent()).thenReturn(student);
        when(student.getId()).thenReturn(1L);
        when(classReservation.getProfessor()).thenReturn(professor);
        when(professor.getId()).thenReturn(2L);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/feedback/giveFeedback")
                        .contentType("application/json")
                        .content(feedback.toString())
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Give feedback to a class returns ok")
    void giveFeedbackToAClassReturnsOk() throws Exception {
        JSONObject feedback = new JSONObject();
        feedback.put("studentId", 1);
        feedback.put("professorId", 1);
        feedback.put("roleReceptor", "STUDENT");
        feedback.put("classId", 1L);
        feedback.put("rating", 3);
        when(classReservationService.getReservationById(1L)).thenReturn(classReservation);
        when(classReservation.getStatus()).thenReturn(ReservationStatus.CONCLUDED);
        when(classReservation.getStudent()).thenReturn(student);
        when(student.getId()).thenReturn(1L);
        when(classReservation.getProfessor()).thenReturn(professor);
        when(professor.getId()).thenReturn(1L);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/feedback/giveFeedback")
                        .contentType("application/json")
                        .content(feedback.toString())
                        .with(csrf()))
                .andExpect(status().isOk());
    }

}
