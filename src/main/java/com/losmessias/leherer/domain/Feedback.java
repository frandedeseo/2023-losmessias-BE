package com.losmessias.leherer.domain;

import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.domain.enumeration.FeedbackOptions;
import jakarta.persistence.*;
import lombok.*;

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
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @Column
    private AppUserRole receptorRole;
    @ElementCollection
    private Set<FeedbackOptions> feedbackOptions;
    @Column
    private LocalDateTime dateTimeOfFeedback;
    @Column
    private Double rating;

    public Feedback(Student student, Professor professor, AppUserRole role, Set<FeedbackOptions> feedbackOptions, Double rating) {
        this.student = student;
        this.professor = professor;
        this.receptorRole = role;
        this.feedbackOptions = feedbackOptions;
        this.rating = verifyRating(rating);
        this.dateTimeOfFeedback = LocalDateTime.now();
    }

    private Double verifyRating(Double rating) {
        if (rating < 0) return 0.0; // si es menor, determinamos el mínimo
        if (rating > 3) return 3.0; // si es mayor, determinamos el máximo
        if (rating % 0.5 != 0) return Math.round(rating * 2) / 2.0; // si no es múltiplo de 0.5, lo redondeamos
        return rating; // si no, lo devolvemos tal cual
    }

    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", student=" + student +
                ", professor=" + professor +
                ", receptorRole=" + receptorRole +
                ", feedbackOptions=" + feedbackOptions +
                ", dateTimeOfFeedback=" + dateTimeOfFeedback +
                ", rating=" + rating +
                '}';
    }

}
