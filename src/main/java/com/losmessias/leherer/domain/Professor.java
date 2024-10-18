package com.losmessias.leherer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.domain.enumeration.AppUserSex;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.WhereJoinTable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "professor")
@PrimaryKeyJoinColumn(name = "id")
public class Professor extends AppUser {


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "professor_subject",
            joinColumns = @JoinColumn(name = "professor_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    @WhereJoinTable(clause = "status = 1")
    private Set<Subject> subjects;

    public Professor(String email, String password, String firstName, String lastName, String location, String phone, AppUserSex appUserSex) {
        super(email, password, firstName, lastName, location, phone, AppUserRole.PROFESSOR, appUserSex);
        this.subjects = new HashSet<>();
    }

    public void addSubject(Subject subject) {
        this.subjects.add(subject);
    }

    public void removePendingClassFeedback(List<ClassReservation> classes) {
        classes.forEach(cla -> this.getPendingClassesFeedbacks().removeIf(id -> Objects.equals(cla.getId(), id)));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority =
                new SimpleGrantedAuthority(AppUserRole.PROFESSOR.name());
        return Collections.singletonList(authority);
    }

}