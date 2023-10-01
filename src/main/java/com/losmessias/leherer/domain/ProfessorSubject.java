package com.losmessias.leherer.domain;

import com.losmessias.leherer.domain.enumeration.SubjectStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "professor_subject")
public class ProfessorSubject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Column(name = "status")
    private SubjectStatus status;

    public ProfessorSubject(Professor professor, Subject subject) {
        this.professor = professor;
        this.subject = subject;
        this.status = SubjectStatus.PENDING;
    }
}
