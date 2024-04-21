package com.losmessias.leherer.domain;

import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.domain.enumeration.HomeworkStatus;
import com.losmessias.leherer.dto.HomeworkResponseDto;
import com.losmessias.leherer.repository.CommentRepository;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.time.ZoneId;

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

    // If in need of more than one file, or to upload both professor and student
    @OneToOne(fetch = FetchType.EAGER)
    private File assignmentFile;

    // Response to the homework
    @OneToOne(fetch = FetchType.EAGER)
    private Comment response;

    @OneToOne(fetch = FetchType.EAGER)
    private File responseFile;

    @Column
    private HomeworkStatus status;

    @Column
    @NotNull
    private LocalDateTime deadline;

    public Homework(ClassReservation classReservation, Comment assignment, LocalDateTime deadline, File file) {
        this.classReservation = classReservation;
        this.assignment = assignment;
        this.assignmentFile = file;
        this.responseFile = null;
        this.status = HomeworkStatus.PENDING;
//        if (deadline.isBefore(LocalDateTime.now()))
        if (deadline.isBefore(convertToGMTMinus3(LocalDateTime.now())))
            throw new IllegalArgumentException("Deadline must be in the future");
        else
            this.deadline = deadline;
    }

    public void respondWith(HomeworkResponseDto homeworkResponseDto, CommentRepository commentRepository) {
        if (homeworkResponseDto.getAssociatedId() == null)
            throw new IllegalArgumentException("Associated id must not be null");
        if (homeworkResponseDto.getResponse() != null) {
            Comment comment = new Comment(homeworkResponseDto.getResponse(), this.classReservation, LocalDateTime.now(), AppUserRole.STUDENT, homeworkResponseDto.getAssociatedId(), true);
            commentRepository.save(comment);
            this.setResponse(comment);
        }
        this.setStatus(HomeworkStatus.DONE);
        if (homeworkResponseDto.getFile() != null) {
            this.setResponseFile(homeworkResponseDto.getFile());
        }
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

    private static LocalDateTime convertToGMTMinus3(LocalDateTime gmtDateTime) {
        ZoneId gmtZone = ZoneId.of("GMT");
        return gmtDateTime.atZone(gmtZone)
                .withZoneSameInstant(ZoneId.of("GMT-3"))
                .toLocalDateTime();
    }
}
