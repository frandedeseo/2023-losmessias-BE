package com.losmessias.leherer.service;

import com.losmessias.leherer.domain.*;
import com.losmessias.leherer.dto.*;
import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.role.AppUserSex;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;
    private final EmailService emailService;
    private final StudentService studentService;
    private final ProfessorService professorService;
    private final ProfessorSubjectService professorSubjectService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse authenticate(AuthenticationRequest request){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var appUser = appUserService.getAppUser(request.getEmail());
        if (!appUser.isEnabled()){
             throw new IllegalStateException("You have not confirmed your email address yet");
        }
        return generateToken(appUser);
    }

    public String register(RegistrationRequest request) {

        appUserService.validateEmailNotTaken(request.getEmail());

        Long id;
        AppUserRole role;
        AppUserSex sex;
        if (request.getSex().equals("Male")){
            sex = AppUserSex.MALE;
        }else{
            sex = AppUserSex.FEMALE;
        }
        role=AppUserRole.STUDENT;
        Student student = studentService.create(
                new Student(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getLocation(),
                        request.getPhone(),
                        sex
                )
        );
        id = student.getId();
        
        AppUser appUser = new AppUser(
                request.getEmail(),
                request.getPassword(),
                role,
                id
        );

        appUserService.signUpUser(appUser);

        String token = confirmationTokenService.generateConfirmationToken(appUser);

        String link = "http://localhost:3000?token=" + token;

        emailService.sendEmailWithLink(request.getFirstName(), link, "Confirm yor email", "Welcome to Leherer! The place where your dreams come true. I would like to thank you for registering! ", request.getEmail());

        return "Successful Registration";
    }

    public String validateEmailNotTaken(String email){
        appUserService.validateEmailNotTaken(email);
        return "Email not taken";
    }
    public String registerProfessor(RegistrationProfessorRequest request) {

        appUserService.validateEmailNotTaken(request.getEmail());

        Long id;
        AppUserRole role;
        AppUserSex sex;
        if (request.getSex().equals("Male")){
            sex = AppUserSex.MALE;
        }else {
            sex = AppUserSex.FEMALE;
        }
        role =AppUserRole.PROFESSOR;
        Professor professor = professorService.saveProfessor(
                new Professor(
                        request.getFirstName(),
                        request.getLastName(),
                        request.getEmail(),
                        request.getLocation(),
                        request.getPhone(),
                        sex
                )
        );
        id = professor.getId();

        AppUser appUser = new AppUser(
                request.getEmail(),
                request.getPassword(),
                role,
                id
        );
        List<Subject> subjectList = request.getSubjects();
        for (Subject subject: subjectList) {
            professorSubjectService.createAssociation(professor, subject);
        }
        appUserService.signUpUser(appUser);

        String token = confirmationTokenService.generateConfirmationToken(appUser);

        String link = "http://localhost:3000?token=" + token;

        emailService.sendEmailWithLink(request.getFirstName(), link, "Confirm yor email", "Welcome to Leherer! The place where your dreams come true. I would like to thank you for registering! ", request.getEmail());

        return "Successful Registration";

    }

    public String sendEmailForPasswordChange(String email){

        AppUser appUser = appUserService.getAppUser(email);

        String firstName;
        if (appUser.getAppUserRole() == AppUserRole.STUDENT){
            firstName = (studentService.getStudentById(appUser.getAssociationId())).getFirstName();
        }else{
            firstName = (professorService.getProfessorById(appUser.getAssociationId())).getFirstName();
        }

        String token = confirmationTokenService.generateConfirmationToken(appUser);

        String link = "http://localhost:3000/recover-password?email="+email+"&token=" + token;

//    emailSender.send(
//            email,
//            buildEmail(firstName, link, "Confirm your Email for Password Change", "Please do not be so silly to forget you password again! "));


        return "Confirm your email for password change";

    }


    @Transactional
    public AuthenticationResponse confirmEmailToken(String token) {

        ConfirmationToken confirmationToken = confirmationTokenService.validateToken(token);

        AppUser appUser = confirmationToken.getAppUser();

        appUserService.enableAppUser(appUser.getEmail());

        confirmationToken.getAppUser().getAuthorities();

        return generateToken(appUser);
    }

    public AuthenticationResponse generateToken(AppUser appUser){

        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", appUser.getAppUserRole());
        extraClaims.put("id", appUser.getAssociationId());

        var jwtToken = jwtService.generateToken(extraClaims, appUser);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Transactional
    public String confirmChangePasswordToken(String token) {

        confirmationTokenService.validateToken(token);

        return "Email confirmed";
    }
    public String changePassword(ForgotPasswordDto request) {

        appUserService.changePassword(request.getEmail(), request.getPassword());

        return "Password changed successfully";
    }

}