package com.losmessias.leherer.controller;

import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subject")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping
    public List<Subject> getSubject() {
        return subjectService.getAllSubjects();
    }

    @PostMapping("/create")
    public Subject createSubject(@RequestBody Subject subject) {
        System.out.println(subject);
        if(subject.getId() != null) {
            throw new RuntimeException("Creation of subject cannot have an id");
        }
        return subjectService.create(subject);
    }
}