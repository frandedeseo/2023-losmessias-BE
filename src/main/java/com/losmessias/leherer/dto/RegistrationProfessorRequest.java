package com.losmessias.leherer.dto;

import com.losmessias.leherer.domain.Subject;
import com.losmessias.leherer.domain.enumeration.AppUserSex;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
public class RegistrationProfessorRequest {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final String role;
    private final String location;
    private final String phone;
    private final AppUserSex sex;
    private final List<Subject> subjects;
}
