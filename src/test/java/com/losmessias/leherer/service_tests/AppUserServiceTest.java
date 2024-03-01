package com.losmessias.leherer.service_tests;

import com.losmessias.leherer.domain.AppUser;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.repository.AppUserRepository;
import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.service.AppUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AppUserServiceTest {

    @Mock
    private AppUserRepository appUserRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    public AppUserService appUserService;

    private AppUser student;

    @BeforeEach
    void setUp(){
        student = new Student(
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
    @DisplayName("Log In Successful")
    void testLoadUserByUsernameReturnTheUser() {
        when(appUserRepository.findByEmail("fran@gmail.com")).thenReturn(student);
        UserDetails appUserResult = appUserService.loadUserByUsername("fran@gmail.com");
        assertEquals(student, appUserResult);

    }
    @Test
    @DisplayName("The log in throws error because of wrong email")
    void testLoadUserByUsernameThrowsErrorInvalidUserName() {

        when(appUserRepository.findByEmail("fran@gmail.com")).thenReturn(null);

        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            appUserService.loadUserByUsername("fran@gmail.com");
        }, "Invalid username or password.");

    }

/*    @Test
    @DisplayName("Sign Up Successful")
    void testUserSignsUpCorrectly() {
        appUserService.signUpUser(appUser);

        ArgumentCaptor<AppUser> appUserArgumentCaptor = ArgumentCaptor.forClass(AppUser.class);
        ArgumentCaptor<String> passwordArgumentCaptor = ArgumentCaptor.forClass(String.class);

        verify(bCryptPasswordEncoder).encode(passwordArgumentCaptor.capture());
        verify(appUserRepository).save(appUserArgumentCaptor.capture());

        AppUser capturedAppUser = appUserArgumentCaptor.getValue();
        String capturedPassword = passwordArgumentCaptor.getValue();

        assertEquals(capturedPassword, "fran123456");
        assertEquals(capturedAppUser, appUser);
    }*/

    @Test
    @DisplayName("Email repeated throws error")
    void testValidateEmailThrowsErrorIfEmailTaken(){

        when(appUserRepository.findByEmail("fran@gmail.com")).thenReturn(student);

        Assertions.assertThrows(IllegalStateException.class, () -> {
            appUserService.validateEmailNotTaken("fran@gmail.com");
        }, "email already taken");

    }

}