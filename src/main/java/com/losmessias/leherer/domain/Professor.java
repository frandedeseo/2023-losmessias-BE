package com.losmessias.leherer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.losmessias.leherer.role.AppUserSex;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.WhereJoinTable;

import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "professor")
public class Professor {

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
    @Column
    private Double avgRating;
    @Column
    private Integer sumMaterial;
    @Column
    private Integer sumPunctuality;
    @Column
    private Integer sumPolite;
    @Column
    private Integer amountOfRatings;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "professor_subject",
            joinColumns = @JoinColumn(name = "professor_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    @WhereJoinTable(clause = "status = 1")
    private Set<Subject> subjects;
    @JsonIgnore
    @OneToMany(mappedBy = "professor", fetch = FetchType.LAZY)
    private List<ClassReservation> classReservations;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Long> pendingClassesFeedbacks;

    public Professor(String firstName, String lastName, String email, String location, String phone, AppUserSex appUserSex) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.location = location;
        this.phone = phone;
        this.subjects = new HashSet<>();
        this.classReservations = new ArrayList<>();
        this.sex = appUserSex;
        this.avgRating = 0.0;
        this.amountOfRatings = 0;
        this.sumMaterial = 0;
        this.sumPunctuality = 0;
        this.sumPolite = 0;
        this.pendingClassesFeedbacks = new ArrayList<>();
    }

    public void addSubject(Subject subject) {
        this.subjects.add(subject);
    }

    public void receiveFeedback(Double rating, Boolean material, Boolean punctuality, Boolean polite) {
        this.avgRating = (this.avgRating * this.amountOfRatings + rating) / (this.amountOfRatings + 1);
        if (material) this.sumMaterial++;
        if (punctuality) this.sumPunctuality++;
        if (polite) this.sumPolite++;
        this.amountOfRatings++;
    }

    public void addPendingClassFeedback(Long classId) {
        if (!this.pendingClassesFeedbacks.contains(classId))this.pendingClassesFeedbacks.add(classId);
    }

    public void removePendingClassFeedback(List<ClassReservation> classes) {
        classes.forEach(cla -> this.pendingClassesFeedbacks.removeIf(id -> Objects.equals(cla.getId(), id)));
    }


    public void giveFeedbackFor(Long classId) {
        this.pendingClassesFeedbacks.remove(classId);
    }


    @Override
    public String toString() {
        return "Professor{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    public String toJson() {
        return "{" +
                "\"id\":" + id +
                ", \"firstName\":\"" + firstName + '\"' +
                ", \"lastName\":\"" + lastName + '\"' +
                ", \"email\":\"" + email + '\"' +
                ", \"location\":\"" + location + '\"' +
                ", \"phone\":\"" + phone + '\"' +
                '}';
    }

}