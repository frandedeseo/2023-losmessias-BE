package com.losmessias.leherer.service;

import com.losmessias.leherer.domain.Homework;
import com.losmessias.leherer.repository.HomeworkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeworkService {

    private final HomeworkRepository homeworkRepository;

    public Homework createHomework(Homework homework) {
        if (homework == null) throw new IllegalArgumentException("Homework must not be null");
        if (homework.getDeadline() == null) throw new IllegalArgumentException("Deadline must not be null");
        if (homework.getDeadline().isBefore(java.time.LocalDateTime.now()))
            throw new IllegalArgumentException("Deadline must be in the future");
        if (homework.getAssignment() == null) throw new IllegalArgumentException("Assignment must not be null");
        if (homework.getClassReservation() == null)
            throw new IllegalArgumentException("Class reservation must not be null");

        return homeworkRepository.save(homework);
    }

    public List<Homework> getAllHomeworks() {
        return homeworkRepository.findAll();
    }

    public Homework getHomeworkById(Long id) {
        return homeworkRepository.findById(id).orElse(null);
    }
}
