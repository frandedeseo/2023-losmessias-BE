package com.losmessias.leherer.controller_tests;

import com.losmessias.leherer.controller.EventController;
import com.losmessias.leherer.domain.AppUser;
import com.losmessias.leherer.domain.Event;
import com.losmessias.leherer.domain.enumeration.AppUserRole;
import com.losmessias.leherer.domain.enumeration.EventType;
import com.losmessias.leherer.repository.AppUserRepository;
import com.losmessias.leherer.service.EventService;
import com.losmessias.leherer.service.JwtService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(EventController.class)
public class EventControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private EventService eventService;
    @MockBean
    private JwtService jwtService;
    @MockBean
    private AppUserRepository appUserRepository;

    @Test
    @WithMockUser
    @DisplayName("Get all events gets empty list")
    void testGetAllEventsReturnsOk() throws Exception {
        mockMvc.perform(get("/api/events/all"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("Get all events gets list of events")
    void testGetAllEventsReturnsOkWithEvents() throws Exception {
        AppUser user = new AppUser("username", "password", AppUserRole.STUDENT, 1L);
        Event event1 = new Event("title", "description",
                LocalDateTime.of(2021, 1, 1, 1, 1),
                LocalDateTime.of(2021, 1, 1, 1, 2), EventType.EXAM, user);
        Event event2 = new Event("title", "description",
                LocalDateTime.of(2021, 1, 1, 1, 1),
                LocalDateTime.of(2021, 1, 1, 1, 2), EventType.EXAM, user);
        ArrayList<Event> events = new ArrayList<>();
        events.add(event1);
        events.add(event2);
        when(eventService.getAllEvents()).thenReturn(events);

        mockMvc.perform(get("/api/events/all"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Get events by user id finds none")
    void testGetEventsByUserIdReturnsOk() throws Exception {
        mockMvc.perform(get("/api/events/user/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    @DisplayName("Get events by user id")
    void testGetEventsByUserIdReturnsOkWithEvents() throws Exception {
        AppUser user = new AppUser("username", "password", AppUserRole.STUDENT, 1L);
        Event event1 = new Event("title", "description",
                LocalDateTime.of(2021, 1, 1, 1, 1),
                LocalDateTime.of(2021, 1, 1, 1, 2), EventType.EXAM, user);
        Event event2 = new Event("title", "description",
                LocalDateTime.of(2021, 1, 1, 1, 1),
                LocalDateTime.of(2021, 1, 1, 1, 2), EventType.EXAM, user);
        ArrayList<Event> events = new ArrayList<>();
        events.add(event1);
        events.add(event2);
        when(eventService.getEventsByUserId(any())).thenReturn(events);
        when(appUserRepository.findByAssociationIdAndAppUserRole(any(), any())).thenReturn(user);

        mockMvc.perform(get("/api/events/user/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("Get events by user id finds no association")
    void testGetEventsByUserIdReturnsNotFound() throws Exception {
        when(appUserRepository.findByAssociationIdAndAppUserRole(any(), any())).thenReturn(null);

        mockMvc.perform(get("/api/events/user/1"))
                .andExpect(status().isNotFound());
    }

    // TEST CREATION
}
