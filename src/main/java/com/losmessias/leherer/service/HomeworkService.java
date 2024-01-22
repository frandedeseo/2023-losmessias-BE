package com.losmessias.leherer.service;

import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.Comment;
import com.losmessias.leherer.domain.Homework;
import com.losmessias.leherer.repository.HomeworkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeworkService {

    private final HomeworkRepository homeworkRepository;
    private final ClassReservationService classReservationService;

    public Homework createHomework(Homework homework){
//        public Homework createHomework(LocalDateTime deadline, String assignment, Long classReservationId) {

//        if (deadline.isBefore(java.time.LocalDateTime.now()))
//            throw new IllegalArgumentException("Deadline must be in the future");
//        if (assignment == null) throw new IllegalArgumentException("Assignment must not be null");
//        if (classReservationId == null)
//            throw new IllegalArgumentException("Class reservation must not be null");
//        ClassReservation classReservation = classReservationService.getReservationById(classReservationId);
//        Comment comment = new Comment(assignment, classReservation, LocalDateTime.now(), null, null);
//        Homework homework = new Homework();
        return homeworkRepository.save(homework);
    }

    public List<Homework> getAllHomeworks() {
        return homeworkRepository.findAll();
    }

    public Homework getHomeworkById(Long id) {
        return homeworkRepository.findById(id).orElse(null);
    }
}
