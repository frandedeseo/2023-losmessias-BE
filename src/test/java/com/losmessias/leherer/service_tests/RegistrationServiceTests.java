package com.losmessias.leherer.service_tests;

import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.dto.AuthenticationResponse;
import com.losmessias.leherer.dto.ForgotPasswordDto;
import com.losmessias.leherer.dto.RegistrationProfessorRequest;
import com.losmessias.leherer.domain.*;
import com.losmessias.leherer.dto.RegistrationRequest;
import com.losmessias.leherer.ext_interface.EmailSender;
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
    private AppUserService appUserService;
    @Mock
    private ProfessorSubjectService professorSubjectService;
    @Mock
    private ConfirmationTokenService confirmationTokenService;
    @Mock
    private StudentService studentService;
    @Mock
    private ProfessorService professorService;
    @Mock
    private EmailService emailService;
    @Mock
    private JwtService jwtService;
    @InjectMocks
    private RegistrationService registrationService;
    @Mock
    private Environment env;
    private RegistrationRequest request2;
    private RegistrationProfessorRequest request1;
    private Professor professor1;
    private Student student1;
    private AppUser user1;
    private Environment environment;

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
                "Male",
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
                "Male"
        );

        professor1 = new Professor();
        student1 = new Student();
        user1 = new AppUser(
                request1.getEmail(),
                request1.getPassword(),
                AppUserRole.PROFESSOR,
                2L
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

    //    @Test
//    @DisplayName("Confirm the registration by the email token")
//    void testConfirmEmailToken() {
//        String token = "token";
//
//        when(confirmationTokenService.validateToken(token)).thenReturn(new ConfirmationToken(
//                "token", LocalDateTime.now(), LocalDateTime.now(), user1
//        ));
//        AuthenticationResponse authenticationResponseExpected = AuthenticationResponse.builder()
//                .token("jwtToken")
//                .build();
//        when(jwtService.generateToken(any())).thenReturn("jwtToken");
//        AuthenticationResponse authenticationResponse = registrationService.confirmEmailToken("token");
//
//        assertEquals(authenticationResponseExpected, authenticationResponse);
//    }
    @Test
    @DisplayName("Confirm the token for Password change was activated successfully")
    void testConfirmChangePasswordToken() {
        String message = registrationService.confirmChangePasswordToken("token");

        assertEquals("Email confirmed", message);
    }
/**
 @Test
 @DisplayName("The password was changed successfully")
 void testChangePassword(){
 ForgotPasswordDto request = new ForgotPasswordDto("fran@gmail.com", "fran123");

 when(appUserService.encodePassword(any())).thenReturn("password");
 String message = registrationService.changePassword(request);
 assertEquals(message, "Password changed successfully");
 }
 */
}