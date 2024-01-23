package com.losmessias.leherer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.losmessias.leherer.domain.Homework;
import com.losmessias.leherer.dto.HomeworkCreationDto;
import com.losmessias.leherer.service.HomeworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/homework")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class HomeworkController {

    private final HomeworkService homeworkService;

    @GetMapping("/all")
    public ResponseEntity<String> getAllHomeworks() throws JsonProcessingException {
        List<Homework> homeworks = homeworkService.getAllHomeworks();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        if (homeworks.isEmpty()) return new ResponseEntity<>("No homeworks found", HttpStatus.NOT_FOUND);

        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(homeworks));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getHomeworkById(@PathVariable("id") Long id) throws JsonProcessingException {
        if (id < 0) return new ResponseEntity<>("Id must be positive", HttpStatus.BAD_REQUEST);
        Homework homework = homeworkService.getHomeworkById(id);
        if (homework == null) return new ResponseEntity<>("No homework found with id " + id, HttpStatus.NOT_FOUND);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(homework));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createHomework(@RequestBody HomeworkCreationDto homework) throws JsonProcessingException {
        if (homework.getAssignment() == null) return new ResponseEntity<>("Assignment must not be null", HttpStatus.BAD_REQUEST);
        if (homework.getClassReservationId() == null) return new ResponseEntity<>("Class reservation must not be null", HttpStatus.BAD_REQUEST);
        if (homework.getProfessorId() == null) return new ResponseEntity<>("Professor id must not be null", HttpStatus.BAD_REQUEST);
        if (homework.getDeadline() == null) return new ResponseEntity<>("Deadline must not be null", HttpStatus.BAD_REQUEST);
        if (homework.getDeadline().isBefore(LocalDateTime.now())) return new ResponseEntity<>("Deadline must be in the future", HttpStatus.BAD_REQUEST);
        Homework createdHomework = homeworkService.createHomework(homework.getDeadline(), homework.getAssignment(), homework.getClassReservationId(), homework.getProfessorId(), homework.getFiles());

        if (createdHomework == null) return new ResponseEntity<>("Homework could not be created", HttpStatus.BAD_REQUEST);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return new ResponseEntity<>(converter.getObjectMapper().writeValueAsString(createdHomework), HttpStatus.CREATED);
    }

}
