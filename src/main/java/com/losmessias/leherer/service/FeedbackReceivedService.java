package com.losmessias.leherer.service;


import com.losmessias.leherer.domain.AppUser;
import com.losmessias.leherer.domain.Feedback;
import com.losmessias.leherer.domain.FeedbackReceived;
import com.losmessias.leherer.dto.FeedbackDto;
import com.losmessias.leherer.repository.FeedbackReceivedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackReceivedService {

    private final AppUserService appUserService;
    private final FeedbackReceivedRepository feedbackReceivedRepository;

    public void updateFeedbackReceived( FeedbackDto feedbackDto, Double avg) {
        AppUser appUser = appUserService.getAppUserById(feedbackDto.getReceiverId());
        FeedbackReceived feedbackReceived = appUser.getFeedbackReceived();
        feedbackReceived.update(feedbackDto, avg);
        feedbackReceivedRepository.save(feedbackReceived);
    }

}
