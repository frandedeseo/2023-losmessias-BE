package com.losmessias.leherer.domain;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private AppUser appUser;

    @Column(name = "message")
    private String message;

    @Column(name = "opened")
    private boolean opened;

    public Notification(AppUser appUser, String message) {
        this.appUser = appUser;
        this.message = message;
        this.opened = false;
    }

}
