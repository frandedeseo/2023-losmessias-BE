package com.losmessias.leherer.service;

import com.losmessias.leherer.domain.ProfessorSubject;
import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Subject create(Subject subject) {
        Optional<Subject> existingSubject = subjectRepository.findByName(subject.getName());
        if (existingSubject.isPresent()) {
            throw new IllegalArgumentException("Subject with this name already exists");
        }
        return subjectRepository.save(subject);
    }

    public Subject editPrice(Long id, Double price) {
        Subject subject = subjectRepository.findById(id).orElse(null);
        if (subject == null) {
            throw new RuntimeException("subject with id " + id + " not found");
        }
        subject.setPrice(price);
        return subjectRepository.save(subject);
    }

    public Subject getSubjectById(Long id) {
        return subjectRepository.findById(id).orElse(null);
    }
}