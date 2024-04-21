package com.losmessias.leherer.service;

import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.Comment;
import com.losmessias.leherer.domain.File;
import com.losmessias.leherer.domain.Homework;
import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.domain.enumeration.HomeworkStatus;
import com.losmessias.leherer.dto.CommentDto;
import com.losmessias.leherer.repository.HomeworkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeworkService {

    private final HomeworkRepository homeworkRepository;
    private final ClassReservationService classReservationService;
    private final CommentService commentService;
    private final FileService fileService;

    public Homework createHomework(LocalDateTime deadline, String assignment, Long classReservationId, Long associatedId, MultipartFile file) {
        System.out.println("HomeworkService - deadline: " + deadline);
        System.out.println("HomeworkService - LocalDateTime.now(): " + LocalDateTime.now());
        System.out.println("HomeworkService - deadline.isBefore(LocalDateTime.now()): " + deadline.isBefore(LocalDateTime.now()));
        System.out.println("HomeworkService - convertToGMTMinus3(LocalDateTime.now()): " + convertToGMTMinus3(LocalDateTime.now());
        if (deadline.isBefore(convertToGMTMinus3(LocalDateTime.now())))
            throw new IllegalArgumentException("Deadline must be in the future");
        if (assignment == null && file == null)
            throw new IllegalArgumentException("Assignment and files can't be both null");
        if (classReservationId == null) throw new IllegalArgumentException("Class reservation must not be null");
        if (associatedId == null) throw new IllegalArgumentException("Associated id must not be null");

        ClassReservation classReservation = classReservationService.getReservationById(classReservationId);
        Comment comment = null;
        if (assignment != null) {
            CommentDto commentDto = new CommentDto(assignment, classReservationId, LocalDateTime.now(), AppUserRole.PROFESSOR, associatedId, true);
            comment = commentService.upload(commentDto);
        }
        File fileReturned = null;
        if (file != null) {
            fileReturned = fileService.storeFile(file);
            fileReturned = fileService.setBelongingToHomework(fileReturned.getId());
        }
        Homework homework = new Homework(classReservation, comment, deadline, fileReturned);
        return homeworkRepository.save(homework);
    }

    public List<Homework> getAllHomeworks() {
        return homeworkRepository.findAll();
    }

    public Homework getHomeworkById(Long id) {
        return homeworkRepository.findById(id).orElse(null);
    }

    public List<Homework> getHomeworkByClassReservationId(Long id) {
        return homeworkRepository.findByClassReservation_Id(id);
    }

    public boolean verifyIfResponded(Long id) {
        Homework homework = homeworkRepository.findById(id).orElse(null);
        if (homework == null) throw new IllegalArgumentException("Homework not found");
        return homework.getStatus() == HomeworkStatus.DONE;
    }

    private static LocalDateTime convertToGMTMinus3(LocalDateTime gmtDateTime) {
        ZoneId gmtZone = ZoneId.of("GMT");
        return gmtDateTime.atZone(gmtZone)
                .withZoneSameInstant(ZoneId.of("GMT-3"))
                .toLocalDateTime();
    }
}
