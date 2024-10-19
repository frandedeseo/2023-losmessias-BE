package com.losmessias.leherer.service;


import com.losmessias.leherer.domain.*;
import com.losmessias.leherer.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;

    public List<Notification> getNotifications(Long id) {
        return notificationRepository.findByAppUserId(id);
    }

    public Notification setNotificationToOpened(Long id) {
        Optional<Notification> notificationOptional = Optional.ofNullable(notificationRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new));
        Notification notification = notificationOptional.get();
        notification.setOpened(true);
        notificationRepository.save(notification);
        return notification;
    }
    public String generateClassReservedNotification(ClassReservation classReservation) {
        Student student = classReservation.getStudent();
        Professor professor = classReservation.getProfessor();

        String textStudent = "You have reserved a class of " + classReservation.getSubject().getName() + " with "
                + professor.getFirstName() + " " + professor.getLastName()
                + ".\n From: " + classReservation.getStartingHour() + " to: " + classReservation.getEndingHour()
                + ".\n The date: " + classReservation.getDate() + ".\n Google Meet Link: " + classReservation.getGoogleMeetLink();

        String textProfessor = "You have been reserved a class of " + classReservation.getSubject().getName() + " with "
                + student.getFirstName() + " " + student.getLastName()
                + ".\n From: " + classReservation.getStartingHour() + " to: " + classReservation.getEndingHour()
                + ".\n The date: " + classReservation.getDate() + ".\n Google Meet Link: " + classReservation.getGoogleMeetLink();

        // Build the iCalendar file content for the event invitation
        String icsContent = generateICSFile(classReservation);

        // Send notification to the student
        String studentBody = buildEmail(student.getFirstName(), "Class Reservation confirmed", textStudent);
        emailService.sendWithHTMLAndAttachment(student.getEmail(), "Class Reservation confirmed", studentBody, icsContent);

        // Send notification to the professor
        String professorBody = buildEmail(professor.getFirstName(), "Class Reservation confirmed", textProfessor);
        emailService.sendWithHTMLAndAttachment(professor.getEmail(), "Class Reservation confirmed", professorBody, icsContent);

        return "Notifications sent successfully";
    }

    // Method to generate iCalendar (ICS) content for the class reservation
    private String generateICSFile(ClassReservation classReservation) {
        String uuid = UUID.randomUUID().toString();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HHmmss");

        String startDate = classReservation.getDate().format(dateFormatter);
        String startTime = classReservation.getStartingHour().format(timeFormatter) + "Z"; // Assuming UTC time
        String endTime = classReservation.getEndingHour().format(timeFormatter) + "Z"; // Assuming UTC time

        StringBuilder icsContent = new StringBuilder();
        icsContent.append("BEGIN:VCALENDAR\n")
                .append("VERSION:2.0\n")
                .append("PRODID:-//YourCompany//ClassReservation//EN\n")
                .append("BEGIN:VEVENT\n")
                .append("UID:").append(uuid).append("\n")
                .append("DTSTAMP:").append(startDate).append("T").append(startTime).append("\n")
                .append("DTSTART:").append(startDate).append("T").append(startTime).append("\n")
                .append("DTEND:").append(startDate).append("T").append(endTime).append("\n")
                .append("SUMMARY:Class Reservation - ").append(classReservation.getSubject().getName()).append("\n")
                .append("DESCRIPTION:Google Meet Link: ").append(classReservation.getGoogleMeetLink()).append("\n")
                .append("LOCATION:Google Meet\n")
                .append("BEGIN:VALARM\n")
                .append("TRIGGER:-PT10M\n")
                .append("ACTION:DISPLAY\n")
                .append("DESCRIPTION:Reminder\n")
                .append("END:VALARM\n")
                .append("END:VEVENT\n")
                .append("END:VCALENDAR");

        return icsContent.toString();
    }

    public String cancelClassReservedNotification(ClassReservation classReservation, AppUser appUser){

        String text = appUser.getFirstName() + " " + appUser.getLastName() + " has cancelled the class of " +  classReservation.getSubject().getName() +
                 " from: " + classReservation.getStartingHour() + " to: " + classReservation.getEndingHour() + ".\n The date: " + classReservation.getDate() + ". ";
        Notification notification= new Notification(appUser, text);
        notificationRepository.save(notification);
        String body = buildEmail(appUser.getFirstName(), "Class Reservation cancelled", text);
        emailService.sendWithHTML(appUser.getEmail(), "Class Reservation cancelled", body);

        return "Notification sent successfully";
    }

    public String lecturedApprovedByAdminNotification(List<ProfessorSubject> approvedSubjects ){
        //TODO no trabajar con listas si siempre se aprueba y se rechaza solo una subject
        ProfessorSubject approvedSubject = approvedSubjects.get(0);
        Professor professor = approvedSubject.getProfessor();
        String textProfessor = professor.getFirstName() + ", " + approvedSubject.getSubject().getName() + " has has been approved!";
        Notification notificationProfessor = new Notification(professor, textProfessor);
        notificationRepository.save(notificationProfessor);
        String professorBody = buildEmail(professor.getFirstName(), "Approval of Subject", textProfessor);
        emailService.sendWithHTML(professor.getEmail(), "Approval of Subject", professorBody);
        return "Notification sent successfully";
    }

    public String lecturedRejectedByAdminNotification(List<ProfessorSubject> rejectedSubjects ){
        ProfessorSubject rejectedSubject = rejectedSubjects.get(0);
        Professor professor = rejectedSubject.getProfessor();
        String textProfessor = professor.getFirstName() + ", " + rejectedSubject.getSubject().getName() + " has has been rejected!";
        Notification notificationProfessor = new Notification(professor, textProfessor);
        notificationRepository.save(notificationProfessor);
        String professorBody = buildEmail(professor.getFirstName(), "Disapproval of Subject", textProfessor);
        emailService.sendWithHTML(professor.getEmail(), "Disapproval of Subject", professorBody);
        return "Notification sent successfully";
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
