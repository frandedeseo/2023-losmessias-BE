package com.losmessias.leherer.service;

import com.losmessias.leherer.domain.AppUser;
import com.losmessias.leherer.domain.Event;
import com.losmessias.leherer.domain.enumeration.EventType;
import com.losmessias.leherer.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    public Event createEvent(String title, String description, LocalDateTime startTime, LocalDateTime endTime, String type, AppUser user) {
        if (title == null) throw new IllegalArgumentException("Title must not be null");
        if (description == null) throw new IllegalArgumentException("Description must not be null");
        if (startTime == null) throw new IllegalArgumentException("Start time must not be null");
        if (endTime == null) throw new IllegalArgumentException("End time must not be null");
        if(startTime.isAfter(endTime)) throw new IllegalArgumentException("Start time must be before end time");
        if (type == null) throw new IllegalArgumentException("Type must not be null");

        Event event = new Event(title, description, startTime, endTime, EventType.valueOf(type), user);
        return eventRepository.save(event);
    }

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Event> getEventsByUserId(Long id) {
        return eventRepository.findByUserId(id);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }
}
