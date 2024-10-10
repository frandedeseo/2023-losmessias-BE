package com.losmessias.leherer.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.calendar.Calendar;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory; // Use GsonFactory
import org.springframework.stereotype.Service;

@Service
public class CalendarService {

    private static final String APPLICATION_NAME = "LosMessias Application";

    public Calendar getCalendarService() throws Exception {
        // Authorize and get credentials
        Credential credential = GoogleAuthorizeUtil.authorize();

        // Build and return the Calendar service
        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}
