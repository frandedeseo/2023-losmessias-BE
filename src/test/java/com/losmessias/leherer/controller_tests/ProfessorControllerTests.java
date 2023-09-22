package com.losmessias.leherer.controller_tests;

import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.service.ProfessorService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest
public class ProfessorControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ProfessorService professorService;

    @Test
    @WithMockUser
    void testGetAllProfessorsReturnsOk() throws Exception {
        when(professorService.getAllProfessors()).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/professor"))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAllProfessorsReturnsUnauthorized() throws Exception {
        when(professorService.getAllProfessors()).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/professor"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    void testGetAllProfessorsReturnArrayOfProfessors() throws Exception {
        List<Professor> professors = new ArrayList<Professor>();
        professors.add(new Professor("John", "Doe"));
        professors.add(new Professor("Jane", "Doe"));
        when(professorService.getAllProfessors()).thenReturn(professors);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/professor"))
                .andExpect(result -> {
                    String json = result.getResponse().getContentAsString();
                    System.out.println(json);
                    assert (json.contains("John"));
                    assert (json.contains("Jane"));
                })
                .andExpect(status().isOk());
    }
}