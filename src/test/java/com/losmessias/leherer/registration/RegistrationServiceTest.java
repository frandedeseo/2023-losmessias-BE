//package com.losmessias.leherer.registration;
//
//import com.losmessias.leherer.domain.AppUser;
//import com.losmessias.leherer.domain.EmailValidator;
//import com.losmessias.leherer.dto.RegistrationRequest;
//import com.losmessias.leherer.role.AppUserRole;
//import com.losmessias.leherer.service.*;
//import com.losmessias.leherer.ext_interface.EmailSender;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.verify;
//@DataJpaTest
//@ExtendWith(MockitoExtension.class)
//class RegistrationServiceTest {
//    private RegistrationService serviceTestWithMocks;
//    private RegistrationService serviceTestWithOutMocks;
//    private RegistrationRequest request;
//    private AppUser user1;
//    @Mock
//    private AppUserService appUserService;
//    @Mock private EmailValidator emailValidator;
//    @Mock private ConfirmationTokenService confirmationTokenService;
//    @Mock private EmailSender emailSender;
//    @Mock private StudentService studentService;
//    @Mock private ProfessorService professorService;
//
//    @Autowired
//    private AppUserService appUserService2;
//    @Autowired
//    private ConfirmationTokenService confirmationTokenService2;
//
//    @BeforeEach
//    void setUp(){
//        serviceTestWithMocks =  new RegistrationService(appUserService, emailValidator, confirmationTokenService, emailSender, studentService, professorService);
//        request = new RegistrationRequest(
//                "Francisco",
//                "de Deseö",
//                "fran@gmail.com",
//                "fran123456",
//                "Teacher"
//        );
//        user1 = new AppUser(
//                request.getFirstName(),
//                request.getLastName(),
//                request.getEmail(),
//                request.getPassword(),
//                AppUserRole.USER,
//                1L
//        );
//        serviceTestWithOutMocks = new RegistrationService(appUserService2, emailValidator, confirmationTokenService2, emailSender, studentService, professorService);
//    }
//    @Test
//    void testRegistrationIsDoneCorrectly() {
//
//        String message = serviceTestWithMocks.register(request);
//
//        ArgumentCaptor<AppUser> appUserArgumentCaptor = ArgumentCaptor.forClass(AppUser.class);
//
//        verify(appUserService).signUpUser(appUserArgumentCaptor.capture());
//
//        AppUser capturedAppUser = appUserArgumentCaptor.getValue();
//
//        assertEquals(capturedAppUser, user1);
//        assertEquals("Successful Registration", message);
//
//    }
//    @Test
//    void confirmToken() {
//        serviceTestWithOutMocks.register(request);
//    }
//}