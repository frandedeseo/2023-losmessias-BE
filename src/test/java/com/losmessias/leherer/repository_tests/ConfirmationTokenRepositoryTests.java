package com.losmessias.leherer.repository_tests;

import com.losmessias.leherer.domain.AppUser;
import com.losmessias.leherer.domain.ConfirmationToken;
import com.losmessias.leherer.repository.AppUserRepository;
import com.losmessias.leherer.repository.ConfirmationTokenRepository;
import com.losmessias.leherer.domain.enumeration.AppUserRole;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
public class ConfirmationTokenRepositoryTests {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;
    @Autowired
    private AppUserRepository appUserRepository;

    private AppUser appUser;
    private ConfirmationToken confirmationToken;
    @BeforeEach
    public void setupData() {
        appUser = new AppUser(
                "fran@gmail.com",
                "fran123",
                AppUserRole.USER,
                1L
        );
        confirmationToken = new ConfirmationToken(
                "token",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );
    }
    @Test
    @Transactional
    @Rollback
    @DisplayName("Find the Token Object")
    void testFindByToken() {

        appUserRepository.save(appUser);
        confirmationTokenRepository.save(confirmationToken);

        ConfirmationToken token_result = confirmationTokenRepository.findByToken("token");

        assertEquals(confirmationToken, token_result);
    }
}