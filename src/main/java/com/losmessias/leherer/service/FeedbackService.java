package com.losmessias.leherer.service;

import com.losmessias.leherer.domain.Feedback;
import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.domain.enumeration.FeedbackOptions;
import com.losmessias.leherer.dto.FeedbackDto;
import com.losmessias.leherer.repository.FeedbackRepository;
import com.losmessias.leherer.repository.ProfessorRepository;
import com.losmessias.leherer.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final StudentService studentService;
    private final ProfessorService professorService;
    private final FeedbackRepository feedbackRepository;
    private final ProfessorRepository professorRepository;
    private final StudentRepository studentRepository;

    public Feedback giveFeedback(FeedbackDto feedbackDto) {
        Student student = studentService.getStudentById(feedbackDto.getStudentId());
        Professor professor = professorService.getProfessorById(feedbackDto.getProfessorId());
        Boolean material = feedbackDto.getMaterial() != null ? feedbackDto.getMaterial() : false;
        Boolean punctuality = feedbackDto.getPunctuality() != null ? feedbackDto.getPunctuality() : false;
        Boolean educated = feedbackDto.getEducated() != null ? feedbackDto.getEducated() : false;

        if (feedbackDto.getRoleReceptor() == AppUserRole.STUDENT) {
            professor.giveFeedbackFor(feedbackDto.getClassId());
            student.receiveFeedback(feedbackDto.getRating(), material, punctuality, educated);
        } else {
            student.giveFeedbackFor(feedbackDto.getClassId());
            professor.receiveFeedback(feedbackDto.getRating(), material, punctuality, educated);
        }
        professorRepository.save(professor);
        studentRepository.save(student);

        Set<FeedbackOptions> feedbackOptions = new HashSet<>();
        if (material) feedbackOptions.add(FeedbackOptions.MATERIAL);
        if (educated) feedbackOptions.add(FeedbackOptions.POLITE);
        if (punctuality) feedbackOptions.add(FeedbackOptions.PUNCTUALITY);

        Feedback feedback = new Feedback(student, professor, feedbackDto.getRoleReceptor(), feedbackOptions, feedbackDto.getRating());
        feedbackRepository.save(feedback);
        return feedback;
    }

    public void requestFeedbackFromConcludedClass(Student student, Professor professor, Long classId) {
        student.addPendingClassFeedback(classId);
        professor.addPendingClassFeedback(classId);
        studentRepository.save(student);
        professorRepository.save(professor);
    }

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }
    //TODO: get feedback by role
}
