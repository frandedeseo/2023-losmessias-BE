package com.losmessias.leherer.appuser;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository repositoryTest;
    @Test
    void testThatTheAppUserIsEnabled() {
        AppUser appUser = new AppUser(
                "Francisco",
                "de Deseö",
                "fran@gmail.com",
                "fran123",
                AppUserRole.USER
        );
        repositoryTest.save(appUser);

        repositoryTest.enableAppUser("fran@gmail.com");

        List<AppUser> users = repositoryTest.findAll();

        assertTrue(true);

    }
}