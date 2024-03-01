package com.losmessias.leherer.service_tests;

import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.dto.AuthenticationResponse;
import com.losmessias.leherer.dto.ForgotPasswordDto;
import com.losmessias.leherer.dto.RegistrationProfessorRequest;
import com.losmessias.leherer.domain.*;
import com.losmessias.leherer.dto.RegistrationRequest;
import com.losmessias.leherer.ext_interface.EmailSender;
import com.losmessias.leherer.role.AppUserSex;
import com.losmessias.leherer.service.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTests {
    @Mock
    private StudentService studentService;
    @Mock
    private ProfessorService professorService;
    @InjectMocks
    private RegistrationService registrationService;
    private RegistrationRequest request2;
    private RegistrationProfessorRequest request1;
    private Professor professor1;
    private Student student1;

    @BeforeEach
    void setUp() {
        List<Subject> subjects = new ArrayList<Subject>();
        subjects.add(new Subject("Math"));
        subjects.add(new Subject("History"));
        request1 = new RegistrationProfessorRequest(
                "Francisco",
                "de Deseö",
                "fran@gmail.com",
                "fran123456",
                "Professor",
                "Recoleta",
                "543462663707",
                AppUserSex.MALE,
                subjects

        );
        request2 = new RegistrationRequest(
                "Francisco",
                "de Deseö",
                "fran@gmail.com",
                "fran123456",
                "Student",
                "Recoleta",
                "543462663707",
                AppUserSex.MALE
        );

        professor1 = new Professor();
        student1 = new Student();
        Student student = new Student(
                "fran@gmail.com",
                "fran123",
                "Francisco",
                "de Deseo",
                "Ayacucho 1822",
                "+54 3462 663707",
                com.losmessias.leherer.role.AppUserSex.MALE
        );
    }

    @Test
    @DisplayName("Professor registration is successful")
    void testRegistrationProfessorIsDoneCorrectly() {
        when(professorService.saveProfessor(any())).thenReturn(professor1);
        when(professorService.saveProfessor(any())).thenReturn(professor1);
        String message = registrationService.registerProfessor(request1);
        assertEquals("Successful Registration", message);

    }

    @Test
    @DisplayName("Student registration is successful")
    void testRegistrationStudentIsDoneCorrectly() {

        when(studentService.create(any())).thenReturn(student1);
        String message = registrationService.register(request2);
        assertEquals("Successful Registration", message);

    }

    @Test
    @DisplayName("Confirm the token for Password change was activated successfully")
    void testConfirmChangePasswordToken() {
        String message = registrationService.confirmChangePasswordToken("token");

        assertEquals("Email confirmed", message);

    }
}