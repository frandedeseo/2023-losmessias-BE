package com.losmessias.leherer.service;

import com.losmessias.leherer.domain.*;
import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.domain.enumeration.FeedbackOptions;
import com.losmessias.leherer.dto.FeedbackDto;
import com.losmessias.leherer.repository.AppUserRepository;
import com.losmessias.leherer.repository.FeedbackRepository;
import com.losmessias.leherer.repository.ProfessorRepository;
import com.losmessias.leherer.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final ProfessorRepository professorRepository;
    private final StudentRepository studentRepository;
    private final AppUserRepository appUserRepository;
    private final AppUserService appUserService;
    private final FeedbackReceivedService feedbackReceivedService;

    @Transactional
    public Feedback giveFeedback(FeedbackDto feedbackDto) throws InstantiationException {
        AppUser sender = appUserService.getAppUserById(feedbackDto.getSenderId());
        AppUser receiver = appUserService.getAppUserById(feedbackDto.getReceiverId());

        sender.giveFeedbackFor(feedbackDto.getClassId());

        Set<FeedbackOptions> feedbackOptions = new HashSet<>();
        if (feedbackDto.getMaterial()) feedbackOptions.add(FeedbackOptions.MATERIAL);
        if (feedbackDto.getPolite()) feedbackOptions.add(FeedbackOptions.POLITE);
        if (feedbackDto.getPunctuality()) feedbackOptions.add(FeedbackOptions.PUNCTUALITY);

        Feedback feedback = new Feedback(sender, receiver, feedbackOptions, feedbackDto.getRating());
        feedbackRepository.save(feedback);

        Double avg = getAvgRating(receiver);
        feedbackReceivedService.updateFeedbackReceived(feedbackDto, avg);

        //appUserRepository.save(sender);

        return feedback;
    }

    public Double getAvgRating(AppUser appUser){
        return feedbackRepository.getAvgRating(appUser.getId());
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
}
