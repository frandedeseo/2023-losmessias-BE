package com.losmessias.leherer.controller_tests;

import com.losmessias.leherer.controller.ClassReservationController;
import com.losmessias.leherer.domain.*;
import com.losmessias.leherer.dto.ClassReservationDto;
import com.losmessias.leherer.repository.ProfessorRepository;
import com.losmessias.leherer.repository.ProfessorSubjectRepository;
import com.losmessias.leherer.repository.StudentRepository;
import com.losmessias.leherer.repository.SubjectRepository;
import com.losmessias.leherer.service.ClassReservationService;
import com.losmessias.leherer.service.ProfessorService;
import com.losmessias.leherer.service.StudentService;
import com.losmessias.leherer.service.SubjectService;
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

import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
    private ProfessorSubjectRepository professorSubjectRepository;

    @Mock
    private ClassReservation classReservationTest;

    @Test
    @WithMockUser
    @DisplayName("Get all reservations")
    void testGetAllReservationsReturnsOk() throws Exception {
        when(classReservationService.getAllReservations()).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/reservation/all"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get all reservations without authentication")
    void testGetAllReservationsReturnsUnauthorized() throws Exception {
        when(classReservationService.getAllReservations()).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/reservation"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("Get reservation by id")
    void testGetReservationByIdReturnsOk() throws Exception {
        when(classReservationService.getReservationById(1L)).thenReturn(classReservationTest);
        when(classReservationTest.getProfessor()).thenReturn(new Professor());
        when(classReservationTest.getSubject()).thenReturn(new Subject());
        when(classReservationTest.getStudent()).thenReturn(new Student());
        when(classReservationTest.getDate()).thenReturn(null);
        when(classReservationTest.getStartingHour()).thenReturn(null);
        when(classReservationTest.getEndingHour()).thenReturn(null);
        when(classReservationTest.getPrice()).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/reservation/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get reservation by id without authentication")
    void testGetReservationByIdReturnsUnauthorized() throws Exception {
        when(classReservationService.getReservationById(1L)).thenReturn(classReservationTest);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/reservation/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("Create a reservation")
    void testCreateAReservationReturnsOk() throws Exception {
        ProfessorSubject professorSubject = new ProfessorSubject();
        professorSubject.setId(1L);
        when(professorSubjectRepository.findById(1L)).thenReturn(java.util.Optional.of(professorSubject));
        when(classReservationService.createReservationFrom(professorSubject, null, null, null, null, 100)).thenReturn(classReservationTest);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/reservation/create")
                        .contentType("application/json")
                        .content(
                                "{\"id\": null," +
                                        "\"professorSubjectId\": 1," +
                                        "\"studentId\": 1," +
                                        "\"day\": \"2023-01-01\"," +
                                        "\"startingTime\": \"12:00:00\"," +
                                        "\"endingTime\": \"13:00:00\"}")
                        .with(csrf()))
                .andExpect(status().isOk());
    }
}
