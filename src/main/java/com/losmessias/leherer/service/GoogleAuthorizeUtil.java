package com.losmessias.leherer.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import java.io.InputStreamReader;
import java.util.Collections;

public class GoogleAuthorizeUtil {

    private static final String CLIENT_SECRET_JSON = "/client_secret.json";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance(); // Updated to GsonFactory
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final int LOCAL_RECEIVER_PORT = 8888; // Use any available port

    public static Credential authorize() throws Exception {
        // Load client secrets
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JSON_FACTORY,
                new InputStreamReader(GoogleAuthorizeUtil.class.getResourceAsStream(CLIENT_SECRET_JSON))
        );

        // Build flow and trigger user authorization request
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                clientSecrets,
                Collections.singletonList("https://www.googleapis.com/auth/calendar.events"))
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();

        // Set up the local server receiver
        LocalServerReceiver receiver = new LocalServerReceiver.Builder()
                .setPort(LOCAL_RECEIVER_PORT)
                .build();

        // Authorize and get credentials
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }
}
