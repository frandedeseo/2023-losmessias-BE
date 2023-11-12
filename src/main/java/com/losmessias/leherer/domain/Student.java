package com.losmessias.leherer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.losmessias.leherer.role.AppUserSex;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column
    private Long id;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String email;
    @Column
    private String location;
    @Column
    private String phone;
    @Column
    private AppUserSex sex;
    @JsonIgnore
    @OneToMany(mappedBy = "student")
    private List<ClassReservation> classReservations;
    @Column
    private Double avgRating;
    @Column
    private Integer sumMaterial;
    @Column
    private Integer sumPunctuality;
    @Column
    private Integer sumEducated;
    @Column
    private Integer amountOfRatings;
    @ElementCollection
    private List<Long> pendingClassesFeedbacks;

    public Student(String firstName, String lastName, String email, String location, String phone, AppUserSex appUserSex) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.location = location;
        this.classReservations = new ArrayList<>();
        this.phone = phone;
        this.sex = appUserSex;
        this.avgRating = 0.0;
        this.amountOfRatings = 0;
        this.sumMaterial = 0;
        this.sumPunctuality = 0;
        this.sumEducated = 0;
    }

    public void addReservation(ClassReservation classReservation) {
        this.classReservations.add(classReservation);
    }

    public void receiveFeedback(Double rating, Boolean material, Boolean punctuality, Boolean educated) {
        this.avgRating = (this.avgRating * this.amountOfRatings + rating) / (this.amountOfRatings + 1);
        if (material) this.sumMaterial++;
        if (punctuality) this.sumPunctuality++;
        if (educated) this.sumEducated++;
        this.amountOfRatings++;
    }

    public void addPendingClassFeedback(Long feedbackId) {
        this.pendingClassesFeedbacks.add(feedbackId);
    }

    public void giveFeedbackFor(Long feedbackId) {
        // TODO: check if feedbackId is in pendingFeedbacks
        this.pendingClassesFeedbacks.remove(feedbackId);
    }

    public boolean canMakeAReservation() {
        return this.pendingClassesFeedbacks.isEmpty();
    }

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
