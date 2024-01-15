package com.losmessias.leherer.domain;

import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.domain.enumeration.FeedbackOptions;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "feedback")
@EntityListeners(AuditingEntityListener.class)
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "sender_id")
    private AppUser sender;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "receiver_id")
    private AppUser receiver;

    @Column(nullable = false)
    private AppUserRole receptorRole;

    @ElementCollection
    @Column(nullable = false)
    private Set<FeedbackOptions> feedbackOptions;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateTimeOfFeedback;

    @Column(nullable = false)
    private Double rating;

    public Feedback(AppUser sender, AppUser receiver, Set<FeedbackOptions> feedbackOptions, Double rating) throws InstantiationException {
        verifySenderAndReceiverAreDifferentObjectsClasses(sender, receiver);
        this.sender = sender;
        this.receiver = receiver;
        this.feedbackOptions = feedbackOptions;
        this.rating = verifyRating(rating);
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
