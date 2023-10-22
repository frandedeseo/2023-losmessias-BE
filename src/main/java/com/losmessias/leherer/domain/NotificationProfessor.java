package com.losmessias.leherer.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "notification_professor")
public class NotificationProfessor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @Column(name = "message")
    private String message;

    @Column(name = "opened")
    private boolean opened;

    public NotificationProfessor(Professor professor, String message) {
        this.professor = professor;
        this.message = message;
        this.opened = false;
    }

}
