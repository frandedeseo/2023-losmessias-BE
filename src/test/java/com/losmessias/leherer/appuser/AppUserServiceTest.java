package com.losmessias.leherer.appuser;

import com.losmessias.leherer.domain.AppUser;
import com.losmessias.leherer.service.ConfirmationTokenService;
import com.losmessias.leherer.repository.AppUserRepository;
import com.losmessias.leherer.role.AppUserRole;
import com.losmessias.leherer.service.AppUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.mockito.Mock;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    private AppUserService serviceTestWithMocks;
    @Mock private AppUserRepository mockAppUserRepository;
    @Mock private BCryptPasswordEncoder mockBCryptPasswordEncoder;
    @Mock private ConfirmationTokenService mockConfirmationTokenService;

    @Autowired
    public AppUserService serviceTestWithOutMocks;
    private AppUserRepository appUserRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private ConfirmationTokenService confirmationTokenService;
    private AppUser appUser1;
    private AppUser appUser2;

    @BeforeEach
    void setUp(){
        serviceTestWithMocks = new AppUserService(mockAppUserRepository, mockBCryptPasswordEncoder, mockConfirmationTokenService);
        appUser1 = new AppUser(
                "Francisco",
                "de Deseö",
                "fran@gmail.com",
                "fran123456",
                AppUserRole.USER,
                23L
        );
    }

    @Test
    void testUserSignsUpCorrectly() {
        serviceTestWithMocks.signUpUser(appUser1);

        ArgumentCaptor<AppUser> appUserArgumentCaptor = ArgumentCaptor.forClass(AppUser.class);
        ArgumentCaptor<String> passwordArgumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(mockBCryptPasswordEncoder).encode(passwordArgumentCaptor.capture());
        verify(mockAppUserRepository).save(appUserArgumentCaptor.capture());

        AppUser capturedAppUser = appUserArgumentCaptor.getValue();
        String capturedPassword = passwordArgumentCaptor.getValue();

        assertEquals(capturedPassword, "fran123456");
        assertEquals(capturedAppUser, appUser1);
    }
    /*
    @Test
    void testUserSignUpWithTheSameEmailThrowsError(){
        userExists
        given((mockAppUserRepository.findByEmail(appUser1.getEmail()))).willReturn(true);

        assertThatThrownBy(() -> serviceTestWithMocks.signUpUser(appUser1))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("email already taken");
    }
    @Test
    void testUserSignUpWithTheSameEmailThrowsErrors(){

        serviceTestWithOutMocks.signUpUser(appUser1);

        assertThatThrownBy(() -> serviceTestWithOutMocks.signUpUser(appUser1))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("email already taken");
    }
    */

}