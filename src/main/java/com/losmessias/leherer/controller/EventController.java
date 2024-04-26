package com.losmessias.leherer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.losmessias.leherer.domain.AppUser;
import com.losmessias.leherer.domain.Event;
import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.dto.EventDto;
import com.losmessias.leherer.repository.AppUserRepository;
import com.losmessias.leherer.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final AppUserRepository appUserRepository;

    @GetMapping("/all")
    public ResponseEntity<String> getAllEvents() throws JsonProcessingException {
        List<Event> events = eventService.getAllEvents();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        if (events.isEmpty()) return new ResponseEntity<>("No events found", HttpStatus.NOT_FOUND);
//        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(events));
        List<EventDto> eventDtoList = events.stream().map(this::convertEventToDto).toList();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(eventDtoList));
    }

    @PostMapping("/create")
    public ResponseEntity<String> createEvent(@RequestParam(required = false) String title, @RequestParam(required = false) String description, @RequestParam(required = false) Long userId, @RequestParam(required = false) LocalDateTime startTime, @RequestParam(required = false) LocalDateTime endTime, @RequestParam(required = false) String type) throws JsonProcessingException {
        if (title == null) return new ResponseEntity<>("Title must not be null", HttpStatus.BAD_REQUEST);
        if (description == null) return new ResponseEntity<>("Description must not be null", HttpStatus.BAD_REQUEST);
        if (userId == null) return new ResponseEntity<>("User id must not be null", HttpStatus.BAD_REQUEST);
        if (startTime == null) return new ResponseEntity<>("Start time must not be null", HttpStatus.BAD_REQUEST);
        if (endTime == null) return new ResponseEntity<>("End time must not be null", HttpStatus.BAD_REQUEST);
        if (startTime.isAfter(endTime))
            return new ResponseEntity<>("Start time must be before end time", HttpStatus.BAD_REQUEST);
        if (type == null) return new ResponseEntity<>("Type must not be null", HttpStatus.BAD_REQUEST);

        AppUser user = appUserRepository.findByAssociationIdAndAppUserRole(userId, AppUserRole.STUDENT);
        if (user == null) return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        Event event = eventService.createEvent(title, description, startTime, endTime, type, user);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return new ResponseEntity<>(converter.getObjectMapper().writeValueAsString(convertEventToDto(event)), HttpStatus.CREATED);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<String> getEventsByUserId(@PathVariable("id") Long id) throws JsonProcessingException {
        if (id < 0) return new ResponseEntity<>("Id must be positive", HttpStatus.BAD_REQUEST);
        AppUser user = appUserRepository.findByAssociationIdAndAppUserRole(id, AppUserRole.STUDENT);
        if (user == null) return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        List<Event> events = eventService.getEventsByUserId(user.getId());
        if (events.isEmpty()) return new ResponseEntity<>("No events found for user with id " + id, HttpStatus.NOT_FOUND);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        List<EventDto> eventDtoList = events.stream().map(this::convertEventToDto).toList();
        return ResponseEntity.ok(converter.getObjectMapper().writeValueAsString(eventDtoList));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable("id") Long id) {
        if (id < 0) return new ResponseEntity<>("Id must be positive", HttpStatus.BAD_REQUEST);
        eventService.deleteEvent(id);
        return new ResponseEntity<>("Event with id " + id + " deleted", HttpStatus.OK);
    }

    public EventDto convertEventToDto(Event event) {
        return new EventDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getStartTime(),
                event.getEndTime(),
                event.getUser().getId(),
                event.getType().toString()
        );
    }
}
