package com.losmessias.leherer.service;

import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.FeedbackReceived;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.repository.FeedbackReceivedRepository;
import com.losmessias.leherer.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final FeedbackReceivedRepository feedbackReceivedRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student create(Student student) {
        feedbackReceivedRepository.save(student.getFeedbackReceived());
        return studentRepository.save(student);
    }
}
