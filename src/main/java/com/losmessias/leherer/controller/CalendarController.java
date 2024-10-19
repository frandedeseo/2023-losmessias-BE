package com.losmessias.leherer.controller;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.losmessias.leherer.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    @GetMapping("/authorize")
    public ResponseEntity<String> authorize() {
        try {
            // Generate the authorization URL and send it to the frontend
            String authorizationUrl = calendarService.getAuthorizationUrl();
            return ResponseEntity.ok(authorizationUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating authorization URL");
        }
    }

    @GetMapping("/oauth2callback")
    public ResponseEntity<String> handleOAuthCallback(@RequestParam("code") String code) {
        try {
            // Exchange the authorization code for OAuth credentials
            Credential credential = calendarService.getCredentials(code);

            // Redirect the user to the frontend after successful authorization with the token in the URL
            String accessToken = credential.getAccessToken();  // Or use refresh token if needed
            String redirectUrl = "https://2023-losmessias.vercel.app/student-landing?token=" + accessToken;

            return ResponseEntity.status(HttpStatus.FOUND)
                    .header("Location", redirectUrl)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Error exchanging authorization code.");
        }
    }
}
