package com.losmessias.leherer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.domain.enumeration.Feedback;
import com.losmessias.leherer.domain.enumeration.Rating;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "valorization")
public class Valorization {
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
    private Set<Feedback> feedbacks;
    @Column
    private LocalDateTime dateTimeOfFeedback;
    @Column
    private Double rating;

    public Valorization(Student student, Professor professor, AppUserRole role, Set<Feedback> feedbacks, Double rating){
        this.student = student;
        this.professor = professor;
        this.receptorRole = role;
        this.feedbacks = feedbacks;
        this.rating = rating;
        this.dateTimeOfFeedback = LocalDateTime.now();
    }

}
