package com.losmessias.leherer.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.losmessias.leherer.domain.NotificationProfessor;
import com.losmessias.leherer.domain.NotificationStudent;
import com.losmessias.leherer.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
@CrossOrigin
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/student-all")
    ResponseEntity<String> getStudentNotifications(@RequestParam Long id) throws JsonProcessingException {
        List<NotificationStudent> notificationsStudent = notificationService.getStudentNotifications(id);
        if (notificationsStudent.isEmpty())
            return new ResponseEntity<>("No notification found", HttpStatus.NOT_FOUND);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return new ResponseEntity<>(converter.getObjectMapper().writeValueAsString(notificationsStudent), HttpStatus.OK);
    }
    @GetMapping("/professor-all")
    public ResponseEntity<String> getProfessorNotifications(@RequestParam Long id) throws JsonProcessingException {
        List<NotificationProfessor> notificationsProfessor = notificationService.getProfessorNotifications(id);
        if (notificationsProfessor.isEmpty())
            return new ResponseEntity<>("No notification found", HttpStatus.NOT_FOUND);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return new ResponseEntity<>(converter.getObjectMapper().writeValueAsString(notificationsProfessor), HttpStatus.OK);
    }

    @PostMapping("/open-student-notification")
    public ResponseEntity<String> setStudentNotificationToOpened(@RequestParam Long id) throws JsonProcessingException {
        if (id==null) return ResponseEntity.badRequest().body("Notification id must be provided");
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(notificationService.setStudentNotificationToOpened(id)));
    }

    @PostMapping("/open-professor-notification")
    public ResponseEntity<String> setProfessorNotificationToOpened(@RequestParam Long id) throws JsonProcessingException {
        if (id==null) return ResponseEntity.badRequest().body("Notification id must be provided");
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(notificationService.setProfessorNotificationToOpened(id)));
    }
}
