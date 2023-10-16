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

        emailService.sendEmail(student.getFirstName(), "Class Reservation confirmed", textStudent, student.getEmail());
        emailService.sendEmail(professor.getFirstName(), "Class Reservation confirmed", textProfessor, professor.getEmail());;
    }

    public void cancelClassReservedNotification(ClassReservation classReservation, AppUserRole role){

        Student student = classReservation.getStudent();
        Professor professor = classReservation.getProfessor();

        if (role == AppUserRole.PROFESSOR){
            String textStudent = professor.getFirstName() + " " + professor.getLastName() + " has cancelled the class of " +  classReservation.getSubject().getName() +
                     " from: " + classReservation.getStartingHour() + " to: " + classReservation.getEndingHour() + ".\n The date: " + classReservation.getDate() + ". ";
            NotificationStudent notificationStudent = new NotificationStudent(student, textStudent);
            notificationStudentRepository.save(notificationStudent);
            emailService.sendEmail(student.getFirstName(), "Class Reservation cancelled", textStudent, student.getEmail());
        }else{
            String textProfessor = student.getFirstName() + " " + student.getLastName() + " has cancelled the class of " +  classReservation.getSubject().getName() +
                     " from: " + classReservation.getStartingHour() + " to: " + classReservation.getEndingHour() + ".\n The date: " + classReservation.getDate() + ". ";
            NotificationProfessor notificationProfessor = new NotificationProfessor(professor, textProfessor);
            notificationProfessorRepository.save(notificationProfessor);
            emailService.sendEmail(professor.getFirstName(), "Class Reservation confirmed", textProfessor, professor.getEmail());;
        }
    }

    public void lecturedApprovedByAdminNotification(List<ProfessorSubject> approvedSubjects ){
        ProfessorSubject approvedSubject = approvedSubjects.get(0);
        Professor professor = approvedSubject.getProfessor();
        String textProfessor = professor.getFirstName() + ", " + approvedSubject.getSubject().getName() + " has has been approved!";
        NotificationProfessor notificationProfessor = new NotificationProfessor(professor, textProfessor);
            notificationProfessorRepository.save(notificationProfessor);
        emailService.sendEmail(professor.getFirstName(), "Approval of Subject", textProfessor, professor.getEmail());
    }

    public void lecturedRejectedByAdminNotification(List<ProfessorSubject> rejectedSubjects ){
        ProfessorSubject rejectedSubject = rejectedSubjects.get(0);
        Professor professor = rejectedSubject.getProfessor();
        String textProfessor = professor.getFirstName() + ", " + rejectedSubject.getSubject().getName() + " has has been rejected!";
        NotificationProfessor notificationProfessor = new NotificationProfessor(professor, textProfessor);
        notificationProfessorRepository.save(notificationProfessor);
        emailService.sendEmail(professor.getFirstName(), "Disapproval of Subject", textProfessor, professor.getEmail());
    }
}
