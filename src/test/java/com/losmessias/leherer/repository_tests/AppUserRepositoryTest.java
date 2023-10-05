package com.losmessias.leherer.repository_tests;

import com.losmessias.leherer.domain.AppUser;
import com.losmessias.leherer.repository.AppUserRepository;
import com.losmessias.leherer.role.AppUserRole;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
public class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository appUserRepository;

    private AppUser appUser;
    @BeforeEach
    public void setupData() {
        appUser = new AppUser(
                "fran@gmail.com",
                "fran123",
                AppUserRole.USER,
                1L
        );
    }
    @Test
    @Transactional
    @Rollback
    @DisplayName("Find by email returns the AppUser")
    void testThatFindByEmailReturnsTheAppUser() {

        appUserRepository.save(appUser);
        AppUser appUser1 = appUserRepository.findByEmail("fran@gmail.com");
        assertEquals(appUser, appUser1);
    }
}