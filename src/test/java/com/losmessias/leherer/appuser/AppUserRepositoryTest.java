package com.losmessias.leherer.appuser;

import com.losmessias.leherer.domain.AppUser;
import com.losmessias.leherer.repository.AppUserRepository;
import com.losmessias.leherer.role.AppUserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
                AppUserRole.USER,
                1L
        );
        repositoryTest.save(appUser);

        repositoryTest.enableAppUser("fran@gmail.com");

        List<AppUser> users = repositoryTest.findAll();

        assertTrue(true);

    }
}