package com.losmessias.leherer.service_tests;

import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.domain.enumeration.AppUserSex;
import com.losmessias.leherer.dto.RegistrationProfessorRequest;
import com.losmessias.leherer.dto.RegistrationRequest;
import com.losmessias.leherer.service.*;
import org.hibernate.validator.constraints.ModCheck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegistrationServiceTests {

    @Mock
    private StudentService studentService;

    @Mock
    private ConfirmationTokenService confirmationTokenService;

    @Mock
    private AppUserService appUserService;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private ProfessorSubjectService professorSubjectService;

    @Mock
    private ProfessorService professorService;

    @Mock
    private Environment environment;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private RegistrationService registrationService;

    private RegistrationRequest studentRequest;
    private RegistrationProfessorRequest professorRequest;
    private Professor professor;
    private Student student;

    @BeforeEach
    void setUp() {
        List<Subject> subjects = new ArrayList<>();
        subjects.add(new Subject("Math"));
        subjects.add(new Subject("History"));

        professorRequest = new RegistrationProfessorRequest(
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

        studentRequest = new RegistrationRequest(
                "Francisco",
                "de Deseö",
                "fran@gmail.com",
                "fran123456",
                "Student",
                "Recoleta",
                "543462663707",
                AppUserSex.MALE
        );

        professor = new Professor("frandedeseo@gmail.com", "password1234", "Francisco", "de Deseo", "Recoleta", "3462663707", AppUserSex.MALE);
        student = new Student("frandedeseo@gmail.com", "fran1234", "John", "Doe", "location", "123", AppUserSex.MALE);
    }

    @Test
    @DisplayName("Professor registration is successful")
    void testProfessorRegistrationIsDoneCorrectly() {
        when(professorService.saveProfessor(any())).thenReturn(professor);

        String message = registrationService.registerProfessor(professorRequest);

        assertEquals("Successful Registration", message);
    }

    @Test
    @DisplayName("Student registration is successful")
    void testStudentRegistrationIsDoneCorrectly() {
        when(studentService.create(any())).thenReturn(student);

        String message = registrationService.register(studentRequest);

        assertEquals("Successful Registration", message);
    }

    @Test
    @DisplayName("Confirm the token for Password change was activated successfully")
    void testConfirmChangePasswordToken() {
        String message = registrationService.confirmChangePasswordToken("token");

        assertEquals("Email confirmed", message);
    }
}