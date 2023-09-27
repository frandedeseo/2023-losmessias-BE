package com.losmessias.leherer.controller_tests;

import com.losmessias.leherer.controller.SubjectController;
import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.service.SubjectService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(SubjectController.class)
public class SubjectControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private SubjectService subjectService;

    @Test
    @WithMockUser
    @DisplayName("Get all subjects")
    void testGetAllSubjectsReturnsOk() throws Exception {
        when(subjectService.getAllSubjects()).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/subject/all"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get all subjects without authentication")
    void testGetAllSubjectsReturnsUnauthorized() throws Exception {
        when(subjectService.getAllSubjects()).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/subject/all"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("Get subject by id")
    void testGetSubjectByIdReturnsOk() throws Exception {
        when(subjectService.getSubjectById(1L)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/subject/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get subject by id without authentication")
    void testGetSubjectByIdReturnsUnauthorized() throws Exception {
        when(subjectService.getSubjectById(1L)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/subject/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("Create subject")
    void testCreateSubjectReturnsOk() throws Exception {
        JSONObject jsonContent = new JSONObject();
        jsonContent.put("name", "Math");
        when(subjectService.create(any())).thenReturn(new Subject());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/subject/create")
                        .contentType("application/json")
                        .content(jsonContent.toString())
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Create subject without authentication")
    void testCreateSubjectReturnsUnauthorized() throws Exception {
        JSONObject jsonContent = new JSONObject();
        jsonContent.put("name", "Math");
        when(subjectService.create(any())).thenReturn(new Subject());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/subject/create")
                        .contentType("application/json")
                        .content(jsonContent.toString())
                        .with(csrf()))
                .andExpect(status().isUnauthorized());
    }
}
