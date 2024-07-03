package com.losmessias.leherer.controller_tests;

import com.losmessias.leherer.controller.ProfessorSubjectController;
import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.ProfessorSubject;
import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.domain.enumeration.SubjectStatus;
import com.losmessias.leherer.dto.SubjectRequestDto;
import com.losmessias.leherer.service.*;
import com.losmessias.leherer.role.AppUserSex;
import org.json.JSONArray;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
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
    @MockBean
    private JwtService jwtService;
    @MockBean
    private NotificationService notificationService;

    @Mock
    private SubjectRequestDto subjectRequestDtoMock;

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    @DisplayName("Create a professor-subject association")
    void testCreateProfessorSubjectAssociationReturnsOk() throws Exception {
        when(professorService.getProfessorById(1L)).thenReturn(new Professor());
        when(subjectService.getSubjectById(1L)).thenReturn(new Subject());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/professor-subject/createAssociation")
                        .param("professorId", "1")
                        .param("subjectId", "1")
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    @DisplayName("Create a professor-subject association finds no professor")
    void testCreateProfessorSubjectAssociationReturnsNotFoundProfessor() throws Exception {
        when(professorService.getProfessorById(1L)).thenReturn(null);
        when(subjectService.getSubjectById(1L)).thenReturn(new Subject());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/professor-subject/createAssociation")
                        .param("professorId", "1")
                        .param("subjectId", "1")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    @DisplayName("Create a professor-subject association finds no subject")
    void testCreateProfessorSubjectAssociationReturnsNotFoundSubject() throws Exception {
        when(professorService.getProfessorById(1L)).thenReturn(new Professor());
        when(subjectService.getSubjectById(1L)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/professor-subject/createAssociation")
                        .param("professorId", "1")
                        .param("subjectId", "1")
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    @DisplayName("Find professor-subjects by status")
    void testFindProfessorSubjectsByStatusReturnsOk() throws Exception {
        List<ProfessorSubject> professorSubjects = new ArrayList<>();
        professorSubjects.add(new ProfessorSubject());
        professorSubjects.add(new ProfessorSubject());
        when(professorSubjectService.findByStatus(SubjectStatus.PENDING)).thenReturn(professorSubjects);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/professor-subject/findByStatus")
                        .param("status", "PENDING"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    @DisplayName("Find professor-subjects by status finds none")
    void testFindProfessorSubjectsByStatusReturnsNotFound() throws Exception {
        when(professorSubjectService.findByStatus(SubjectStatus.PENDING)).thenReturn(new ArrayList<>());
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/professor-subject/findByStatus")
                        .param("status", "PENDING"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    @DisplayName("Approve professor-subjects returns ok")
    void testApproveProfessorSubjectsReturnsOk() throws Exception {
        Professor professor = new Professor("frandedeseo@gmail.com", "password1234", "Francisco", "de Deseo", "Recoleta", "3462663707", AppUserSex.MALE);;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("professorId", 1L);
        jsonObject.put("subjectIds", new JSONArray(List.of(1L)));
        when(professorService.getProfessorById(any())).thenReturn(professor);
        when(subjectService.getSubjectById(any())).thenReturn(new Subject());
        when(professorSubjectService.findByProfessorAndSubject(any(), any())).thenReturn(new ProfessorSubject());
        when(professorSubjectService.changeStatusOf(any(), any())).thenReturn(new ProfessorSubject());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/professor-subject/approve")
                        .contentType("application/json")
                        .content(jsonObject.toString())
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    @DisplayName("Approve professor-subjects finds no professor")
    void testApproveProfessorSubjectsReturnsNotFoundProfessor() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("professorId", 1L);
        jsonObject.put("subjectIds", new JSONArray(List.of(1L)));
        when(professorService.getProfessorById(any())).thenReturn(null);
        when(subjectService.getSubjectById(any())).thenReturn(new Subject());
        when(professorSubjectService.findByProfessorAndSubject(any(), any())).thenReturn(new ProfessorSubject());
        when(professorSubjectService.changeStatusOf(any(), any())).thenReturn(new ProfessorSubject());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/professor-subject/approve")
                        .contentType("application/json")
                        .content(jsonObject.toString())
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    @DisplayName("Approve professor-subjects finds no subject")
    void testApproveProfessorSubjectsReturnsNotFoundSubject() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("professorId", 1L);
        jsonObject.put("subjectIds", new JSONArray(List.of(1L)));
        when(professorService.getProfessorById(any())).thenReturn(new Professor());
        when(subjectService.getSubjectById(any())).thenReturn(null);
        when(professorSubjectService.findByProfessorAndSubject(any(), any())).thenReturn(new ProfessorSubject());
        when(professorSubjectService.changeStatusOf(any(), any())).thenReturn(new ProfessorSubject());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/professor-subject/approve")
                        .contentType("application/json")
                        .content(jsonObject.toString())
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    @DisplayName("Approve professor-subjects finds no professor-subject")
    void testApproveProfessorSubjectsReturnsNotFoundProfessorSubject() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("professorId", 1L);
        jsonObject.put("subjectIds", new JSONArray(List.of(1L)));
        when(professorService.getProfessorById(any())).thenReturn(new Professor());
        when(subjectService.getSubjectById(any())).thenReturn(new Subject());
        when(professorSubjectService.findByProfessorAndSubject(any(), any())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/professor-subject/approve")
                        .contentType("application/json")
                        .content(jsonObject.toString())
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    @DisplayName("Reject professor-subjects returns ok")
    void testRejectProfessorSubjectsReturnsOk() throws Exception {
        Professor professor = new Professor("frandedeseo@gmail.com", "password1234", "Francisco", "de Deseo", "Recoleta", "3462663707", AppUserSex.MALE);;
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("professorId", 1L);
        jsonObject.put("subjectIds", new JSONArray(List.of(1L)));
        when(professorService.getProfessorById(any())).thenReturn(professor);
        when(subjectService.getSubjectById(any())).thenReturn(new Subject());
        when(professorSubjectService.findByProfessorAndSubject(any(), any())).thenReturn(new ProfessorSubject());
        when(professorSubjectService.changeStatusOf(any(), any())).thenReturn(new ProfessorSubject());
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/professor-subject/reject")
                        .contentType("application/json")
                        .content(jsonObject.toString())
                        .with(csrf()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    @DisplayName("Reject professor-subjects finds no professor")
    void testRejectProfessorSubjectsReturnsNotFoundProfessor() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("professorId", 1L);
        jsonObject.put("subjectIds", new JSONArray(List.of(1L)));
        when(professorService.getProfessorById(any())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/professor-subject/reject")
                        .contentType("application/json")
                        .content(jsonObject.toString())
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    @DisplayName("Reject professor-subjects finds no subject")
    void testRejectProfessorSubjectsReturnsNotFoundSubject() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("professorId", 1L);
        jsonObject.put("subjectIds", new JSONArray(List.of(1L)));
        when(professorService.getProfessorById(any())).thenReturn(new Professor());
        when(subjectService.getSubjectById(any())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/professor-subject/reject")
                        .contentType("application/json")
                        .content(jsonObject.toString())
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username="admin",roles={"ADMIN"})
    @DisplayName("Reject professor-subjects finds no professor-subject")
    void testRejectProfessorSubjectsReturnsNotFoundProfessorSubject() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("professorId", 1L);
        jsonObject.put("subjectIds", new JSONArray(List.of(1L)));
        when(professorService.getProfessorById(any())).thenReturn(new Professor());
        when(subjectService.getSubjectById(any())).thenReturn(new Subject());
        when(professorSubjectService.findByProfessorAndSubject(any(), any())).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/professor-subject/reject")
                        .contentType("application/json")
                        .content(jsonObject.toString())
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
