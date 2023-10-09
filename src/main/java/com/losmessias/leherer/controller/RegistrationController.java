package com.losmessias.leherer.controller;

import com.losmessias.leherer.dto.*;
import com.losmessias.leherer.service.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping(path = "api/v1/registration")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(registrationService.register(request));
    }
    @PostMapping(path = "api/v1/authentication")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(registrationService.authenticate(request));
    }

    @PostMapping(path = "api/v1/registration-professor")
    public String registerProfessor(@RequestBody RegistrationProfessorRequest request) {
        return registrationService.registerProfessor(request);
    }

    @GetMapping(path = "api/v1/registration/confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmEmailToken(token);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "api/forgot_password/confirm")
    public String confirmTokenForgotPassword(@RequestParam("token") String token) {
        return registrationService.confirmChangePasswordToken(token);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(path = "api/v1/loadEmailForPasswordChange")
    public String sendEmailForPasswordChange(@RequestParam("email") String email) {
        return registrationService.sendEmailForPasswordChange(email);
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(path = "api/v1/validate-email")
    public String validateEmailNotTaken(@RequestParam("email") String email) {
        return registrationService.validateEmailNotTaken(email);
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(path = "api/v1/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody ForgotPasswordDto request) {
        String message = "{\"message\":" +
                "\"" + registrationService.changePassword(request)
                + "\"}";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}