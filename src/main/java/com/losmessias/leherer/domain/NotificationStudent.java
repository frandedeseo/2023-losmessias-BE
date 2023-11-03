package com.losmessias.leherer.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "notification_student")
public class NotificationStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(name = "message")
    private String message;

    @Column(name = "opened")
    private boolean opened;

    public NotificationStudent(Student student, String message) {
        this.student = student;
        this.message = message;
        this.opened = false;
    }

}
