package com.losmessias.leherer.domain;

import com.losmessias.leherer.domain.enumeration.HomeworkStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "homework")
public class Homework {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "class_reservation_id")
    @NotNull
    private ClassReservation classReservation;

    @OneToOne(fetch = FetchType.EAGER)
    @NotNull
    private Comment assignment;

    // Response to the homework
    @OneToOne(fetch = FetchType.EAGER)
    private Comment response;

    // If in need of more than one file, or to upload both professor and student
    @OneToMany(fetch = FetchType.EAGER)
    private List<File> files;

    @Column
    private HomeworkStatus status;

    @Column
    @NotNull
    private LocalDateTime deadline;

    public Homework(ClassReservation classReservation, Comment assignment, LocalDateTime deadline) {
        this.classReservation = classReservation;
        this.assignment = assignment;
        this.files = new ArrayList<>();
        this.status = HomeworkStatus.PENDING;
        if (deadline.isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Deadline must be in the future");
        else
            this.deadline = deadline;
    }

    public Homework(ClassReservation classReservation, Comment assignment, LocalDateTime deadline, File file) {
        this.classReservation = classReservation;
        this.assignment = assignment;
        this.files = List.of(file);
        this.status = HomeworkStatus.PENDING;
        if (deadline.isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Deadline must be in the future");
        else
            this.deadline = deadline;
    }

    @Override
    public String toString() {
        return "Homework{" +
                "id=" + id +
                ", classReservation=" + classReservation +
                ", assignment=" + assignment +
                ", response=" + response +
                ", status=" + status +
                ", deadline=" + deadline +
                '}';
    }
}
