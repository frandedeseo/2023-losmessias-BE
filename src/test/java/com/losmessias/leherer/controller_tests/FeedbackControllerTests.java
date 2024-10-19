package com.losmessias.leherer.controller_tests;

import com.losmessias.leherer.controller.FeedbackController;
import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.domain.enumeration.ReservationStatus;
import com.losmessias.leherer.service.ClassReservationService;
import com.losmessias.leherer.service.FeedbackService;
import com.losmessias.leherer.service.JwtService;
import com.losmessias.leherer.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
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
    @DisplayName("Give feedback to a class not given by the professor returns bad request")
    void giveFeedbackToAClassNotGivenByTheProfessorReturnsBadRequest() throws Exception {
        // Mocking JwtUtil static method
        try (MockedStatic<JwtUtil> jwtUtilMock = mockStatic(JwtUtil.class)) {
            // Set up the mocked behavior for JwtUtil
            jwtUtilMock.when(() -> JwtUtil.extractUserIdFromRequest(any(HttpServletRequest.class), any(JwtService.class)))
                    .thenReturn(ResponseEntity.ok(1L)); // Mock userId as 1

            // Mock the classReservation and other necessary objects
            when(classReservationService.getReservationById(1L)).thenReturn(classReservation);
            when(classReservation.getStatus()).thenReturn(ReservationStatus.CONCLUDED);
            when(classReservation.getStudent()).thenReturn(student);
            when(student.getId()).thenReturn(1L); // studentId matches senderId
            when(classReservation.getProfessor()).thenReturn(professor);
            when(professor.getId()).thenReturn(2L); // ProfessorId does not match

            // Prepare the JSON request
            JSONObject feedback = new JSONObject();
            feedback.put("senderId", 1);
            feedback.put("receiverId", 1);
            feedback.put("roleReceptor", "STUDENT");
            feedback.put("classId", 1L);
            feedback.put("rating", 3);

            // Perform the test
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/feedback/giveFeedback")
                            .contentType("application/json")
                            .content(feedback.toString())
                            .with(csrf()))
                    .andExpect(status().isBadRequest());
        }
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Give feedback to a class returns ok")
    void giveFeedbackToAClassReturnsOk() throws Exception {
        // Mocking JwtUtil static method
        try (MockedStatic<JwtUtil> jwtUtilMock = mockStatic(JwtUtil.class)) {
            // Set up the mocked behavior for JwtUtil
            jwtUtilMock.when(() -> JwtUtil.extractUserIdFromRequest(any(HttpServletRequest.class), any(JwtService.class)))
                    .thenReturn(ResponseEntity.ok(1L)); // Mock userId as 1

            // Mock the classReservation and other necessary objects
            when(classReservationService.getReservationById(1L)).thenReturn(classReservation);
            when(classReservation.getStatus()).thenReturn(ReservationStatus.CONCLUDED);
            when(classReservation.getStudent()).thenReturn(student);
            when(student.getId()).thenReturn(1L); // studentId matches senderId
            when(classReservation.getProfessor()).thenReturn(professor);
            when(professor.getId()).thenReturn(1L); // ProfessorId matches

            // Prepare the JSON request
            JSONObject feedback = new JSONObject();
            feedback.put("senderId", 1);
            feedback.put("receiverId", 1);
            feedback.put("roleReceptor", "STUDENT");
            feedback.put("classId", 1L);
            feedback.put("rating", 3);

            // Perform the test
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/feedback/giveFeedback")
                            .contentType("application/json")
                            .content(feedback.toString())
                            .with(csrf()))
                    .andExpect(status().isOk());
        }
    }

//    @Test
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    @DisplayName("Give feedback to a class not taken by the student returns bad request")
//    public void giveFeedbackToAClassNotTakenByTheStudentReturnsBadRequest() throws Exception {
//        JSONObject feedback = new JSONObject();
//        feedback.put("senderId", 1);
//        feedback.put("recieverId", 1);
//        feedback.put("roleReceptor", "STUDENT");
//        feedback.put("classId", 1L);
//        feedback.put("rating", 3);
//        when(classReservationService.getReservationById(1L)).thenReturn(classReservation);
//        when(classReservation.getStatus()).thenReturn(ReservationStatus.CONCLUDED);
//        when(classReservation.getStudent()).thenReturn(student);
//        when(student.getId()).thenReturn(2L);
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/api/feedback/giveFeedback")
//                        .contentType("application/json")
//                        .content(feedback.toString())
//                        .with(csrf()))
//                .andExpect(status().isBadRequest());
//    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Give feedback to a class not found returns bad request")
    public void giveFeedbackToAClassNotFoundReturnsBadRequest() throws Exception {
        // Mocking JwtUtil static method
        try (MockedStatic<JwtUtil> jwtUtilMock = mockStatic(JwtUtil.class)) {
            // Set up the mocked behavior for JwtUtil
            jwtUtilMock.when(() -> JwtUtil.extractUserIdFromRequest(any(HttpServletRequest.class), any(JwtService.class)))
                    .thenReturn(ResponseEntity.ok(1L)); // Mock userId as 1

            // Mock the classReservationService to return null (class not found)
            when(classReservationService.getReservationById(1L)).thenReturn(null);

            // Prepare the JSON request
            JSONObject feedback = new JSONObject();
            feedback.put("studentId", 1);
            feedback.put("professorId", 1);
            feedback.put("roleReceptor", "STUDENT");
            feedback.put("classId", 1L);
            feedback.put("rating", 3);

            // Perform the test
            mockMvc.perform(MockMvcRequestBuilders
                            .post("/api/feedback/giveFeedback")
                            .contentType("application/json")
                            .content(feedback.toString())
                            .with(csrf()))
                    .andExpect(status().isBadRequest()); // Expecting 400 Bad Request
        }
    }

//    @Test
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    @DisplayName("Give feedback to a class not concluded returns bad request")
//    public void giveFeedbackToAClassNotConcludedReturnsBadRequest() throws Exception {
//        // Mocking JwtUtil static method
//        try (MockedStatic<JwtUtil> jwtUtilMock = mockStatic(JwtUtil.class)) {
//            // Set up the mocked behavior for JwtUtil
//            jwtUtilMock.when(() -> JwtUtil.extractUserIdFromRequest(any(HttpServletRequest.class), any(JwtService.class)))
//                    .thenReturn(ResponseEntity.ok(1L)); // Mock userId as 1
//
//            // Lenient stubbing for classReservationService
//            lenient().when(classReservationService.getReservationById(1L)).thenReturn(classReservation);
//            when(classReservation.getStatus()).thenReturn(ReservationStatus.CONFIRMED); // Class is not concluded
//
//            // Prepare the JSON request
//            JSONObject feedback = new JSONObject();
//            feedback.put("studentId", 1);
//            feedback.put("professorId", 1);
//            feedback.put("roleReceptor", "STUDENT");
//            feedback.put("classId", 1L);
//            feedback.put("rating", 3);
//
//            // Perform the test
//            mockMvc.perform(MockMvcRequestBuilders
//                            .post("/api/feedback/giveFeedback")
//                            .contentType("application/json")
//                            .content(feedback.toString())
//                            .with(csrf()))
//                    .andExpect(status().isBadRequest()); // Expecting 400 Bad Request
//        }
//    }

}
