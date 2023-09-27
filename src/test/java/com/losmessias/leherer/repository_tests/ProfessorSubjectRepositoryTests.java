package com.losmessias.leherer.repository_tests;

import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.ProfessorSubject;
import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.domain.enumeration.SubjectStatus;
import com.losmessias.leherer.repository.ProfessorRepository;
import com.losmessias.leherer.repository.ProfessorSubjectRepository;
import com.losmessias.leherer.repository.SubjectRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
public class ProfessorSubjectRepositoryTests {

    @Autowired
    private ProfessorSubjectRepository professorSubjectRepository;
    @Autowired
    private ProfessorRepository professorRepository;
    @Autowired
    private SubjectRepository subjectRepository;

    private ProfessorSubject professorSubject;
    private Professor professor;
    private Subject subject;

    @BeforeEach
    public void setupData() {
        professor = Professor.builder()
                .firstName("John")
                .lastName("Doe")
                .build();
        subject = Subject.builder()
                .name("Math")
                .build();

        professorRepository.save(professor);
        subjectRepository.save(subject);
        professorSubject = ProfessorSubject.builder()
                .professor(professor)
                .subject(subject)
                .status(SubjectStatus.PENDING)
                .build();
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Find by professorId method")
    void findByProfessorId() {
        professorRepository.save(professor);
        subjectRepository.save(subject);
        professorSubjectRepository.save(professorSubject);
        List<ProfessorSubject> subjectsFound = professorSubjectRepository.findByProfessorId(professor.getId());
        ProfessorSubject professorSubjectFound = subjectsFound.get(0);

        assertEquals(professorSubjectFound.getId(), professorSubject.getId());
        assertEquals(professorSubjectFound.getProfessor().getId(), professorSubject.getProfessor().getId());
        assertEquals(professorSubjectFound.getSubject().getId(), professorSubject.getSubject().getId());
        assertEquals(professorSubjectFound.getProfessor().getLastName(), professorSubject.getProfessor().getLastName());
        assertEquals(professorSubjectFound.getSubject().getId(), professorSubject.getSubject().getId());
        assertEquals(professorSubjectFound.getSubject().getName(), professorSubject.getSubject().getName());
        assertEquals(professorSubjectFound.getStatus(), professorSubject.getStatus());
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Find by professorId and Subject_Id method")
    void findByProfessorIdAndSubject_Id() {
        professorRepository.save(professor);
        subjectRepository.save(subject);
        professorSubjectRepository.save(professorSubject);
        ProfessorSubject professorSubjectFound = professorSubjectRepository.findByProfessorIdAndSubject_Id(professor.getId(), subject.getId());

        assertEquals(professorSubjectFound.getId(), professorSubject.getId());
        assertEquals(professorSubjectFound.getProfessor().getId(), professorSubject.getProfessor().getId());
        assertEquals(professorSubjectFound.getSubject().getId(), professorSubject.getSubject().getId());
        assertEquals(professorSubjectFound.getProfessor().getLastName(), professorSubject.getProfessor().getLastName());
        assertEquals(professorSubjectFound.getSubject().getId(), professorSubject.getSubject().getId());
        assertEquals(professorSubjectFound.getSubject().getName(), professorSubject.getSubject().getName());
        assertEquals(professorSubjectFound.getStatus(), professorSubject.getStatus());
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Find by status method")
    void findByStatus() {
        professorRepository.save(professor);
        subjectRepository.save(subject);
        professorSubjectRepository.save(professorSubject);
        List<ProfessorSubject> subjectsFound = professorSubjectRepository.findByStatus(professorSubject.getStatus());
        ProfessorSubject professorSubjectFound = subjectsFound.get(0);

        assertEquals(professorSubjectFound.getId(), professorSubject.getId());
        assertEquals(professorSubjectFound.getProfessor().getId(), professorSubject.getProfessor().getId());
        assertEquals(professorSubjectFound.getSubject().getId(), professorSubject.getSubject().getId());
        assertEquals(professorSubjectFound.getProfessor().getLastName(), professorSubject.getProfessor().getLastName());
        assertEquals(professorSubjectFound.getSubject().getId(), professorSubject.getSubject().getId());
        assertEquals(professorSubjectFound.getSubject().getName(), professorSubject.getSubject().getName());
        assertEquals(professorSubjectFound.getStatus(), professorSubject.getStatus());
    }

    @Test
    @Transactional
    @Rollback
    @DisplayName("Find by professorId and status method")
    void findByProfessorIdAndStatus() {
        professorRepository.save(professor);
        subjectRepository.save(subject);
        professorSubjectRepository.save(professorSubject);
        List<ProfessorSubject> subjectsFound = professorSubjectRepository.findByProfessorIdAndStatus(professor.getId(), professorSubject.getStatus());
        ProfessorSubject professorSubjectFound = subjectsFound.get(0);

        assertEquals(professorSubjectFound.getId(), professorSubject.getId());
        assertEquals(professorSubjectFound.getProfessor().getId(), professorSubject.getProfessor().getId());
        assertEquals(professorSubjectFound.getSubject().getId(), professorSubject.getSubject().getId());
        assertEquals(professorSubjectFound.getProfessor().getLastName(), professorSubject.getProfessor().getLastName());
        assertEquals(professorSubjectFound.getSubject().getId(), professorSubject.getSubject().getId());
        assertEquals(professorSubjectFound.getSubject().getName(), professorSubject.getSubject().getName());
        assertEquals(professorSubjectFound.getStatus(), professorSubject.getStatus());
    }

}
