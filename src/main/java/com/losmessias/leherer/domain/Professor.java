package com.losmessias.leherer.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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

    @ManyToMany(
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "professor_subject",
            joinColumns = @JoinColumn(name = "professor_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private Set<Subject> subjects;

    public Professor(String firstName, String lastName, String email, String location, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.location = location;
        this.phone = phone;
        this.subjects = new HashSet<>();
    }


    public void addSubject(Subject subject) {
        this.subjects.add(subject);
    }

    @Override
    public String toString() {
        return "Professor{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", subjects=" + subjects +
                '}';
    }

}