package com.losmessias.leherer.controller;

import com.losmessias.leherer.dto.*;
import com.losmessias.leherer.service.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@RequestMapping("/api")
@RestController
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping(path = "/registration")
    public String register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }

    @PostMapping(path = "/authentication")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(registrationService.authenticate(request));
    }

    @PostMapping(path = "/registration-professor")
    public String registerProfessor(@RequestBody RegistrationProfessorRequest request) {
        return registrationService.registerProfessor(request);
    }

    @GetMapping(path = "/registration/confirm")
    public AuthenticationResponse confirm(@RequestParam("token") String token) {
        return registrationService.confirmEmailToken(token);
    }

    @GetMapping(path = "/forgot_password/confirm")
    public String confirmTokenForgotPassword(@RequestParam("token") String token) {
        return registrationService.confirmChangePasswordToken(token);
    }

    @PostMapping(path = "/loadEmailForPasswordChange")
    public String sendEmailForPasswordChange(@RequestParam("email") String email) {
        return registrationService.sendEmailForPasswordChange(email);
    }

    @PostMapping(path = "/validate-email")
    public String validateEmailNotTaken(@RequestParam("email") String email) {
        return registrationService.validateEmailNotTaken(email);
    }

    @PostMapping(path = "/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody ForgotPasswordDto request) {
        String message = "{\"message\":" +
                "\"" + registrationService.changePassword(request)
                + "\"}";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}