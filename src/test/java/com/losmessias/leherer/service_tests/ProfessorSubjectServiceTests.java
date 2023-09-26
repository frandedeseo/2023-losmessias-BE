package com.losmessias.leherer.service_tests;

import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.ProfessorSubject;
import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.repository.ProfessorRepository;
import com.losmessias.leherer.repository.ProfessorSubjectRepository;
import com.losmessias.leherer.repository.SubjectRepository;
import com.losmessias.leherer.service.ProfessorService;
import com.losmessias.leherer.service.ProfessorSubjectService;
import com.losmessias.leherer.service.SubjectService;
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
    @Mock
    private ProfessorRepository professorRepository;
    @Mock
    private SubjectRepository subjectRepository;
    @InjectMocks
    private ProfessorSubjectService professorSubjectService;
    @InjectMocks
    private ProfessorService professorService;
    @InjectMocks
    private SubjectService subjectService;

    @Test
    void testGetAllProfessorSubjects() {
        List<ProfessorSubject> professorSubjects = new ArrayList<>();
        professorSubjects.add(new ProfessorSubject());
        professorSubjects.add(new ProfessorSubject());
        when(professorSubjectRepository.findAll()).thenReturn(professorSubjects);
        assertEquals(professorSubjects, professorSubjectService.getAllProfessorSubjects());
    }

    @Test
    void testGetAllProfessorSubjectsReturnsEmptyList() {
        List<ProfessorSubject> professorSubjects = new ArrayList<>();
        when(professorSubjectRepository.findAll()).thenReturn(professorSubjects);
        assertEquals(professorSubjects, professorSubjectService.getAllProfessorSubjects());
    }

    @Test
    void testCreatingAnAssociationBetweenProfessorAndSubject() {
        Professor professor = new Professor("John", "Doe");
        Subject subject = new Subject("Math");
        ProfessorSubject professorSubject = new ProfessorSubject(professor, subject);

        when(professorSubjectRepository.save(any())).thenReturn(professorSubject);
        assertEquals(professorSubject, professorSubjectService.createAssociation(professor,subject));
    }

}
