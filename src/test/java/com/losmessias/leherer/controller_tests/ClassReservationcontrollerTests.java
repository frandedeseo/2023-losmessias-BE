package com.losmessias.leherer.controller_tests;

import com.losmessias.leherer.controller.ClassReservationController;
import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.domain.enumeration.ReservationStatus;
import com.losmessias.leherer.dto.ClassReservationDto;
import com.losmessias.leherer.repository.ProfessorSubjectRepository;
import com.losmessias.leherer.repository.interfaces.ProfessorDailySummary;
import com.losmessias.leherer.service.*;
import com.losmessias.leherer.domain.enumeration.AppUserSex;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ClassReservationController.class)
public class ClassReservationcontrollerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ClassReservationService classReservationService;
    @MockBean
    private StudentService studentService;
    @MockBean
    private SubjectService subjectService;
    @MockBean
    private ProfessorService professorService;
    @MockBean
    private ProfessorSubjectService professorSubjectService;
    @MockBean
    private ProfessorSubjectRepository professorSubjectRepository;
    @MockBean
    private JwtService jwtService;

    @Mock
    private ClassReservation classReservationTest1;
    @Mock
    private ClassReservation classReservationTest2;
    @Mock
    private Student studentTest;
    @Mock
    private ClassReservationDto classReservationDtoTest;


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Get reservation by id")
    void testGetReservationByIdReturnsOk() throws Exception {
        when(classReservationService.getReservationById(1L)).thenReturn(classReservationTest1);
        when(classReservationTest1.getProfessor()).thenReturn(new Professor());
        when(classReservationTest1.getSubject()).thenReturn(new Subject());
        when(classReservationTest1.getStudent()).thenReturn(new Student());
        when(classReservationTest1.getDate()).thenReturn(null);
        when(classReservationTest1.getStartingHour()).thenReturn(null);
        when(classReservationTest1.getEndingHour()).thenReturn(null);
        when(classReservationTest1.getPrice()).thenReturn(null);
        when(classReservationTest1.getStatus()).thenReturn(ReservationStatus.CONFIRMED);
        mockMvc.perform(get("/api/reservation/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Get reservation by id finds no reservation")
    void testGetReservationByIdReturnsNotFound() throws Exception {
        when(classReservationService.getReservationById(1L)).thenReturn(null);
        mockMvc.perform(get("/api/reservation/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Get reservation by id without authentication")
    void testGetReservationByIdReturnsUnauthorized() throws Exception {
        when(classReservationService.getReservationById(1L)).thenReturn(classReservationTest1);
        mockMvc.perform(get("/api/reservation/1"))
                .andExpect(status().isUnauthorized());
    }

//    @Test
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    @DisplayName("Create a reservation returns ok")
//    void testCreateAReservationReturnsOk() throws Exception {
//        Professor professor = new Professor("frandedeseo@gmail.com", "password1234", "Francisco", "de Deseo", "Recoleta", "3462663707", AppUserSex.MALE);;
//        Subject subject = new Subject();
//        Student student = new Student("frandedeseo@gmail.com","fran1234","John", "Doe",  "location", "123", AppUserSex.MALE);
//        JSONObject jsonContent = new JSONObject();
//        jsonContent.put("professorId", 1);
//        jsonContent.put("subjectId", 1);
//        jsonContent.put("studentId", 1);
//        jsonContent.put("day", LocalDate.of(2023, 1, 1));
//        jsonContent.put("startingTime", LocalTime.of(12, 0));
//        jsonContent.put("endingHour", LocalTime.of(13, 0));
//        jsonContent.put("price", 100);
//
//        when(studentService.getStudentById(1L)).thenReturn(studentTest);
//        when(studentService.getStudentById(1L).canMakeAReservation()).thenReturn(true);
//
//        when(classReservationService.createReservation(professor, subject, student, LocalDate.of(2023, 1, 1), LocalTime.of(12, 0, 0), LocalTime.of(13, 0, 0), 100.0)).thenReturn(classReservationTest1);
//        when(classReservationService.createReservation(
//                professor,
//                subject,
//                student,
//                LocalDate.of(2023, 1, 1),
//                LocalTime.of(12, 0, 0),
//                LocalTime.of(13, 0, 0),
//                100.0)).thenReturn(classReservationTest1);
//
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/api/reservation/create")
//                        .contentType("application/json")
//                        .content(jsonContent.toString())
//                        .with(csrf()))
//                .andExpect(status().isOk());
//    }

//    @Test
//    @WithMockUser
//    @DisplayName("Create a reservation returns bad request on already exists")
//    void testCreateAReservationReturnsBadRequestOnAlreadyExists() throws Exception {
//        Professor professor = new Professor("frandedeseo@gmail.com", "password1234", "Francisco", "de Deseo", "Recoleta", "3462663707", AppUserSex.MALE);;
//        Subject subject = new Subject();
//        Student student = new Student("frandedeseo@gmail.com","fran1234","John", "Doe",  "location", "123", AppUserSex.MALE);
//        JSONObject jsonContent = new JSONObject();
//        jsonContent.put("professorId", 1);
//        jsonContent.put("subjectId", 1);
//        jsonContent.put("studentId", 1);
//        jsonContent.put("day", LocalDate.of(2023, 1, 1));
//        jsonContent.put("startingTime", LocalTime.of(11, 0));
//        jsonContent.put("endingHour", LocalTime.of(14, 0));
//        jsonContent.put("price", 100);
//
//        when(classReservationService.existsReservationForProfessorOrStudentOnDayAndTime(any(), any(), any(), any(), any())).thenReturn(true);
//        when(studentService.getStudentById(1L)).thenReturn(studentTest);
//        when(studentService.getStudentById(1L).canMakeAReservation()).thenReturn(true);
//
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/api/reservation/create")
//                        .contentType("application/json")
//                        .content(jsonContent.toString())
//                        .with(csrf()))
//                .andExpect(status().isBadRequest());
//    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create a reservation with no professor id returns bad request")
    void testCreateAReservationReturnsBadRequest() throws Exception {
        Subject subject = new Subject();
        Student student = new Student("frandedeseo@gmail.com","fran1234","John", "Doe",  "location", "123", AppUserSex.MALE);
        JSONObject jsonContent = new JSONObject();
        jsonContent.put("subjectId", 1);
        jsonContent.put("studentId", 1);
        jsonContent.put("day", LocalDate.of(2023, 1, 1));
        jsonContent.put("startingTime", LocalTime.of(12, 0));
        jsonContent.put("endingHour", LocalTime.of(13, 0));
        jsonContent.put("price", 100);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/reservation/create")
                        .contentType("application/json")
                        .content(jsonContent.toString())
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Create a reservation with no subject id returns bad request")
    void testCreateAReservationReturnsBadRequest2() throws Exception {
        Professor professor = new Professor("frandedeseo@gmail.com", "password1234", "Francisco", "de Deseo", "Recoleta", "3462663707", AppUserSex.MALE);;
        Student student = new Student("frandedeseo@gmail.com","fran1234","John", "Doe",  "location", "123", AppUserSex.MALE);
        JSONObject jsonContent = new JSONObject();
        jsonContent.put("professorId", 1);
        jsonContent.put("studentId", 1);
        jsonContent.put("day", LocalDate.of(2023, 1, 1));
        jsonContent.put("startingTime", LocalTime.of(12, 0));
        jsonContent.put("endingHour", LocalTime.of(13, 0));
        jsonContent.put("price", 100);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/reservation/create")
                        .contentType("application/json")
                        .content(jsonContent.toString())
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Cancel a reservation with no Class Reservation id returns bad request")
    void testCancelAReservationReturnsBadRequest() throws Exception {
        JSONObject jsonContent = new JSONObject();
        jsonContent.put("id", null);
        jsonContent.put("role", "STUDENT");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/reservation/cancel")
                        .contentType("application/json")
                        .content(jsonContent.toString())
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Cancel a reservation return 200 OK")
    void testCancelAReservationReturnsOk() throws Exception {
        JSONObject jsonContent = new JSONObject();
        jsonContent.put("id", 1);
        jsonContent.put("role", "STUDENT");

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/reservation/cancel")
                        .contentType("application/json")
                        .content(jsonContent.toString())
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Create a reservation with no student id returns bad request")
    void testCreateAReservationReturnsBadRequest3() throws Exception {
        Professor professor = new Professor("frandedeseo@gmail.com", "password1234", "Francisco", "de Deseo", "Recoleta", "3462663707", AppUserSex.MALE);;
        Subject subject = new Subject();
        JSONObject jsonContent = new JSONObject();
        jsonContent.put("professorId", 1);
        jsonContent.put("subjectId", 1);
        jsonContent.put("day", LocalDate.of(2023, 1, 1));
        jsonContent.put("startingTime", LocalTime.of(12, 0));
        jsonContent.put("endingHour", LocalTime.of(13, 0));
        jsonContent.put("price", 100);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/reservation/create")
                        .contentType("application/json")
                        .content(jsonContent.toString())
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Create unavailable reservation")
    void testCreateUnavailableReservationReturnsOk() throws Exception {
        Professor professor = new Professor("frandedeseo@gmail.com", "password1234", "Francisco", "de Deseo", "Recoleta", "3462663707", AppUserSex.MALE);;
        JSONObject jsonContent = new JSONObject();
        jsonContent.put("professorId", 1);
        jsonContent.put("day", LocalDate.of(2023, 1, 1));
        jsonContent.put("startingTime", LocalTime.of(12, 0));
        jsonContent.put("endingHour", LocalTime.of(13, 0));

        when(professorService.getProfessorById(1L)).thenReturn(professor);
        when(classReservationService.createUnavailableReservation(professor, LocalDate.of(2023, 1, 1), LocalTime.of(12, 0), LocalTime.of(13, 0))).thenReturn(classReservationTest1);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/reservation/createUnavailable")
                        .contentType("application/json")
                        .content(jsonContent.toString())
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Create a reservation without authentication")
    void testCreateAReservationReturnsUnauthorized() throws Exception {
        JSONObject jsonContent = new JSONObject();
        jsonContent.put("professorId", 1);
        jsonContent.put("day", LocalDate.of(2023, 1, 1));
        jsonContent.put("startingTime", LocalTime.of(12, 0));
        jsonContent.put("endingHour", LocalTime.of(13, 0));
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/reservation/create")
                        .contentType("application/json")
                        .content(jsonContent.toString())
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }

//    @Test
//    @WithMockUser
//    @DisplayName("Create multiple unavailable reservations")
//    void testCreateMultipleUnavailableReservationsReturnsOk() throws Exception {
//        Professor professor = new Professor("frandedeseo@gmail.com", "password1234", "Francisco", "de Deseo", "Recoleta", "3462663707", AppUserSex.MALE);;
//        JSONObject jsonContent = new JSONObject();
//        jsonContent.put("professorId", 1);
//        jsonContent.put("day", LocalDate.of(2023, 1, 1));
//        jsonContent.put("startingHour", LocalTime.of(12, 0));
//        jsonContent.put("endingHour", LocalTime.of(13, 0));
//        jsonContent.put("duration", 1.0);
//        List<ClassReservation> classReservationList = new ArrayList<>();
//        classReservationList.add(new ClassReservation());
//        classReservationList.add(new ClassReservation());
//        when(professorService.getProfessorById(1L)).thenReturn(professor);
//        when(classReservationService.createMultipleUnavailableReservationsFor(professor, LocalDate.of(2023, 1, 1), LocalTime.of(12, 0), LocalTime.of(13, 0))).thenReturn(classReservationList);
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/api/reservation/createMultipleUnavailable")
//                        .contentType("application/json")
//                        .content(jsonContent.toString())
//                        .with(csrf()))
//                .andExpect(status().isOk());
//    }

//    @Test
//    @WithMockUser
//    @DisplayName("Create multiple unavailable reservations with no professor id returns bad request")
//    void testCreateMultipleUnavailableReservationsReturnsBadRequest() throws Exception {
//        JSONObject jsonContent = new JSONObject();
//        jsonContent.put("day", LocalDate.of(2023, 1, 1));
//        jsonContent.put("startingHour", LocalTime.of(12, 0));
//        jsonContent.put("endingHour", LocalTime.of(13, 0));
//        jsonContent.put("duration", 1.0);
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/api/reservation/createMultipleUnavailable")
//                        .contentType("application/json")
//                        .content(jsonContent.toString())
//                        .with(csrf()))
//                .andExpect(status().isBadRequest());
//    }

//    @Test
//    @WithMockUser
//    @DisplayName("Create multiple unavailable reservations with no day returns bad request")
//    void testCreateMultipleUnavailableReservationsReturnsBadRequest2() throws Exception {
//        JSONObject jsonContent = new JSONObject();
//        jsonContent.put("professorId", 1);
//        jsonContent.put("startingHour", LocalTime.of(12, 0));
//        jsonContent.put("endingHour", LocalTime.of(13, 0));
//        jsonContent.put("duration", 1.0);
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/api/reservation/createMultipleUnavailable")
//                        .contentType("application/json")
//                        .content(jsonContent.toString())
//                        .with(csrf()))
//                .andExpect(status().isBadRequest());
//    }

//    @Test
//    @WithMockUser
//    @DisplayName("Create multiple unavailable reservations with no starting hour returns bad request")
//    void testCreateMultipleUnavailableReservationsReturnsBadRequest3() throws Exception {
//        JSONObject jsonContent = new JSONObject();
//        jsonContent.put("professorId", 1);
//        jsonContent.put("day", LocalDate.of(2023, 1, 1));
//        jsonContent.put("endingHour", LocalTime.of(13, 0));
//        jsonContent.put("duration", 1.0);
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/api/reservation/createMultipleUnavailable")
//                        .contentType("application/json")
//                        .content(jsonContent.toString())
//                        .with(csrf()))
//                .andExpect(status().isBadRequest());
//    }

//    @Test
//    @WithMockUser
//    @DisplayName("Create multiple unavailable reservations with no ending hour returns bad request")
//    void testCreateMultipleUnavailableReservationsReturnsBadRequest4() throws Exception {
//        JSONObject jsonContent = new JSONObject();
//        jsonContent.put("professorId", 1);
//        jsonContent.put("day", LocalDate.of(2023, 1, 1));
//        jsonContent.put("startingHour", LocalTime.of(12, 0));
//        jsonContent.put("duration", 1.0);
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/api/reservation/createMultipleUnavailable")
//                        .contentType("application/json")
//                        .content(jsonContent.toString())
//                        .with(csrf()))
//                .andExpect(status().isBadRequest());
//    }

//    @Test
//    @WithMockUser
//    @DisplayName("Create multiple unavailable reservations with no duration of reservations returns bad request")
//    void testCreateMultipleUnavailableReservationsReturnsBadRequest5() throws Exception {
//        JSONObject jsonContent = new JSONObject();
//        jsonContent.put("professorId", 1);
//        jsonContent.put("day", LocalDate.of(2023, 1, 1));
//        jsonContent.put("startingHour", LocalTime.of(12, 0));
//        jsonContent.put("endingHour", LocalTime.of(13, 0));
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/api/reservation/createMultipleUnavailable")
//                        .contentType("application/json")
//                        .content(jsonContent.toString())
//                        .with(csrf()))
//                .andExpect(status().isBadRequest());
//    }

    @Test
    @DisplayName("Get custom day summary for admin without authentication")
    void testGetTodaysSummaryReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/reservation/customDaySummary")
                        .param("day", "2023-01-01"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("Get custom day with no day provided")
    void testGetTodaysSummaryReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/reservation/customDaySummary"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Get today's summary for admin")
    void testGetTodaysSummaryReturnsOk2() throws Exception {
        List<ProfessorDailySummary> professorDailySummaryList = new ArrayList<>();
        when(classReservationService.getDailySummary(LocalDate.now())).thenReturn(professorDailySummaryList);
        mockMvc.perform(get("/api/reservation/todaySummary"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get today's summary for admin without authentication")
    void testGetTodaysSummaryReturnsUnauthorized2() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/reservation/todaySummary"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser
    @DisplayName("Get all reservations by professor id with no professor id provided")
    void testGetAllReservationsByProfessorIdReturnsBadRequest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/reservation/findByProfessor"))
                .andExpect(status().isBadRequest());
    }

//    @Test
//    @WithMockUser
//    @DisplayName("Make reservation with pending feedback return bad request")
//    void testMakeReservationWithPendingFeedbackReturnsBadRequest() throws Exception {
//        JSONObject jsonContent = new JSONObject();
//        jsonContent.put("professorId", 1);
//        jsonContent.put("subjectId", 1);
//        jsonContent.put("studentId", 1);
//        jsonContent.put("day", LocalDate.of(2023, 1, 1));
//        jsonContent.put("startingTime", LocalTime.of(12, 0));
//        jsonContent.put("endingHour", LocalTime.of(13, 0));
//        jsonContent.put("price", 100);
//
//        studentTest.setPendingClassesFeedbacks(new ArrayList<>());
//        when(studentService.getStudentById(1L)).thenReturn(studentTest);
//        when(studentService.getStudentById(1L).canMakeAReservation()).thenReturn(false);
//
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/api/reservation/create")
//                        .contentType("application/json")
//                        .content(jsonContent.toString())
//                        .with(csrf()))
//                .andExpect(status().isBadRequest());
//    }

}
