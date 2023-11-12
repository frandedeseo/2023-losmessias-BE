package com.losmessias.leherer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.Feedback;
import com.losmessias.leherer.domain.enumeration.ReservationStatus;
import com.losmessias.leherer.dto.FeedbackDto;
import com.losmessias.leherer.service.ClassReservationService;
import com.losmessias.leherer.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedback")
@RequiredArgsConstructor
@CrossOrigin
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final ClassReservationService classReservationService;

    @PostMapping("/giveFeedback")
    public ResponseEntity<String> giveFeedback(@RequestBody FeedbackDto feedbackDto) throws JsonProcessingException {
        ClassReservation classReservation = classReservationService.getReservationById(feedbackDto.getClassId());
        if (classReservation == null) return new ResponseEntity<>("Class not found", HttpStatus.NOT_FOUND);
        if (classReservation.getStatus() != ReservationStatus.CONCLUDED)
            return new ResponseEntity<>("Class has not concluded yet", HttpStatus.BAD_REQUEST);
        if (!classReservation.getStudent().getId().equals(feedbackDto.getStudentId()))
            return new ResponseEntity<>("Student has not taken the class", HttpStatus.BAD_REQUEST);
        if (!classReservation.getProfessor().getId().equals(feedbackDto.getProfessorId()))
            return new ResponseEntity<>("Professor has not given the class", HttpStatus.BAD_REQUEST);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(feedbackService.giveFeedback(feedbackDto)));
    }

    @GetMapping("/getAllFeedbacks")
    public ResponseEntity<String> getAllFeedbacks() throws JsonProcessingException {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(feedbackService.getAllFeedbacks()));
    }

}
