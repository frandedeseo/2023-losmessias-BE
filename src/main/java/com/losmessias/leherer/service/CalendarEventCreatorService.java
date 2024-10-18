//package com.losmessias.leherer.service;
//
//import com.google.api.services.calendar.Calendar;
//import com.google.api.services.calendar.model.Event;
//import com.google.api.services.calendar.model.EventDateTime;
//import com.google.api.client.util.DateTime;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CalendarEventCreatorService {
//    public String createEvent() throws Exception {
//        Calendar service = CalendarService.getCalendarService();
//
//        Event event = new Event()
//                .setSummary("Meeting with Client")
//                .setLocation("Virtual")
//                .setDescription("Discuss project requirements.");
//
//        DateTime startDateTime = new DateTime("2024-10-10T10:00:00-07:00");
//        EventDateTime start = new EventDateTime()
//                .setDateTime(startDateTime)
//                .setTimeZone("America/Los_Angeles");
//        event.setStart(start);
//
//        DateTime endDateTime = new DateTime("2024-10-10T11:00:00-07:00");
//        EventDateTime end = new EventDateTime()
//                .setDateTime(endDateTime)
//                .setTimeZone("America/Los_Angeles");
//        event.setEnd(end);
//
//        // Insert the event into the primary calendar
//        Event createdEvent = service.events().insert("primary", event).execute();
//
//        // Get the HTML link to the event
//        return createdEvent.getHtmlLink();
//    }
//}