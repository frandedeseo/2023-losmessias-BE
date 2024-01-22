package com.losmessias.leherer.controller_tests;

import com.losmessias.leherer.controller.HomeworkController;
import com.losmessias.leherer.domain.Homework;
import com.losmessias.leherer.service.HomeworkService;
import com.losmessias.leherer.service.JwtService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
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
                .andExpect(result -> result.getResponse().getContentAsString().equals("No homeworks found"));
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
                .andExpect(result -> result.getResponse().getContentAsString().equals("No homework found with id 1"));
    }

}
