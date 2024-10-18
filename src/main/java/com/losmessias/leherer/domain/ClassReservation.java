package com.losmessias.leherer.domain;

import com.losmessias.leherer.domain.enumeration.ReservationStatus;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.persistence.Column;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "class_reservation")
public class ClassReservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "professor_id")
    @NotNull
    @Valid
    private Professor professor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private Student student;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime startingHour;

    @Column(nullable = false)
    private LocalTime endingHour;

    @Column
    @Min(value=0)
    private Double duration;

    @Column
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @Column
    @Min(0)
    private Double price;

    @Column(name = "google_calendar_event_id")
    private String googleCalendarEventId;

    @Column(name = "google_meet_link")
    private String googleMeetLink;

    public ClassReservation(Professor professor,
                            Subject subject,
                            Student student,
                            LocalDate date,
                            LocalTime startingTime,
                            LocalTime endingTime,
                            Double price) {
        if (startingTime.isAfter(endingTime))
            throw new IllegalArgumentException("Starting time must be before ending time");
        this.professor = professor;
        this.subject = subject;
        this.student = student;
        this.date = date;
        this.startingHour = startingTime;
        this.endingHour = endingTime;
        this.duration = calculateDuration(startingTime, endingTime);
        this.price = price;
        this.status = ReservationStatus.CONFIRMED;
    }

    //Unavailable reservation initialization
    public ClassReservation(Professor professor, LocalDate date, LocalTime startingHour, LocalTime endingHour) {
        if (startingHour.isAfter(endingHour))
            throw new IllegalArgumentException("Starting time must be before ending time");
        this.professor = professor;
        this.date = date;
        this.startingHour = startingHour;
        this.endingHour = endingHour;
        this.duration = calculateDuration(startingHour, endingHour);
        this.status = ReservationStatus.NOT_AVAILABLE;
    }

    private Double calculateDuration( LocalTime startingTime, LocalTime endingTime){
        return (endingTime.getHour() - startingTime.getHour()) + ((endingTime.getMinute() - startingTime.getMinute()) / 60.0);
    }

    @Override
    public String toString() {
        return "ClassReservation{" +
                "id=" + id +
                ", professor=" + professor +
                ", subject=" + subject +
                ", student=" + student +
                ", date=" + date +
                ", startingHour=" + startingHour +
                ", endingHour=" + endingHour +
                ", status=" + status +
                ", price=" + price +
                '}';
    }
}
