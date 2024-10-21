package com.losmessias.leherer.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.Collections;
import java.util.Objects;

public class GoogleAuthorizeUtil {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static GoogleAuthorizationCodeFlow flow;

    // Initialize Google Authorization Flow
    private static GoogleAuthorizationCodeFlow getFlow() throws Exception {
        if (flow == null) {
            GoogleClientSecrets clientSecrets = getClientSecrets();  // Updated to use environment variables

            flow = new GoogleAuthorizationCodeFlow.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JSON_FACTORY,
                    clientSecrets,
                    Collections.singletonList("https://www.googleapis.com/auth/calendar.events"))
                    .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                    .setAccessType("offline")
                    .build();
        }
        return flow;
    }

    // Get the Authorization URL to redirect the user
    public static String getAuthorizationUrl(String redirectUri) throws Exception {
        GoogleAuthorizationCodeFlow flow = getFlow();
        return flow.newAuthorizationUrl().setRedirectUri(redirectUri).build();
    }

    // Exchange the authorization code for credentials
    public static Credential getCredentials(String authorizationCode, String redirectUri) throws Exception {
        // Get the authorization code flow
        GoogleAuthorizationCodeFlow flow = getFlow();

        // Exchange the authorizati on code for a token
        GoogleTokenResponse tokenResponse = flow.newTokenRequest(authorizationCode)
                .setRedirectUri(redirectUri)
                .execute();

        // Now use the token response to create the Credential object
        return flow.createAndStoreCredential(tokenResponse, "user");  // "user" can be replaced with a unique ID for each user if needed
    }

    // New method to load client secrets from environment variables
    private static GoogleClientSecrets getClientSecrets() throws IOException {
        GoogleClientSecrets clientSecrets = new GoogleClientSecrets()
                .setInstalled(new GoogleClientSecrets.Details()
                        .setClientId(System.getenv("GOOGLE_CLIENT_ID"))
                        .setClientSecret(System.getenv("GOOGLE_CLIENT_SECRET"))
                );
        return clientSecrets;
}
