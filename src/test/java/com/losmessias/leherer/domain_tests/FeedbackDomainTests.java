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
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class FeedbackDomainTests {
    private AppUser sender;
    private AppUser receiver;
    private Set<FeedbackOptions> feedbackOptions;

    @BeforeEach
    void setUp() {
        sender = Student.builder().build();
        receiver = Professor.builder().build();
        feedbackOptions = new HashSet<>();
    }

    @Test
    @DisplayName("Creating feedback with valid parameters")
    void creatingFeedbackWithValidParameters() throws InstantiationException {
        Feedback feedback = new Feedback(sender, receiver, feedbackOptions, 3.0);
        assertThat(feedback).isNotNull();
        assertThat(feedback.getSender()).isEqualTo(sender);
        assertThat(feedback.getReceiver()).isEqualTo(receiver);
        assertThat(feedback.getFeedbackOptions()).isEqualTo(feedbackOptions);
        assertThat(feedback.getRating()).isEqualTo(3.0);
    }

    @Test
    @DisplayName("Creating feedback with invalid rating (more than 3.0) throws exception")
    void creatingFeedbackWithInvalidHighRating() {
        assertThrows(InstantiationException.class, () -> {
            new Feedback(sender, receiver, feedbackOptions, 3.5);
        });
    }

    @Test
    @DisplayName("Creating feedback with invalid rating (less than 0.0) throws exception")
    void creatingFeedbackWithInvalidLowRating() {
        assertThrows(InstantiationException.class, () -> {
            new Feedback(sender, receiver, feedbackOptions, -1.0);
        });
    }

    @Test
    @DisplayName("Creating feedback with invalid rating (not a multiple of 0.5) throws exception")
    void creatingFeedbackWithNonMultipleRating() {
        assertThrows(InstantiationException.class, () -> {
            new Feedback(sender, receiver, feedbackOptions, 2.3);
        });
    }

    @Test
    @DisplayName("Creating feedback with invalid rating (round up to nearest 0.5) throws exception")
    void creatingFeedbackWithInvalidRoundUpRating() {
        assertThrows(InstantiationException.class, () -> {
            new Feedback(sender, receiver, feedbackOptions, 2.7);
        });
    }
}