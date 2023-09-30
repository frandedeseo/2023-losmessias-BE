package com.losmessias.leherer.controller;

import com.losmessias.leherer.dto.RegistrationRequest;
import com.losmessias.leherer.service.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @GetMapping("student")
    public String student(){
        return "I am a student";
    }

    @GetMapping( "teacher")
    public String teacher(){
        return "I am a teacher";
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(path = "api/v1/registration")
    public String register(@RequestBody RegistrationRequest request) {
        return registrationService.register(request);
    }

//    @CrossOrigin(origins = "http://localhost:3000")
//    @GetMapping("/login")
//    public String login(){
//        System.out.println("Hola");
//        return "login";
//    }

    @GetMapping(path = "api/v1/registration/confirm")
    public String confirm(@RequestParam("token") String token) {
        return registrationService.confirmToken(token);
    }
}