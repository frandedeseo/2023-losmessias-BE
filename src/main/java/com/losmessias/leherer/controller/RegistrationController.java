package com.losmessias.leherer.controller;

import com.losmessias.leherer.dto.ForgotPasswordDto;
import com.losmessias.leherer.dto.RegistrationRequest;
import com.losmessias.leherer.service.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;


    @PostMapping(path = "api/v1/registration")
    public String register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
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
    @PostMapping(path = "api/v1/changePassword")
    public String changePassword(@RequestBody ForgotPasswordDto request) {
        return registrationService.changePassword(request);
    }
}