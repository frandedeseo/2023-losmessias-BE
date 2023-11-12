package com.losmessias.leherer.service;

import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.domain.Feedback;
import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.domain.enumeration.FeedbackOptions;
import com.losmessias.leherer.dto.FeedbackDto;
import com.losmessias.leherer.repository.FeedbackRepository;
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

    public Feedback giveFeedback(FeedbackDto feedbackDto) {
        Student student = studentService.getStudentById(feedbackDto.getStudentId());
        Professor professor = professorService.getProfessorById(feedbackDto.getProfessorId());

        if (feedbackDto.getRoleReceptor() == AppUserRole.STUDENT) {
            professor.giveFeedbackFor(feedbackDto.getClassId());
            student.receiveFeedback(feedbackDto.getRating(),
                    feedbackDto.getMaterial(),
                    feedbackDto.getPunctuality(),
                    feedbackDto.getEducated());
        } else {
            student.giveFeedbackFor(feedbackDto.getClassId());
            professor.receiveFeedback(feedbackDto.getRating(),
                    feedbackDto.getMaterial(),
                    feedbackDto.getPunctuality(),
                    feedbackDto.getEducated());
        }

        Set<FeedbackOptions> feedbackOptions = new HashSet<>();
        if (feedbackDto.getEducated()) feedbackOptions.add(FeedbackOptions.POLITE);
        if (feedbackDto.getPunctuality()) feedbackOptions.add(FeedbackOptions.PUNCTUALITY);
        if (feedbackDto.getMaterial()) feedbackOptions.add(FeedbackOptions.MATERIAL);

        Feedback feedback = new Feedback(student, professor, feedbackDto.getRoleReceptor(), feedbackOptions, feedbackDto.getRating());
        feedbackRepository.save(feedback);
        return feedback;
    }

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }
    //TODO: get feedback by role
}
