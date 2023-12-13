package com.losmessias.leherer.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.losmessias.leherer.domain.Notification;
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

    @GetMapping("/all")
    ResponseEntity<String> getNotifications(@RequestParam Long id) throws JsonProcessingException {
        List<Notification> notificationsStudent = notificationService.getNotifications(id);
        if (notificationsStudent.isEmpty())
            return new ResponseEntity<>("No notification found", HttpStatus.NOT_FOUND);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return new ResponseEntity<>(converter.getObjectMapper().writeValueAsString(notificationsStudent), HttpStatus.OK);
    }

    @PostMapping("/open-notification")
    public ResponseEntity<String> setNotificationToOpened(@RequestParam Long id) throws JsonProcessingException {
        if (id==null) return ResponseEntity.badRequest().body("Notification id must be provided");
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(notificationService.setNotificationToOpened(id)));
    }

}
