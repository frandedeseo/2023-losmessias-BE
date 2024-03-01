package com.losmessias.leherer.repository_tests;

import com.losmessias.leherer.domain.AppUser;
import com.losmessias.leherer.domain.ConfirmationToken;
import com.losmessias.leherer.domain.Student;
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

    private Student student;
    private ConfirmationToken confirmationToken;
    @BeforeEach
    public void setupData() {
        student = new Student(
                "fran@gmail.com",
                "fran123",
                "Francisco",
                "de Deseo",
                "Ayacucho 1822",
                "+54 3462 663707",
                com.losmessias.leherer.role.AppUserSex.MALE
        );
        confirmationToken = new ConfirmationToken(
                "token",
                student
        );
    }
    @Test
    @Transactional
    @Rollback
    @DisplayName("Find the Token Object")
    void testFindByToken() {

        appUserRepository.save(student);
        confirmationTokenRepository.save(confirmationToken);

        ConfirmationToken token_result = confirmationTokenRepository.findByToken("token");

        assertEquals(confirmationToken, token_result);
    }
}