package com.losmessias.leherer.controller_tests;

import com.losmessias.leherer.controller.FeedbackController;
import com.losmessias.leherer.service.FeedbackService;
import com.losmessias.leherer.service.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(FeedbackController.class)
public class FeedbackControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private FeedbackController feedbackController;
    @MockBean
    private FeedbackService feedbackService;
    @MockBean
    private JwtService jwtService;

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

}
