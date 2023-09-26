//package com.losmessias.leherer.controller;
//
//import com.losmessias.leherer.domain.Subject;
//import com.losmessias.leherer.service.SubjectService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/subject")
//@RequiredArgsConstructor
//public class SubjectController {
//
//    private final SubjectService subjectService;
//
//    @GetMapping
//    public List<Subject> getSubject() {
//        return subjectService.getAllSubjects();
//    }
//}