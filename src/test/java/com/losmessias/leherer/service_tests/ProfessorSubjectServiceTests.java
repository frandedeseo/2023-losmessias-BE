package com.losmessias.leherer.service_tests;

import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.ProfessorSubject;
import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.domain.enumeration.SubjectStatus;
import com.losmessias.leherer.repository.ProfessorRepository;
import com.losmessias.leherer.repository.ProfessorSubjectRepository;
import com.losmessias.leherer.repository.SubjectRepository;
import com.losmessias.leherer.service.ProfessorService;
import com.losmessias.leherer.service.ProfessorSubjectService;
import com.losmessias.leherer.service.SubjectService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfessorSubjectServiceTests {
    @Mock
    private ProfessorSubjectRepository professorSubjectRepository;
    @InjectMocks
    private ProfessorSubjectService professorSubjectService;

    @Test
    @DisplayName("Get all professor subjects")
    void testGetAllProfessorSubjects() {
        List<ProfessorSubject> professorSubjects = new ArrayList<>();
        professorSubjects.add(new ProfessorSubject());
        professorSubjects.add(new ProfessorSubject());
        when(professorSubjectRepository.findAll()).thenReturn(professorSubjects);
        assertEquals(professorSubjects, professorSubjectService.getAllProfessorSubjects());
    }

    @Test
    @DisplayName("Find by id")
    void testFindById() {
        ProfessorSubject professorSubject = new ProfessorSubject();
        when(professorSubjectRepository.findById(any())).thenReturn(Optional.of(professorSubject));
        assertEquals(professorSubject, professorSubjectService.findById(1L));
    }

    @Test
    @DisplayName("Create association between professor and subject")
    void testCreatingAnAssociationBetweenProfessorAndSubject() {
        Professor professor = new Professor("John", "Doe", "mail", "ubication", "phone");
        Subject subject = new Subject("Math");
        ProfessorSubject professorSubject = new ProfessorSubject(professor, subject);

        when(professorSubjectRepository.save(any())).thenReturn(professorSubject);
        assertEquals(professorSubject, professorSubjectService.createAssociation(professor,subject));
    }

    @Test
    @DisplayName("Change status of professor subject from pending to approved")
    void testChangingStatusOfProfessorSubjectFromPendingToApproved() {
        Professor professor = new Professor("John", "Doe", "mail", "ubication", "phone");
        Subject subject = new Subject("Math");
        ProfessorSubject professorSubject = new ProfessorSubject(professor, subject);

        when(professorSubjectRepository.save(any())).thenReturn(professorSubject);
        ProfessorSubject subjectCreated = professorSubjectService.createAssociation(professor,subject);
        assertEquals(professorSubject, subjectCreated);

        when(professorSubjectRepository.findById(any())).thenReturn(Optional.ofNullable(subjectCreated));
        subjectCreated = professorSubjectService.changeStatusOf(subjectCreated.getId(), SubjectStatus.APPROVED);
        assertEquals(professorSubject, subjectCreated);
    }

    @Test
    @DisplayName("Change status of professor subject from pending to rejected")
    void testChangingStatusOfProfessorSubjectFromPendingToRejected() {
        Professor professor = new Professor("John", "Doe", "mail", "ubication", "phone");
        Subject subject = new Subject("Math");
        ProfessorSubject professorSubject = new ProfessorSubject(professor, subject);

        when(professorSubjectRepository.save(any())).thenReturn(professorSubject);
        ProfessorSubject subjectCreated = professorSubjectService.createAssociation(professor,subject);
        assertEquals(professorSubject, subjectCreated);

        when(professorSubjectRepository.findById(any())).thenReturn(Optional.ofNullable(subjectCreated));
        subjectCreated = professorSubjectService.changeStatusOf(subjectCreated.getId(), SubjectStatus.REJECTED);
        assertEquals(professorSubject, subjectCreated);
    }

    @Test
    @DisplayName("Find by professor")
    void testFindSubjectByProfessor(){
        Professor professor1 = new Professor("John", "Doe", "mail", "ubication", "phone");
        Subject subject1 = new Subject("Math");
        ProfessorSubject professorSubject1 = new ProfessorSubject(professor1, subject1);

        Subject subject2 = new Subject("Math");
        ProfessorSubject professorSubject2 = new ProfessorSubject(professor1, subject2);
        List<ProfessorSubject> professorSubjects = new ArrayList<>();
        professorSubjects.add(professorSubject1);
        professorSubjects.add(professorSubject2);

        when(professorSubjectRepository.findByProfessorId(any())).thenReturn(professorSubjects);
        assertEquals(professorSubjects, professorSubjectService.findByProfessor(professor1));
    }

    @Test
    @DisplayName("Find by professor and subject")
    void testFindByProfessorAndSubject(){
        Professor professor1 = new Professor("John", "Doe", "mail", "ubication", "phone");
        Subject subject1 = new Subject("Math");
        ProfessorSubject professorSubject = new ProfessorSubject(professor1, subject1);

        when(professorSubjectRepository.findByProfessorIdAndSubject_Id(any(), any())).thenReturn(professorSubject);
        assertEquals(professorSubject, professorSubjectService.findByProfessorAndSubject(professor1, subject1));
    }

    @Test
    @DisplayName("Find by pending status")
    void testFindByPendingStatus(){
        Professor professor1 = new Professor("John", "Doe", "mail", "ubication", "phone");
        Subject subject1 = new Subject("Math");
        ProfessorSubject professorSubject1 = new ProfessorSubject(professor1, subject1);

        Subject subject2 = new Subject("Math");
        ProfessorSubject professorSubject2 = new ProfessorSubject(professor1, subject2);
        List<ProfessorSubject> professorSubjects = new ArrayList<>();
        professorSubjects.add(professorSubject1);
        professorSubjects.add(professorSubject2);

        when(professorSubjectRepository.findByStatus(any())).thenReturn(professorSubjects);
        assertEquals(professorSubjects, professorSubjectService.findByStatus(SubjectStatus.PENDING));
    }

    @Test
    @DisplayName("Find by professor id and status")
    void testFindByProfessorAndPendingStatus(){
        Professor professor1 = new Professor("John", "Doe", "mail", "ubication", "phone");
        Subject subject1 = new Subject("Math");
        ProfessorSubject professorSubject1 = new ProfessorSubject(professor1, subject1);

        Subject subject2 = new Subject("Math");
        ProfessorSubject professorSubject2 = new ProfessorSubject(professor1, subject2);
        List<ProfessorSubject> professorSubjects = new ArrayList<>();
        professorSubjects.add(professorSubject1);
        professorSubjects.add(professorSubject2);

        when(professorSubjectRepository.findByProfessorIdAndStatus(any(), any())).thenReturn(professorSubjects);
        assertEquals(professorSubjects, professorSubjectService.findByProfessorIdAndStatus(professor1.getId(), SubjectStatus.PENDING));
    }

}
