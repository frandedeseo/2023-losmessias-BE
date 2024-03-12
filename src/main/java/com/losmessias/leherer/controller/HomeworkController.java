package com.losmessias.leherer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.losmessias.leherer.domain.File;
import com.losmessias.leherer.domain.Homework;
import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.dto.HomeworkDto;
import com.losmessias.leherer.dto.HomeworkResponseDto;
import com.losmessias.leherer.dto.UploadInformationDto;
import com.losmessias.leherer.repository.CommentRepository;
import com.losmessias.leherer.repository.HomeworkRepository;
import com.losmessias.leherer.service.FileService;
import com.losmessias.leherer.service.HomeworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/homework")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class HomeworkController {

    private final HomeworkService homeworkService;
    private final HomeworkRepository homeworkRepository;
    private final CommentRepository commentRepository;
    private final FileService fileService;

    @GetMapping("/all")
    public ResponseEntity<String> getAllHomeworks() throws JsonProcessingException {
        List<Homework> homeworks = homeworkService.getAllHomeworks();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        if (homeworks.isEmpty()) return new ResponseEntity<>("No homeworks found", HttpStatus.NOT_FOUND);
        List<HomeworkDto> homeworkDtoList = homeworks.stream().map(this::convertHomeworkToDto).toList();

        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(homeworkDtoList));
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getHomeworkById(@PathVariable("id") Long id) throws JsonProcessingException {
        if (id < 0) return new ResponseEntity<>("Id must be positive", HttpStatus.BAD_REQUEST);
        Homework homework = homeworkService.getHomeworkById(id);
        if (homework == null) return new ResponseEntity<>("No homework found with id " + id, HttpStatus.NOT_FOUND);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(convertHomeworkToDto(homework)));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createHomework(@RequestParam(required = false) String assignment, @RequestParam(required = false) LocalDateTime deadline, @RequestParam(required = false) Long classReservationId, @RequestParam(required = false) Long professorId, @RequestParam(required = false) MultipartFile file) throws JsonProcessingException {
        if (assignment == null && file == null)
            return new ResponseEntity<>("Assignment and files can't be both null. Please upload either an assignment or a file.", HttpStatus.BAD_REQUEST);
        if (classReservationId == null)
            return new ResponseEntity<>("Class reservation must not be null", HttpStatus.BAD_REQUEST);
        if (professorId == null)
            return new ResponseEntity<>("Professor id must not be null.", HttpStatus.BAD_REQUEST);
        if (deadline == null)
            return new ResponseEntity<>("Deadline must not be null.", HttpStatus.BAD_REQUEST);
        if (deadline.isBefore(LocalDateTime.now()))
            return new ResponseEntity<>("Deadline must be in the future.", HttpStatus.BAD_REQUEST);
        Homework createdHomework = homeworkService.createHomework(
                deadline,
                assignment,
                classReservationId,
                professorId,
                file
        );
        if (createdHomework == null)
            return new ResponseEntity<>("Homework could not be created", HttpStatus.BAD_REQUEST);

        UploadInformationDto info = new UploadInformationDto();
        if (file != null) {
            info.setIdFile(createdHomework.getFiles().get(0).getId());
            info.setClassReservation(classReservationId);
            info.setAssociatedId(professorId);
            info.setRole(AppUserRole.PROFESSOR);
            info.setHomeworkId(createdHomework.getId());
            info.setUploadedDateTime(LocalDateTime.now());
            fileService.setUploadInformation(info);
        }
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        HomeworkDto homeworkDto = convertHomeworkToDto(createdHomework);
        return new ResponseEntity<>(converter.getObjectMapper().writeValueAsString(homeworkDto), HttpStatus.CREATED);
    }

    @PatchMapping("/respond/{id}")
    public ResponseEntity<String> respondHomework(@PathVariable("id") Long id, @RequestBody HomeworkResponseDto homeworkResponseDto) throws JsonProcessingException {
        if (id < 0) return new ResponseEntity<>("Id must be positive", HttpStatus.BAD_REQUEST);
        if (homeworkResponseDto.getResponse() == null)
            return new ResponseEntity<>("Response must not be null", HttpStatus.BAD_REQUEST);
        if (homeworkResponseDto.getAssociatedId() == null)
            return new ResponseEntity<>("Associated id must not be null", HttpStatus.BAD_REQUEST);
        if (homeworkService.verifyIfResponded(id))
            return new ResponseEntity<>("Homework already responded", HttpStatus.BAD_REQUEST);

        Homework homework = homeworkService.getHomeworkById(id);
        if (homework == null)
            return new ResponseEntity<>("No homework found with id " + id, HttpStatus.NOT_FOUND);
        if (homework.getClassReservation().getStudent().getId().equals(homeworkResponseDto.getAssociatedId()))
            return new ResponseEntity<>("Student id does not match", HttpStatus.BAD_REQUEST);
        // Homework has the responsibility and capability of handling the response by itself:
        homework.respondWith(homeworkResponseDto, commentRepository);
        Homework savedHomework = homeworkRepository.save(homework);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return new ResponseEntity<>(converter.getObjectMapper().writeValueAsString(convertHomeworkToDto(savedHomework)), HttpStatus.OK);
    }

    public HomeworkDto convertHomeworkToDto(Homework homework) {
        List<String> fileNames = new ArrayList<>();
        if (homework.getFiles() != null) {
            for (File file : homework.getFiles()) {
                fileNames.add(file.getFileName());
            }
        }
        return new HomeworkDto(
                homework.getId(),
                homework.getClassReservation().getId(),
                homework.getAssignment().getComment(),
                homework.getResponse() != null ? homework.getResponse().getComment() : null,
                homework.getStatus().toString(),
                homework.getDeadline().toString(),
                homework.getClassReservation().getProfessor().getId(),
                homework.getClassReservation().getStudent().getId(),
                fileNames);
    }

//    @DeleteMapping("/delete/{id}")
//    public ResponseEntity<String> deleteHomework(@PathVariable("id") Long id) throws JsonProcessingException {
//        if (id < 0) return new ResponseEntity<>("ID must be positive", HttpStatus.BAD_REQUEST);
//        Homework homeworkToDelete = homeworkService.getHomeworkById(id);
//        if (homework == null) return new ResponseEntity<>("No homework found with id " + id, HttpStatus.NOT_FOUND);
//        homeworkService.deleteHomework(id);
//        return new ResponseEntity<>("Homework deleted", HttpStatus.OK);
//    }

}
