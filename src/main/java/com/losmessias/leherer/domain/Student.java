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
@Table(name = "student")
@PrimaryKeyJoinColumn(name = "id")
public class Student extends AppUser{

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "professor_subject",
            joinColumns = @JoinColumn(name = "professor_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    @WhereJoinTable(clause = "status = 1")
    private Set<Subject> subjects;
    public Student(String email, String password, String firstName, String lastName, String location, String phone, AppUserSex appUserSex) {
        super(email, password, firstName, lastName, location, phone, AppUserRole.STUDENT, appUserSex);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(AppUserRole.STUDENT.name()));
        return authorities;
    }
}
