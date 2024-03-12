package com.losmessias.leherer.domain;

import com.losmessias.leherer.domain.enumeration.AppUserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class LoadedData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "class_reservation_id")
    private ClassReservation classReservation;
    @Column
    private LocalDateTime uploadedDateTime;
    @Column
    private AppUserRole role;
    @Column
    private Long associatedId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "homework_id")
    private Homework homework;

    public LoadedData(ClassReservation classReservation, LocalDateTime uploadedDateTime, AppUserRole role, Long associatedId) {
        super();
        this.classReservation = classReservation;
        this.uploadedDateTime = uploadedDateTime;
        this.role = role;
        this.associatedId = associatedId;
    }
}
