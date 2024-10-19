package com.losmessias.leherer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.losmessias.leherer.domain.ClassReservation;
import com.losmessias.leherer.domain.Feedback;
import com.losmessias.leherer.domain.Professor;
import com.losmessias.leherer.domain.Student;
import com.losmessias.leherer.domain.enumeration.ReservationStatus;
import com.losmessias.leherer.dto.FeedbackDto;
import com.losmessias.leherer.service.ClassReservationService;
import com.losmessias.leherer.service.FeedbackService;
import com.losmessias.leherer.service.JwtService;
import com.losmessias.leherer.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
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
    private final JwtService jwtService;

    @PostMapping("/giveFeedback")
    public ResponseEntity<String> giveFeedback(HttpServletRequest request, @RequestBody FeedbackDto feedbackDto) throws JsonProcessingException, InstantiationException {
        ResponseEntity<Long> userIdResponse = JwtUtil.extractUserIdFromRequest(request, jwtService);
        if (!userIdResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(userIdResponse.getStatusCode()).body("Invalid token or user ID not found");
        }
        Long userId = userIdResponse.getBody();
        if (userId!=feedbackDto.getSenderId()){
            return new ResponseEntity<>("This user has no permission to give this feedback", HttpStatus.BAD_REQUEST);
        }

        ClassReservation classReservation = classReservationService.getReservationById(feedbackDto.getClassId());
        if (classReservation == null) return new ResponseEntity<>("Class not found", HttpStatus.NOT_FOUND);
        if (classReservation.getStatus() != ReservationStatus.CONCLUDED)
            return new ResponseEntity<>("Class has not concluded yet", HttpStatus.BAD_REQUEST);
        Long studentId = classReservation.getStudent().getId();
        Long professorId = classReservation.getProfessor().getId();
        if (studentId == feedbackDto.getSenderId()) {
            if (!classReservation.getProfessor().getId().equals(feedbackDto.getReceiverId()))
                return new ResponseEntity<>("Professor has not given the class", HttpStatus.BAD_REQUEST);
        }else if (professorId == feedbackDto.getSenderId() ){
            if (!classReservation.getStudent().getId().equals(feedbackDto.getReceiverId()))
                return new ResponseEntity<>("Student has not taken the class", HttpStatus.BAD_REQUEST);
        }else {
            return new ResponseEntity<>("There was an error on the feedback", HttpStatus.BAD_REQUEST);
        }
        feedbackDto.setMaterial(feedbackDto.getMaterial() != null ? feedbackDto.getMaterial() : false);
        feedbackDto.setPunctuality( feedbackDto.getPunctuality() != null ? feedbackDto.getPunctuality() : false);
        feedbackDto.setPolite(feedbackDto.getPolite() != null ? feedbackDto.getPolite() : false);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(feedbackService.giveFeedback(feedbackDto)));
    }

    @GetMapping("/getAllFeedbacks")
    public ResponseEntity<String> getAllFeedbacks() throws JsonProcessingException {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(feedbackService.getAllFeedbacks()));
    }

}
