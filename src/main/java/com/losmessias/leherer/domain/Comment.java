package com.losmessias.leherer.domain;

import com.losmessias.leherer.domain.enumeration.AppUserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comment")
public class Comment extends LoadedData {

    @Column
    private String comment;

    public Comment(String comment, ClassReservation classReservation, LocalDateTime uploadedDateTime, AppUserRole role, Long associatedId) {
        super(classReservation, uploadedDateTime, role, associatedId);
        this.comment = comment;
    }
}
