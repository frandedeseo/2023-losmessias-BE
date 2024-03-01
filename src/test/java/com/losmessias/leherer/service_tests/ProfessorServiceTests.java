package com.losmessias.leherer.service_tests;

import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.dto.AppUserUpdateDto;
import com.losmessias.leherer.repository.ProfessorRepository;
import com.losmessias.leherer.role.AppUserSex;
import com.losmessias.leherer.service.AppUserService;
import com.losmessias.leherer.service.ProfessorService;
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
public class ProfessorServiceTests {

    @Mock
    private ProfessorRepository professorRepository;

    @InjectMocks
    private ProfessorService professorService;
    @InjectMocks
    private AppUserService appUserService;

    @Test
    @DisplayName("Get all professors")
    void testGetAllProfessors() {
        List<Professor> professors = new ArrayList<Professor>();
        professors.add(new Professor());
        professors.add(new Professor());
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
    @DisplayName("Find professor by id")
    void testFindProfessorById() {
        Professor professor = new Professor();
        when(professorRepository.findById(1L)).thenReturn(java.util.Optional.of(professor));
        assertEquals(professorService.getProfessorById(1L), professor);
    }

    @Test
    @DisplayName("Update a professor")
    void testUpdateProfessor() {
        Professor professor = new Professor();
        AppUserUpdateDto professorToUpdate = new AppUserUpdateDto();
        professorToUpdate.setEmail("jane@gmail.com");
        when(professorRepository.findById(1L)).thenReturn(Optional.of(professor));
        when(professorRepository.save(any())).thenReturn(professorToUpdate);
        assertEquals(appUserService.update(1L, professorToUpdate), professorToUpdate);
    }
}
