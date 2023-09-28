package com.losmessias.leherer.registration;

import com.losmessias.leherer.appuser.AppUser;
import com.losmessias.leherer.appuser.AppUserRepository;
import com.losmessias.leherer.appuser.AppUserRole;
import com.losmessias.leherer.appuser.AppUserService;
import com.losmessias.leherer.email.EmailSender;
import com.losmessias.leherer.registration.token.ConfirmationTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
@DataJpaTest
@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {
    private RegistrationService serviceTestWithMocks;
    private RegistrationService serviceTestWithOutMocks;
    private RegistrationRequest request;
    private AppUser user1;
    @Mock
    private AppUserService appUserService;
    @Mock private EmailValidator emailValidator;
    @Mock private ConfirmationTokenService confirmationTokenService;
    @Mock private EmailSender emailSender;

    @Autowired
    private AppUserService appUserService2;
    @Autowired
    private ConfirmationTokenService confirmationTokenService2;

    @BeforeEach
    void setUp(){
        serviceTestWithMocks = new RegistrationService(appUserService, emailValidator, confirmationTokenService, emailSender);
        request = new RegistrationRequest(
                "Francisco",
                "de Deseö",
                "fran@gmail.com",
                "fran123456",
                "Teacher"
        );
        user1 = new AppUser(
                request.getFirstName(),
                request.getLastName(),
                request.getEmail(),
                request.getPassword(),
                AppUserRole.USER
        );
        serviceTestWithOutMocks = new RegistrationService(appUserService2, emailValidator, confirmationTokenService2, emailSender);
    }
    @Test
    void testRegistrationIsDoneCorrectly() {

        String message = serviceTestWithMocks.register(request);

        ArgumentCaptor<AppUser> appUserArgumentCaptor = ArgumentCaptor.forClass(AppUser.class);

        verify(appUserService).signUpUser(appUserArgumentCaptor.capture());

        AppUser capturedAppUser = appUserArgumentCaptor.getValue();

        assertEquals(capturedAppUser, user1);
        assertEquals("Successful Registration", message);

    }
    @Test
    void confirmToken() {
        serviceTestWithOutMocks.register(request);
    }
}