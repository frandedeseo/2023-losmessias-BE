//package com.losmessias.leherer.controller;
//
//import com.google.api.client.auth.oauth2.Credential;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import com.losmessias.leherer.service.CalendarService;
//import org.springframework.beans.factory.annotation.Autowired;
//
//@RestController
//public class OAuth2CallbackController {
//    @GetMapping("/oauth2callback")
//    public ResponseEntity<String> handleOAuthCallback(@RequestParam("code") String code) {
//        try {
//        // Exchange the authorization code for credentials
//        Credential credential = googleAuthorizeUtil.getCredentials(code);
//
//        // Store credentials for future use (if necessary)
//        // You can store the token in a database or session for the user
//
//        // Redirect to the frontend after authorization
//        return ResponseEntity.status(HttpStatus.FOUND)
//        .header("Location", "http://localhost:3000/student-landing") // Change this to your frontend URL
//        .build();
//        } catch (Exception e) {
//        e.printStackTrace();
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during Google OAuth process");
//        }
//    }
//}