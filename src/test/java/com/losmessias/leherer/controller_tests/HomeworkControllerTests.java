package com.losmessias.leherer.controller_tests;

import com.losmessias.leherer.controller.HomeworkController;
import com.losmessias.leherer.domain.Homework;
import com.losmessias.leherer.service.HomeworkService;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = HomeworkController.class)
public class HomeworkControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private HomeworkService homeworkService;
    @MockBean
    private JwtService jwtService;

    @Mock
    private Homework homeworkTest1;
    @Mock
    private Homework homeworkTest2;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Get all homeworks")
    void testGetAllHomeworks() throws Exception {
        List<Homework> homeworkList = homeworkService.getAllHomeworks();
        homeworkList.add(homeworkTest1);
        homeworkList.add(homeworkTest2);

        when(homeworkService.getAllHomeworks()).thenReturn(homeworkList);

        mockMvc.perform(get("/api/homework/all"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Get all homeworks without authentication")
    void testGetAllHomeworksWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/homework/all"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Get all homeworks finds none")
    void testGetAllHomeworksFindsNone() throws Exception {
        when(homeworkService.getAllHomeworks()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/homework/all"))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    assert (result.getResponse().getContentAsString().equals("No homeworks found"));
                });
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Get homework by id")
    void testGetHomeworkById() throws Exception {
        when(homeworkService.getHomeworkById(1L)).thenReturn(homeworkTest1);

        mockMvc.perform(get("/api/homework/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Get homework by id finds none")
    void testGetHomeworkByIdFindsNone() throws Exception {
        when(homeworkService.getHomeworkById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/homework/1"))
                .andExpect(status().isNotFound())
                .andExpect(result -> {
                    assert (result.getResponse().getContentAsString().equals("No homework found with id 1"));
                });
    }

    @Test
    @DisplayName("Get homework by id without authentication")
    void testGetHomeworkByIdWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/homework/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Get homework by id with invalid id")
    void testGetHomeworkByIdWithInvalidId() throws Exception {
        mockMvc.perform(get("/api/homework/-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create homework")
    void testCreateHomework() throws Exception {
        JSONObject jsonContent = new JSONObject();
        LocalDateTime deadline = LocalDateTime.now().plusDays(1);
        jsonContent.put("assignment", "Assignment");
        jsonContent.put("deadline", deadline);
        jsonContent.put("classReservationId", 1L);
        jsonContent.put("professorId", 1L);
        jsonContent.put("files", null);

        when(homeworkService.createHomework(
                deadline,
                "Assignment",
                1L,
                1L,
                null
        )).thenReturn(homeworkTest1);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/homework/create")
                        .contentType("application/json")
                        .content(jsonContent.toString())
                        .with(csrf()))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create homework with invalid deadline")
    void testCreateHomeworkWithInvalidDeadline() throws Exception {
        JSONObject jsonContent = new JSONObject();
        LocalDateTime deadline = LocalDateTime.now().minusDays(1);
        jsonContent.put("assignment", "Assignment");
        jsonContent.put("deadline", deadline);
        jsonContent.put("classReservationId", 1L);
        jsonContent.put("professorId", 1L);
        jsonContent.put("files", null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/homework/create")
                        .contentType("application/json")
                        .content(jsonContent.toString())
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assert (result.getResponse().getContentAsString().equals("Deadline must be in the future"));
                });
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create homework with null assignment")
    void testCreateHomeworkWithNullAssignment() throws Exception {
        JSONObject jsonContent = new JSONObject();
        LocalDateTime deadline = LocalDateTime.now().plusDays(1);
        jsonContent.put("assignment", null);
        jsonContent.put("deadline", deadline);
        jsonContent.put("classReservationId", 1L);
        jsonContent.put("professorId", 1L);
        jsonContent.put("files", null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/homework/create")
                        .contentType("application/json")
                        .content(jsonContent.toString())
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assert (result.getResponse().getContentAsString().equals("Assignment must not be null"));
                });
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create homework with null class reservation")
    void testCreateHomeworkWithNullClassReservation() throws Exception {
        JSONObject jsonContent = new JSONObject();
        LocalDateTime deadline = LocalDateTime.now().plusDays(1);
        jsonContent.put("assignment", "Assignment");
        jsonContent.put("deadline", deadline);
        jsonContent.put("classReservationId", null);
        jsonContent.put("professorId", 1L);
        jsonContent.put("files", null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/homework/create")
                        .contentType("application/json")
                        .content(jsonContent.toString())
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assert (result.getResponse().getContentAsString().equals("Class reservation must not be null"));
                });
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create homework with null professor id")
    void testCreateHomeworkWithNullProfessorId() throws Exception {
        JSONObject jsonContent = new JSONObject();
        LocalDateTime deadline = LocalDateTime.now().plusDays(1);
        jsonContent.put("assignment", "Assignment");
        jsonContent.put("deadline", deadline);
        jsonContent.put("classReservationId", 1L);
        jsonContent.put("professorId", null);
        jsonContent.put("files", null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/homework/create")
                        .contentType("application/json")
                        .content(jsonContent.toString())
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assert (result.getResponse().getContentAsString().equals("Professor id must not be null"));
                });
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @DisplayName("Create homework with null deadline")
    void testCreateHomeworkWithNullDeadline() throws Exception {
        JSONObject jsonContent = new JSONObject();
        LocalDateTime deadline = LocalDateTime.now().plusDays(1);
        jsonContent.put("assignment", "Assignment");
        jsonContent.put("deadline", null);
        jsonContent.put("classReservationId", 1L);
        jsonContent.put("professorId", 1L);
        jsonContent.put("files", null);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/homework/create")
                        .contentType("application/json")
                        .content(jsonContent.toString())
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assert (result.getResponse().getContentAsString().equals("Deadline must not be null"));
                });
    }
}
