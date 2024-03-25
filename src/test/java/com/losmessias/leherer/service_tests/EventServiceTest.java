package com.losmessias.leherer.service_tests;

import com.losmessias.leherer.domain.Event;
import com.losmessias.leherer.domain.enumeration.EventType;
import com.losmessias.leherer.repository.EventRepository;
import com.losmessias.leherer.service.EventService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    @DisplayName("Get all events")
    void testGetAllEvents() {
        List<Event> events = new ArrayList<>();
        events.add(new Event("title", "description", null, null, null, null));
        events.add(new Event("title", "description", null, null, null, null));
        when(eventRepository.findAll()).thenReturn(events);

        assertEquals(events, eventService.getAllEvents());
    }

    @Test
    @DisplayName("Get all events empty")
    void testGetAllEventsEmpty() {
        List<Event> events = new ArrayList<>();
        when(eventRepository.findAll()).thenReturn(events);
        assertEquals(events, eventService.getAllEvents());
    }

    @Test
    @DisplayName("Get events by user id")
    void testGetEventsByUserId() {
        List<Event> events = new ArrayList<>();
        events.add(new Event("title", "description", null, null, null, null));
        events.add(new Event("title", "description", null, null, null, null));
        when(eventRepository.findByUserId(1L)).thenReturn(events);

        assertEquals(events, eventService.getEventsByUserId(1L));
    }

    @Test
    @DisplayName("Get events by user id empty")
    void testGetEventsByUserIdEmpty() {
        List<Event> events = new ArrayList<>();
        when(eventRepository.findByUserId(1L)).thenReturn(events);
        assertEquals(events, eventService.getEventsByUserId(1L));
    }

    @Test
    @DisplayName("Create event")
    public void testCreateEvent() {
        Event eventExpected = new Event("title", "description", LocalDateTime.of(2024, 7, 1, 0, 0), LocalDateTime.of(2024, 7, 1, 0, 10), EventType.EXAM, null);

        when(eventRepository.save(any())).thenReturn(eventExpected);
        Event eventCreated = eventService.createEvent("title", "description", LocalDateTime.of(2024, 7, 1, 0, 0), LocalDateTime.of(2024, 7, 1, 0, 10), "EXAM", null);

        assertEquals(eventExpected, eventCreated);
    }

    @Test
    @DisplayName("Create event with null title")
    public void testCreateEventNullTitle() {
        assertThrows(IllegalArgumentException.class, () -> eventService.createEvent(null, "description", LocalDateTime.of(2024, 7, 1, 0, 0), LocalDateTime.of(2024, 7, 1, 0, 10), "EXAM", null), "Title must not be null");
    }

    @Test
    @DisplayName("Create event with null description")
    public void testCreateEventNullDescription() {
        assertThrows(IllegalArgumentException.class, () -> eventService.createEvent("title", null, LocalDateTime.of(2024, 7, 1, 0, 0), LocalDateTime.of(2024, 7, 1, 0, 10), "EXAM", null), "Description must not be null");
    }

    @Test
    @DisplayName("Create event with null start time")
    public void testCreateEventNullStartTime() {
        assertThrows(IllegalArgumentException.class, () -> eventService.createEvent("title", "description", null, LocalDateTime.of(2024, 7, 1, 0, 10), "EXAM", null), "Start time must not be null");
    }

    @Test
    @DisplayName("Create event with null end time")
    public void testCreateEventNullEndTime() {
        assertThrows(IllegalArgumentException.class, () -> eventService.createEvent("title", "description", LocalDateTime.of(2024, 7, 1, 0, 0), null, "EXAM", null), "End time must not be null");
    }

    @Test
    @DisplayName("Create event with start time after end time")
    public void testCreateEventStartTimeAfterEndTime() {
        assertThrows(IllegalArgumentException.class, () -> eventService.createEvent("title", "description", LocalDateTime.of(2024, 7, 1, 0, 10), LocalDateTime.of(2024, 7, 1, 0, 0), "EXAM", null), "Start time must be before end time");
    }

    @Test
    @DisplayName("Create event with null type")
    public void testCreateEventNullType() {
        assertThrows(IllegalArgumentException.class, () -> eventService.createEvent("title", "description", LocalDateTime.of(2024, 7, 1, 0, 0), LocalDateTime.of(2024, 7, 1, 0, 10), null, null), "Type must not be null");
    }
}
