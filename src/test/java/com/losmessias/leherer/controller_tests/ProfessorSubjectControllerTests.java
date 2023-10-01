package com.losmessias.leherer.controller_tests;

import com.losmessias.leherer.controller.ProfessorSubjectController;
import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.ProfessorSubject;
import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.dto.SubjectRequestDto;
import com.losmessias.leherer.service.ProfessorService;
import com.losmessias.leherer.service.ProfessorSubjectService;
import com.losmessias.leherer.service.SubjectService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.parameters.P;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProfessorSubjectController.class)
public class ProfessorSubjectControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ProfessorSubjectService professorSubjectService;
    @MockBean
    private ProfessorService professorService;
    @MockBean
    private SubjectService subjectService;

    @Mock
    private SubjectRequestDto subjectRequestDtoMock;

    @Test
    @WithMockUser
    @DisplayName("Get all professor-subjects")
    void testGetAllProfessorSubjectsReturnsOk() throws Exception {
        when(professorSubjectService.getAllProfessorSubjects()).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/professor-subject/all"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get all professor-subjects without authentication")
    void testGetAllProfessorSubjectsReturnsUnauthorized() throws Exception {
        when(professorSubjectService.getAllProfessorSubjects()).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/professor-subject"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("Get a professor-subject by id")
    void testGetProfessorSubjectByIdReturnsOk() throws Exception {
        ProfessorSubject professorSubject = new ProfessorSubject();
        when(professorSubjectService.findById(1L)).thenReturn(professorSubject);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/professor-subject/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get a professor-subject by id without authentication")
    void testGetProfessorSubjectByIdReturnsUnauthorized() throws Exception {
        ProfessorSubject professorSubject = new ProfessorSubject();
        when(professorSubjectService.findById(1L)).thenReturn(professorSubject);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/professor-subject/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser
    @DisplayName("Create a professor-subject association")
    void testCreateProfessorSubjectAssociationReturnsOk() throws Exception {
        when(professorService.getProfessorById(1L)).thenReturn(null);
        when(subjectService.getSubjectById(1L)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/professor-subject/createAssociation")
                        .param("professorId", "1")
                        .param("subjectId", "1")
                        .with(csrf()))
                .andExpect(status().isOk());
    }

//    @Test
//    @WithMockUser
//    @DisplayName("Approve a professor-subject association")
//    void testApproveProfessorSubjectAssociationReturnsOk() throws Exception {
//        JSONObject jsonContent = new JSONObject();
//        JSONArray jsonArray = new JSONArray();
//        jsonArray.put(1L);
//        jsonContent.put("professorId", 1L);
//        jsonContent.put("subjectsId", jsonArray);
//        when(subjectRequestDtoMock.getSubjectIds()).thenReturn(List.of(1L));
//        when(subjectRequestDtoMock.getProfessorId()).thenReturn(1L);
//        when(professorSubjectService.findByProfessorAndSubject(Mockito.any(), Mockito.any())).thenReturn(null);
//        when(professorSubjectService.findByProfessorIdAndStatus(1L, null)).thenReturn(new ArrayList<>());
//
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/api/professor-subject/approve")
//                        .contentType("application/json")
//                        .content(jsonContent.toString())
//                        .with(csrf()))
//                .andExpect(status().isOk());
//    }

//    @Test
//    @WithMockUser
//    @DisplayName("Reject a professor-subject association")
//    void testRejectProfessorSubjectAssociationReturnsOk() throws Exception {
//        JSONObject jsonContent = new JSONObject();
//        JSONArray jsonArray = new JSONArray();
//        jsonArray.put(1L);
//        jsonContent.put("professorId", 1L);
//        jsonContent.put("subjectsId", jsonArray);
//        when(professorSubjectService.findByProfessorIdAndStatus(1L, null)).thenReturn(new ArrayList<>());
//
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/api/professor-subject/reject")
//                        .contentType("application/json")
//                        .content(jsonContent.toString())
//                        .with(csrf()))
//                .andExpect(status().isOk());
//    }

    @Test
    @WithMockUser
    @DisplayName("Find professor-subjects by professor")
    void testFindProfessorSubjectsByProfessorReturnsOk() throws Exception {
        when(professorService.getProfessorById(1L)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/professor-subject/findByProfessor/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Find professor-subjects by status")
    void testFindProfessorSubjectsByStatusReturnsOk() throws Exception {
        when(professorSubjectService.findByStatus(Mockito.any())).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/professor-subject/findByStatus")
                        .param("status", "PENDING"))
                .andExpect(status().isOk());
    }
}
