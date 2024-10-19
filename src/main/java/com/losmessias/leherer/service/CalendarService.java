package com.losmessias.leherer.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.stereotype.Service;

@Service
public class CalendarService {

    private static final String APPLICATION_NAME = "LosMessias Application";
    private static final String REDIRECT_URI = "https://two023-losmessias-be-7ic0.onrender.com/oauth2callback";

    // Get Calendar service with authorized credentials
    public Calendar getCalendarService(Credential credential) throws Exception {
        return new Calendar.Builder(
                (HttpTransport) GoogleNetHttpTransport.newTrustedTransport(),
                (JsonFactory) GsonFactory.getDefaultInstance(),
                (HttpRequestInitializer) credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    // Generate OAuth URL to send to frontend
    public String getAuthorizationUrl() throws Exception {
        return GoogleAuthorizeUtil.getAuthorizationUrl(REDIRECT_URI);
    }

    // Exchange authorization code for credentials
    public Credential getCredentials(String code) throws Exception {
        // Use the updated method in GoogleAuthorizeUtil to get the Credential object
        return GoogleAuthorizeUtil.getCredentials(code, REDIRECT_URI);
    }
}
