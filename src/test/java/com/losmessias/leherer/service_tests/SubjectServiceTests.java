package com.losmessias.leherer.service_tests;

import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.repository.SubjectRepository;
import com.losmessias.leherer.service.SubjectService;
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
public class SubjectServiceTests {
    @InjectMocks
    private SubjectService subjectService;

    @Mock
    private SubjectRepository subjectRepository;

    @Test
    @DisplayName("Get all subjects")
    void testGetAllSubjects() {
        List<Subject> subjects = new ArrayList<>();
        subjects.add(new Subject("Math"));
        subjects.add(new Subject("Physics"));
        subjects.add(new Subject("Chemistry"));
        when(subjectRepository.findAll()).thenReturn(subjects);
        assert(subjectService.getAllSubjects().size() == 3);
        assertEquals(subjectService.getAllSubjects(), subjects);
    }

    @Test
    @DisplayName("Get all subjects empty")
    void testGetAllSubjectsReturnsEmptyList() {
        List<Subject> subjects = new ArrayList<>();
        when(subjectRepository.findAll()).thenReturn(subjects);
        assert(subjectService.getAllSubjects().isEmpty());
        assertEquals(subjectService.getAllSubjects(), subjects);
    }

    @Test
    @DisplayName("Create a valid subject")
    void testCreatingAValidSubject() {
        Subject subject = new Subject("Math");
        when(subjectRepository.save(subject)).thenReturn(subject);
        assertEquals(subjectService.create(subject), subject);
    }

    @Test
    @DisplayName("Find subject by id")
    void testFindSubjectById() {
        Subject subject = new Subject("Math");
        when(subjectRepository.findById(1L)).thenReturn(java.util.Optional.of(subject));
        assertEquals(subjectService.getSubjectById(1L), subject);
    }

}