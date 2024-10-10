package com.losmessias.leherer.controller;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.*;
import com.losmessias.leherer.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalendarController {

    @Autowired
    private CalendarService calendarService;



    @GetMapping("/api/calendar/eventLink")
    public String createEvent() {
        try {
            // Get the Calendar service
            Calendar service = calendarService.getCalendarService();

            // Create a new event (same as before)
            Event event = new Event()
                    .setSummary("Sample Event")
                    .setLocation("Online")
                    .setDescription("This is a sample event.");

            // Set the start and end times
            DateTime startDateTime = new DateTime("2024-10-10T10:00:00-07:00");
            EventDateTime start = new EventDateTime()
                    .setDateTime(startDateTime)
                    .setTimeZone("America/Los_Angeles");
            event.setStart(start);

            DateTime endDateTime = new DateTime("2024-10-10T11:00:00-07:00");
            EventDateTime end = new EventDateTime()
                    .setDateTime(endDateTime)
                    .setTimeZone("America/Los_Angeles");
            event.setEnd(end);

            // Insert the event into the user's calendar
            Event createdEvent = service.events().insert("primary", event).execute();

            // Return the event link
            return "Event created: <a href=\"" + createdEvent.getHtmlLink() + "\">View Event</a>";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error creating event.";
        }
    }
}
