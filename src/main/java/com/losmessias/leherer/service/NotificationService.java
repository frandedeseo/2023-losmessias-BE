package com.losmessias.leherer.service;


import com.losmessias.leherer.domain.*;
import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.repository.NotificationProfessorRepository;
import com.losmessias.leherer.repository.NotificationStudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationProfessorRepository notificationProfessorRepository;
    private final NotificationStudentRepository notificationStudentRepository;
    private final EmailService emailService;

    public List<NotificationStudent> getStudentNotifications(Long id) {
        return notificationStudentRepository.findByStudentId(id);
    }
    public List<NotificationProfessor> getProfessorNotifications(Long id) {
        return notificationProfessorRepository.findByProfessorId(id);
    }
    public NotificationProfessor setProfessorNotificationToOpened(Long id){
        Optional<NotificationProfessor> notificationProfessorOptional = Optional.ofNullable(notificationProfessorRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new));
        NotificationProfessor notificationProfessor = notificationProfessorOptional.get();
        notificationProfessor.setOpened(true);
        return notificationProfessorRepository.save(notificationProfessor);
    }

    public NotificationStudent setStudentNotificationToOpened(Long id) {
        Optional<NotificationStudent> notificationStudentOptional = Optional.ofNullable(notificationStudentRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new));
        NotificationStudent notificationStudent = notificationStudentOptional.get();
        notificationStudent.setOpened(true);
        return notificationStudentRepository.save(notificationStudent);
    }
    public void generateClassReservedNotification(ClassReservation classReservation){
        Student student = classReservation.getStudent();
        Professor professor = classReservation.getProfessor();

        String textStudent = "You have reserved a class of "+ classReservation.getSubject().getName() +" with " + professor.getFirstName() + " " + professor.getLastName()
                + ".\n From: " + classReservation.getStartingHour() + " to: " + classReservation.getEndingHour() + ".\n The date: " + classReservation.getDate() + ". ";
        String textProfessor = "You have been reserved a class of "+ classReservation.getSubject().getName() +" with " + student.getFirstName() + " " + student.getLastName()
                + ".\n From: " + classReservation.getStartingHour() + " to: " + classReservation.getEndingHour() + ".\n The date: " + classReservation.getDate() + ". ";

        NotificationStudent notificationStudent = new NotificationStudent(student, textStudent);
        NotificationProfessor notificationProfessor = new NotificationProfessor(professor, textProfessor);

        notificationStudentRepository.save(notificationStudent);
        notificationProfessorRepository.save(notificationProfessor);

        String studentBody = buildEmail(student.getFirstName(), "Class Reservation confirmed", textStudent);
        emailService.sendWithHTML(student.getEmail(), "Class Reservation confirmed", studentBody);
        String professorBody = buildEmail(professor.getFirstName(), "Class Reservation confirmed", textProfessor);
        emailService.sendWithHTML(professor.getEmail(), "Class Reservation confirmed", professorBody);
    }

    public void cancelClassReservedNotification(ClassReservation classReservation, AppUserRole role){

        Student student = classReservation.getStudent();
        Professor professor = classReservation.getProfessor();

        if (role == AppUserRole.PROFESSOR){
            String textStudent = professor.getFirstName() + " " + professor.getLastName() + " has cancelled the class of " +  classReservation.getSubject().getName() +
                     " from: " + classReservation.getStartingHour() + " to: " + classReservation.getEndingHour() + ".\n The date: " + classReservation.getDate() + ". ";
            NotificationStudent notificationStudent = new NotificationStudent(student, textStudent);
            notificationStudentRepository.save(notificationStudent);
            String studentBody = buildEmail(student.getFirstName(), "Class Reservation cancelled", textStudent);
            emailService.sendWithHTML(student.getEmail(), "Class Reservation cancelled", studentBody);
        }else{
            String textProfessor = student.getFirstName() + " " + student.getLastName() + " has cancelled the class of " +  classReservation.getSubject().getName() +
                     " from: " + classReservation.getStartingHour() + " to: " + classReservation.getEndingHour() + ".\n The date: " + classReservation.getDate() + ". ";
            NotificationProfessor notificationProfessor = new NotificationProfessor(professor, textProfessor);
            notificationProfessorRepository.save(notificationProfessor);
            String professorBody = buildEmail(professor.getFirstName(), "Class Reservation confirmed", textProfessor);
            emailService.sendWithHTML(professor.getEmail(), "Class Reservation confirmed", professorBody);
        }
    }

    public void lecturedApprovedByAdminNotification(List<ProfessorSubject> approvedSubjects ){
        ProfessorSubject approvedSubject = approvedSubjects.get(0);
        Professor professor = approvedSubject.getProfessor();
        String textProfessor = professor.getFirstName() + ", " + approvedSubject.getSubject().getName() + " has has been approved!";
        NotificationProfessor notificationProfessor = new NotificationProfessor(professor, textProfessor);
            notificationProfessorRepository.save(notificationProfessor);
        String professorBody = buildEmail(professor.getFirstName(), "Approval of Subject", textProfessor);
        emailService.sendWithHTML(professor.getEmail(), "Approval of Subject", professorBody);
    }

    public void lecturedRejectedByAdminNotification(List<ProfessorSubject> rejectedSubjects ){
        ProfessorSubject rejectedSubject = rejectedSubjects.get(0);
        Professor professor = rejectedSubject.getProfessor();
        String textProfessor = professor.getFirstName() + ", " + rejectedSubject.getSubject().getName() + " has has been rejected!";
        NotificationProfessor notificationProfessor = new NotificationProfessor(professor, textProfessor);
        notificationProfessorRepository.save(notificationProfessor);
        String professorBody = buildEmail(professor.getFirstName(), "Disapproval of Subject", textProfessor);
        emailService.sendWithHTML(professor.getEmail(), "Disapproval of Subject", professorBody);
    }

    private String buildEmail(String name, String title, String text) {
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
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">" + text + " <p>See you soon</p>" +
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
