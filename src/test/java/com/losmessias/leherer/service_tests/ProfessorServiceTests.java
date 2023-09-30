package com.losmessias.leherer.service_tests;

import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.repository.ProfessorRepository;
import com.losmessias.leherer.service.ProfessorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfessorServiceTests {

    @Mock
    private ProfessorRepository professorRepository;

    @InjectMocks
    private ProfessorService professorService;

    @Test
    @DisplayName("Get all professors")
    void testGetAllProfessors() {
        List<Professor> professors = new ArrayList<Professor>();
        professors.add(new Professor("John", "Doe", "mail", "ubication", "phone"));
        professors.add(new Professor("Jane", "Doe", "mail", "ubication", "phone"));
        when(professorRepository.findAll()).thenReturn(professors);

        assertEquals(professors, professorService.getAllProfessors());
    }

    @Test
    @DisplayName("Get all professors empty")
    void testGetAllProfessorsEmpty() {
        List<Professor> professors = new ArrayList<Professor>();
        when(professorRepository.findAll()).thenReturn(professors);
        assertEquals(professors, professorService.getAllProfessors());
    }

    @Test
    @DisplayName("Add subject to professor")
    void testGetProfessorSubjects() {
        Professor professor = new Professor("John", "Doe", "mail", "ubication", "phone");
        Subject subject = new Subject( "Math");
        professor.addSubject(subject);

        when(professorRepository.save(professor)).thenReturn(professor);
        assertEquals(professorService.addSubjectTo(professor, subject), professor);
    }

    @Test
    @DisplayName("Find professor by id")
    void testFindProfessorById() {
        Professor professor = new Professor("John", "Doe", "mail", "ubication", "phone");
        when(professorRepository.findById(1L)).thenReturn(java.util.Optional.of(professor));
        assertEquals(professorService.getProfessorById(1L), professor);
    }
}
