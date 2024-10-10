package com.losmessias.leherer.controller_tests;


import com.losmessias.leherer.controller.ProfessorController;
import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.enumeration.AppUserSex;
import com.losmessias.leherer.service.AppUserService;
import com.losmessias.leherer.service.ClassReservationService;
import com.losmessias.leherer.service.JwtService;
import com.losmessias.leherer.service.ProfessorService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProfessorController.class)
public class AppUserControllerTests {


/*    @Autowired
    MockMvc mockMvc;

    @MockBean
    private AppUserService appUserService;
    @MockBean
    private ClassReservationService classReservationService;
    @MockBean
    private JwtService jwtService;

    @Mock
    private Professor professorTest;

    @Mock
    private Student professorS;*/
}
