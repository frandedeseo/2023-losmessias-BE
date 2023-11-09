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
    private Integer lengthOfRating;

    public Student(String firstName, String lastName, String email, String location, String phone, AppUserSex appUserSex) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.location = location;
        this.classReservations = new ArrayList<>();
        this.phone = phone;
        this.sex = appUserSex;
        this.avgRating = -1.0;
        this.lengthOfRating = 0;
        this.sumMaterial = 0;
        this.sumPunctuality = 0;
        this.sumEducated = 0;
    }

    public void addReservation(ClassReservation classReservation) {
        this.classReservations.add(classReservation);
    }

    public void setFeedback(Double rating, Boolean material, Boolean punctuality, Boolean educated ){
        this.avgRating = (this.avgRating*this.lengthOfRating + rating)/ (this.lengthOfRating + 1);
        if (material){ this.sumMaterial = this.sumMaterial + 1;}
        if (punctuality){ this.sumPunctuality = this.sumPunctuality + 1;}
        if (educated){ this.sumEducated = this.sumEducated + 1;}
        this.lengthOfRating = this.lengthOfRating +1;
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

    public String toJson(){
        return "{" +
                "\"id\":" + id +
                ", \"firstName\":\"" + firstName + '\"' +
                ", \"lastName\":\"" + lastName + '\"' +
                ", \"email\":\"" + email + '\"' +
                ", \"location\":\"" + location + '\"' +
                '}';
    }
}
