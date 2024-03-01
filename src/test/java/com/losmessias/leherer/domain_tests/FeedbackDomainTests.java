package com.losmessias.leherer.domain_tests;

import com.losmessias.leherer.domain.AppUser;
import com.losmessias.leherer.domain.Feedback;
import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.domain.enumeration.FeedbackOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class FeedbackDomainTests {
    private AppUser sender;
    private AppUser reciever;
    private Set<FeedbackOptions> feedbackOptions;

    @BeforeEach
    void setUp() {
        sender = Student.builder().build();
        reciever = Professor.builder().build();
        feedbackOptions = new HashSet<>();
    }

    @Test
    @DisplayName("Creating feedback with valid parameters")
    void creatingFeedbackWithValidParameters() throws InstantiationException {
        Feedback feedback = new Feedback(sender, reciever,  feedbackOptions, 3.0);
        assertThat(feedback).isNotNull();
        assert (feedback.getReceiver().equals(reciever));
        assert (feedback.getSender().equals(sender));
        assert (feedback.getReceptorRole().equals(AppUserRole.STUDENT));
        assert (feedback.getFeedbackOptions().equals(feedbackOptions));
        assert (feedback.getRating().equals(3.0));
    }

    @Test
    @DisplayName("Creating feedback with than 3.0 rating rounds to 3.0")
    void creatingFeedbackWithInvalidParameters() throws InstantiationException {
        Feedback feedback = new Feedback(sender, reciever, feedbackOptions, 3.5);
        assertThat(feedback).isNotNull();
        assert (feedback.getSender().equals(sender));
        assert (feedback.getReceiver().equals(reciever));
        assert (feedback.getReceptorRole().equals(AppUserRole.STUDENT));
        assert (feedback.getFeedbackOptions().equals(feedbackOptions));
        assert (feedback.getRating().equals(3.0));
    }
//TODO Tendría que ser que tire excepción
    @Test
    @DisplayName("Creating feedback with less than 0.0 rating rounds to 0.0")
    void creatingFeedbackWithInvalidParameters2() throws InstantiationException {
        Feedback feedback = new Feedback(sender, reciever, feedbackOptions, -1.0);
        assertThat(feedback).isNotNull();
        assert (feedback.getSender().equals(sender));
        assert (feedback.getReceiver().equals(reciever));
        assert (feedback.getReceptorRole().equals(AppUserRole.STUDENT));
        assert (feedback.getFeedbackOptions().equals(feedbackOptions));
        assert (feedback.getRating().equals(0.0));
    }

    @Test
    @DisplayName("Creating feedback with invalid rating rounds down to nearest 0.5")
    void creatingFeedbackWithInvalidParameters3() throws InstantiationException {
        Feedback feedback = new Feedback(sender, reciever, feedbackOptions, 2.3);
        assertThat(feedback).isNotNull();
        assert (feedback.getSender().equals(sender));
        assert (feedback.getReceiver().equals(reciever));
        assert (feedback.getReceptorRole().equals(AppUserRole.STUDENT));
        assert (feedback.getFeedbackOptions().equals(feedbackOptions));
        assert (feedback.getRating().equals(2.5));
    }

    @Test
    @DisplayName("Creating feedback with invalid rating rounds up to nearest 0.5")
    void creatingFeedbackWithInvalidParameters4() throws InstantiationException {
        Feedback feedback = new Feedback(sender, reciever, feedbackOptions, 2.7);
        assertThat(feedback).isNotNull();
        assert (feedback.getSender().equals(sender));
        assert (feedback.getReceiver().equals(reciever));
        assert (feedback.getReceptorRole().equals(AppUserRole.STUDENT));
        assert (feedback.getFeedbackOptions().equals(feedbackOptions));
        assert (feedback.getRating().equals(2.5));
    }
}
