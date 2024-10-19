package com.losmessias.leherer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.losmessias.leherer.domain.enumeration.AppUserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.losmessias.leherer.domain.enumeration.AppUserSex;

import java.util.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "app_user")
public abstract class AppUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @JsonIgnore
    @Column(nullable = false)
    @Size(min = 8, message = "Password must be longer than 8 characters")
    private String password;

    private Boolean locked = false;

    private Boolean enabled = false;

    @Column(nullable = false)
    @NotEmpty(message = "First name cannot be empty")
    private String firstName;

    @Column(nullable = false)
    @NotEmpty(message = "Last name cannot be empty")
    private String lastName;

    @Column(nullable = false)
    @NotEmpty(message = "Location cannot be empty")
    private String location;

    @Column(nullable = false)
    @NotEmpty(message = "Phone cannot be empty")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull(message = "Role cannot be empty")
    private AppUserRole role;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false)
    @NotNull(message = "Sex cannot be empty")
    private AppUserSex sex;

    @JoinColumn(name = "feedback_received_id")
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private FeedbackReceived feedbackReceived;

    @ElementCollection(fetch = FetchType.EAGER)
    @JoinColumn(name = "class_reservation_id")
    private List<Long> pendingClassesFeedbacks;

    @JsonIgnore
    @OneToMany( fetch = FetchType.LAZY)
    private List<ClassReservation> classReservations;

    public AppUser(String email, String password, String firstName, String lastName, String location, String phone, AppUserRole role, AppUserSex appUserSex) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.location = location;
        this.phone = phone;
        this.role = role;
        this.feedbackReceived = new FeedbackReceived();
        this.classReservations = new ArrayList<>();
        this.sex = appUserSex;
        this.pendingClassesFeedbacks = new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void addReservation(ClassReservation classReservation) {
        this.classReservations.add(classReservation);
    }

    public void addPendingClassFeedback(Long classId) {
        if (!this.pendingClassesFeedbacks.contains(classId))this.pendingClassesFeedbacks.add(classId);
    }

    public void giveFeedbackFor(Long classId) {
        this.pendingClassesFeedbacks.remove(classId);
    }

    public boolean canMakeAReservation() {
        return this.pendingClassesFeedbacks.isEmpty();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(AppUserRole.USER.name());
        return Collections.singletonList(authority);
    }


    //TODO: Change this functions
    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName=" + lastName +
                ", email='" + email + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    public String toJson() {
        return "{" +
                "\"id\":" + id +
                ", \"firstName\":\"" + firstName + '\"' +
                ", \"lastName\":\"" + lastName + '\"' +
                ", \"email\":\"" + email + '\"' +
                ", \"location\":\"" + location + '\"' +
                '}';
    }
}