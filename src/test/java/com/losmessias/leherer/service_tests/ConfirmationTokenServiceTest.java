package com.losmessias.leherer.service_tests;

import com.losmessias.leherer.domain.AppUser;
import com.losmessias.leherer.domain.ConfirmationToken;
import com.losmessias.leherer.repository.ConfirmationTokenRepository;
import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.service.ConfirmationTokenService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConfirmationTokenServiceTest {
    @Mock
    private ConfirmationTokenRepository confirmationTokenRepository;
    @InjectMocks
    private ConfirmationTokenService confirmationTokenService;
    private String token;
    private AppUser appUser;
    private ConfirmationToken confirmationToken, confirmationToken2;
    @BeforeEach
    void setUp(){

        appUser = new AppUser(
                "fran@gmail.com",
                "fran123456",
                AppUserRole.USER,
                23L
        );

        token = "token";
        confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );
        confirmationToken2 = new ConfirmationToken(
                token,
                LocalDateTime.now().minusMinutes(60),
                LocalDateTime.now().minusMinutes(45),
                appUser
        );
    }
    @Test
    @DisplayName("Confirmation of Token was successful")
    void testValidateTokenReturnsTokenValidated() {

        when(confirmationTokenRepository.findByToken(token)).thenReturn(confirmationToken);
        ConfirmationToken confirmationToken_result  = confirmationTokenService.validateToken(token);

        assertEquals(confirmationToken, confirmationToken_result);

    }
    @Test
    @DisplayName("Token is not found")
    void testGetTokenThrowsErrorBecauseTokenIsNotFound() {

        Assertions.assertThrows(IllegalStateException.class, () -> {
            confirmationTokenService.validateToken(token);
        }, "token not found");
    }

    @Test
    @DisplayName("Token already confirmed throws error")
    void testValidateAlreadyConfirmedTokenThrowsError() {

        when(confirmationTokenRepository.findByToken(token)).thenReturn(confirmationToken2);

        Assertions.assertThrows(IllegalStateException.class, () -> {
            confirmationTokenService.validateToken(token);
        }, "email already confirmed");
    }
    @Test
    @DisplayName("Token that confirmed time expired throws error")
    void testValidateExpiredTokenThrowsError() {

        when(confirmationTokenRepository.findByToken(token)).thenReturn(confirmationToken2);

        Assertions.assertThrows(IllegalStateException.class, () -> {
            confirmationTokenService.validateToken(token);
        }, "token expired");
    }
}