package com.losmessias.leherer.domain;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "confirmation_token")
@EntityListeners(AuditingEntityListener.class)
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    @NotEmpty(message = "Token can not be an empty string")
    private String token;

  //  @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

//    @Column(nullable = false, updatable = false)
//    private LocalDateTime expiresAt = {() -> if (createdAt != null) {return createdAt.plusMinutes(15)}};

    @Column
    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(nullable = false, name = "app_user_id")
    @Valid
    private AppUser appUser;

    public ConfirmationToken(String token, AppUser user) {
        this.token = token;
        this.appUser = user;
    }
}
