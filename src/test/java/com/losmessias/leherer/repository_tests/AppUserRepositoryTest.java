package com.losmessias.leherer.repository_tests;

import com.losmessias.leherer.domain.AppUser;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.repository.AppUserRepository;
import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.domain.enumeration.AppUserSex;
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

    private Student student;
    @BeforeEach
    public void setupData() {
        student = new Student(
                "fran@gmail.com",
                "fran12345",
                "Francisco",
                "de Deseo",
                "Ayacucho 1822",
                "+54 3462 663707",
                AppUserSex.MALE
        );
    }
    @Test
    @Transactional
    @Rollback
    @DisplayName("Find by email returns the AppUser")
    void testThatFindByEmailReturnsTheAppUser() {

        appUserRepository.save(student);
        AppUser appUser1 = appUserRepository.findByEmail("fran@gmail.com");
        assertEquals(student, appUser1);
    }
}