package com.losmessias.leherer.domain;

import com.losmessias.leherer.domain.enumeration.AppUserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comment")
public class Comment extends LoadedData {

    @Column(nullable = false)
    @NotEmpty(message = "You can not send empty comments")
    private String comment;

    public Comment(String comment, ClassReservation classReservation, LocalDateTime uploadedDateTime, AppUserRole role, Long associatedId) {
        super(classReservation, uploadedDateTime, role, associatedId);
        this.comment = comment;
    }
}
