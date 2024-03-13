package com.losmessias.leherer.domain;

import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.domain.enumeration.HomeworkStatus;
import com.losmessias.leherer.dto.HomeworkResponseDto;
import com.losmessias.leherer.repository.CommentRepository;
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
    private Comment assignment;

    // Response to the homework
    @OneToOne(fetch = FetchType.EAGER)
    private Comment response;

    // If in need of more than one file, or to upload both professor and student
    @OneToMany(fetch = FetchType.LAZY)
    private List<File> files;

    @Column
    private HomeworkStatus status;

    @Column
    @NotNull
    private LocalDateTime deadline;

    public Homework(ClassReservation classReservation, Comment assignment, LocalDateTime deadline, List<File> files) {
        this.classReservation = classReservation;
        this.assignment = assignment;
        if (files == null) this.files = new ArrayList<>();
        else this.files = files;
        this.status = HomeworkStatus.PENDING;
        if (deadline.isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Deadline must be in the future");
        else
            this.deadline = deadline;
    }

    public void respondWith(HomeworkResponseDto homeworkResponseDto, CommentRepository commentRepository) {
        if (homeworkResponseDto.getResponse() == null) throw new IllegalArgumentException("Response must not be null");
        if (homeworkResponseDto.getAssociatedId() == null)
            throw new IllegalArgumentException("Associated id must not be null");
        Comment comment = new Comment(homeworkResponseDto.getResponse(), this.classReservation, LocalDateTime.now(), AppUserRole.STUDENT, homeworkResponseDto.getAssociatedId(), true);
        commentRepository.save(comment);
        this.setResponse(comment);
        this.setStatus(HomeworkStatus.DONE);
        if (homeworkResponseDto.getFile() != null)
            this.files.add(homeworkResponseDto.getFile());
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
