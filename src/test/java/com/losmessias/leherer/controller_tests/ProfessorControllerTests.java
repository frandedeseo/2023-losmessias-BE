package com.losmessias.leherer.controller_tests;

import com.losmessias.leherer.controller.ProfessorController;
import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.service.ProfessorService;
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
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProfessorController.class)
public class ProfessorControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ProfessorService professorService;

    @Mock
    private Professor professorTest;

    @Test
    @WithMockUser
    @DisplayName("Get all professors")
    void testGetAllProfessorsReturnsOk() throws Exception {
        when(professorService.getAllProfessors()).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/professor/all"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get all professors without authentication")
    void testGetAllProfessorsReturnsUnauthorized() throws Exception {
        when(professorService.getAllProfessors()).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/professor"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("Get all professors returns array of professors")
    void testGetAllProfessorsReturnArrayOfProfessors() throws Exception {
        List<Professor> professors = new ArrayList<Professor>();
        professors.add(new Professor("John", "Doe", "mail", "ubication", "phone"));
        professors.add(new Professor("Jane", "Doe", "mail", "ubication", "phone"));
        when(professorService.getAllProfessors()).thenReturn(professors);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/professor/all"))
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    assert (json.contains("John"));
                    assert (json.contains("Jane"));
                })
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Save a professor")
    void testSaveAProfessorReturnsOk() throws Exception {
        Professor professor = new Professor("John", "Doe", "mail", "ubication", "phone");

        when(professorService.saveProfessor(professor)).thenReturn(professorTest);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/professor/register")
                        .contentType("application/json")
                        .content(
                                "{\"id\": null," +
                                        "\"firstName\": \"John\"," +
                                        "\"lastName\": \"Doe\"" +
                                        "}")
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    @DisplayName("Save a professor returns bad request at id")
    void testSaveAProfessorReturnsBadRequest() throws Exception {
        Professor professor = new Professor("John", "Doe", "mail", "ubication", "phone");
        when(professorService.saveProfessor(professor)).thenReturn(professor);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/professor/register")
                        .contentType("application/json")
                        .content(
                                "{\"id\": 1," +
                                        "\"firstName\": \"John\"," +
                                        "\"lastName\": \"Doe\"" +
                                        "}")
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    @DisplayName("Update a professor")
    void testChangingProfessorReturnsNotFound() throws Exception {
        Professor professor = new Professor("John", "Doe", "mail", "ubication", "phone");
        professorService.saveProfessor(professor);
        when(professorService.saveProfessor(professor)).thenReturn(professor);
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/professor/update/1")
                        .contentType("application/json")
                        .content(
                                "{\"id\": 1," +
                                        "\"firstName\": \"John\"," +
                                        "\"lastName\": \"Doe\"" +
                                        "}")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}