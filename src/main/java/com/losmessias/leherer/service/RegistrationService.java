package com.losmessias.leherer.service;

import com.losmessias.leherer.domain.*;
import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.dto.*;
import com.losmessias.leherer.role.AppUserSex;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;
    private final StudentService studentService;
    private final ProfessorService professorService;
    private final ProfessorSubjectService professorSubjectService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final Environment environment;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var appUser = appUserService.getAppUser(request.getEmail());
        if (!appUser.isEnabled()) {
            throw new IllegalStateException("You have not confirmed your email address yet");
        }
        return generateToken(appUser);
    }

    public String register(RegistrationRequest request) {

        appUserService.validateEmailNotTaken(request.getEmail());

        Student student = studentService.create(
                new Student(
                        request.getEmail(),
                        bCryptPasswordEncoder.encode(request.getPassword()),
                        request.getFirstName(),
                        request.getLastName(),
                        request.getLocation(),
                        request.getPhone(),
                        request.getSex()
                )
        );

        AppUser appUser = appUserService.getAppUser(request.getEmail());

        String token = confirmationTokenService.generateConfirmationToken(appUser);

        String link = "http://" + environment.getProperty("app.mail_response") + "?token=" + token;

        emailService.sendWithHTML(
                request.getEmail(),
                "Confirm your email",
                buildEmail(request.getFirstName(), link, "Confirm yor email", "Welcome to Leherer! The place where you will learn to make your dreams come true. We would like to thank you for registering! "));
        return "Successful Registration";
    }

    public String validateEmailNotTaken(String email) {
        appUserService.validateEmailNotTaken(email);
        return "Email not taken";
    }

    public String registerProfessor(RegistrationProfessorRequest request) {

        appUserService.validateEmailNotTaken(request.getEmail());

        Professor professor = professorService.saveProfessor(
                new Professor(
                        request.getEmail(),
                        bCryptPasswordEncoder.encode(request.getPassword()),
                        request.getFirstName(),
                        request.getLastName(),
                        request.getLocation(),
                        request.getPhone(),
                        request.getSex()
                )
        );

        List<Subject> subjectList = request.getSubjects();
        for (Subject subject : subjectList) {
            professorSubjectService.createAssociation(professor, subject);
        }
        AppUser appUser = appUserService.getAppUser( request.getEmail());

        String token = confirmationTokenService.generateConfirmationToken(appUser);

        String link = "http://" + environment.getProperty("app.mail_response") + "?token=" + token;

        emailService.sendWithHTML(
                request.getEmail(),
                "Confirm your email",
                buildEmail(request.getFirstName(), link, "Confirm yor email", "Welcome to Leherer! The place where your dreams come true. I would like to thank you for registering! "));

        return "Successful Registration";

    }

    public String sendEmailForPasswordChange(String email) {

        AppUser appUser = appUserService.getAppUser(email);

        String token = confirmationTokenService.generateConfirmationToken(appUser);

        String link = "http://" + environment.getProperty("app.mail_response") + "/recover-password?email=" + email + "&token=" + token;


        emailService.sendWithHTML(
                email,
                "Confirm your Email for Password Change",
                buildEmail(appUser.getFirstName(), link, "Confirm your Email for Password Change", "Please do not be so silly to forget you password again! "));

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

    public AuthenticationResponse generateToken(AppUser appUser) {

        Map<String, Object> extraClaims = new HashMap<>();

        extraClaims.put("role", appUser.getRole());
        extraClaims.put("name", appUser.getFirstName());
        extraClaims.put("surname", appUser.getLastName());
        extraClaims.put("id", appUser.getId());

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

    private String buildEmail(String name, String link, String title, String text) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">" + title + "</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">" + text + "Please click on the below link: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Click here</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

}