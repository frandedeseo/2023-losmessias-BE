package com.losmessias.leherer.domain;

import com.losmessias.leherer.domain.enumeration.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.Locale;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "class_reservation")
public class ClassReservation {

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

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @Column
    private LocalDate date;
    @Column
    private LocalTime startingHour;
    @Column
    private LocalTime endingHour;
    @Column
    private ReservationStatus status;
    @Column
    private Integer price;

    public ClassReservation(Professor professor, Subject subject, Student student, LocalDate date, LocalTime startingHour, LocalTime endingHour, Integer price) {
        this.professor = professor;
        this.subject = subject;
        this.student = student;
        this.date = date;
        this.startingHour = startingHour;
        this.endingHour = endingHour;
        this.price = price;
        this.status = ReservationStatus.CONFIRMED;
    }
}
