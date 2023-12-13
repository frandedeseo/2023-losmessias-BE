package com.losmessias.leherer.domain;

import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.domain.enumeration.FeedbackOptions;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sender_id")
    private AppUser sender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "receiver_id")
    private AppUser receiver;

    @Column
    private AppUserRole receptorRole;
    @ElementCollection
    private Set<FeedbackOptions> feedbackOptions;
    @Column
    private LocalDateTime dateTimeOfFeedback;
    @Column
    private Double rating;

    public Feedback(AppUser sender, AppUser receiver, Set<FeedbackOptions> feedbackOptions, Double rating) throws InstantiationException {
        verifySenderAndReceiverAreDifferentObjectsClasses(sender, receiver);
        this.sender = sender;
        this.receiver = receiver;
        this.feedbackOptions = feedbackOptions;
        this.rating = verifyRating(rating);
        this.dateTimeOfFeedback = LocalDateTime.now();
    }

    private Double verifyRating(Double rating) throws InstantiationException {
        if (rating< 0 || rating > 3 || rating % 0.5 != 0){
            throw new InstantiationException("Invalid rating");
        }
        return rating;
    }

    private void verifySenderAndReceiverAreDifferentObjectsClasses(AppUser sender, AppUser receiver) throws InstantiationException {
        if ((sender instanceof Student && receiver instanceof Student) || (sender instanceof Professor && receiver instanceof Professor)){
            throw new InstantiationException("Invalid feedback");
        }
    }

//    @Override
//    public String toString() {
//        return "Feedback{" +
//                "id=" + id +
//                ", student=" + student +
//                ", professor=" + professor +
//                ", receptorRole=" + receptorRole +
//                ", feedbackOptions=" + feedbackOptions +
//                ", dateTimeOfFeedback=" + dateTimeOfFeedback +
//                ", rating=" + rating +
//                '}';
//    }

}
