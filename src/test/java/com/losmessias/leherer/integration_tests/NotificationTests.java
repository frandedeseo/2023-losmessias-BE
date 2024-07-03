//package com.losmessias.leherer.integration_tests;
//
//
//import com.losmessias.leherer.LehererApplication;
//import com.losmessias.leherer.domain.*;
//import com.losmessias.leherer.domain.enumeration.AppUserRole;
//import com.losmessias.leherer.dto.RegistrationProfessorRequest;
//import com.losmessias.leherer.dto.RegistrationRequest;
//import com.losmessias.leherer.repository.*;
//import com.losmessias.leherer.service.ClassReservationService;
//import com.losmessias.leherer.service.RegistrationService;
//import com.losmessias.leherer.service.StudentService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.boot.test.web.server.LocalServerPort;
//import org.springframework.test.context.jdbc.Sql;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@RunWith(SpringRunner.class)
//@DataJpaTest
//public class NotificationTests {
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Autowired
//    private MockMvc mvc;
//
//    @Autowired
//    private NotificationProfessorRepository notificationProfessorRepository;
//    @Autowired
//    private NotificationStudentRepository notificationStudentRepository;
//    @Autowired
//    private RegistrationService registrationService;
//    @Autowired
//    private ClassReservationService classReservationService;
//    @Autowired
//    private StudentService studentService;
//    @Autowired
//    private AppUserRepository appUserRepository;
//    @Autowired
//    private StudentRepository studentRepository;
//    @Autowired
//    private ProfessorRepository professorRepository;
//
//    private RegistrationProfessorRequest request1;
//    private RegistrationRequest request2;
//
//    @BeforeEach
//    void setUp(){
//        List<Subject> subjects = new ArrayList<Subject>();
//        subjects.add(new Subject("Math"));
//        subjects.add(new Subject("History"));
//        request1 = new RegistrationProfessorRequest(
//                "Francisco",
//                "de Deseö",
//                "fran@gmail.com",
//                "fran123456",
//                "Professor",
//                "Recoleta",
//                "543462663707",
//                "Male",
//                subjects
//
//        );
//        request2 = new RegistrationRequest(
//                "Francisco",
//                "de Deseö",
//                "fran@gmail.com",
//                "fran123456",
//                "Student",
//                "Recoleta",
//                "543462663707",
//                "Male"
//        );
//        registrationService.register(request2);
//        registrationService.registerProfessor(request1);
//        professor1 = new Professor("Francisco",
//                "de Deseö",
//                "fran@gmail.com",);
//        student1 = new Student("frandedeseo@gmail.com","fran1234","John", "Doe",  "location", "123", AppUserSex.MALE);
//
//
//
//
//        classReservationService.createReservation(
//
//        );
//
//
//        user1 = new AppUser(
//                request1.getEmail(),
//                request1.getPassword(),
//                AppUserRole.PROFESSOR,
//                2L
//        );
//    }
//    @Test
//    public void testGetStudentNotifications() {
//        // given
//        NotificationStudent notificationStudent = new NotificationStudent(, );
//        entityManager.persist(alex);
//        entityManager.flush();
//
//        // when
//        Employee found = employeeRepository.findByName(alex.getName());
//
//        // then
//        assertThat(found.getName())
//                .isEqualTo(alex.getName());
//    }
//}
