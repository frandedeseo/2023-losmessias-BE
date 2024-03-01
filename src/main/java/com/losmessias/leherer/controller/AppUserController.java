package com.losmessias.leherer.controller;

import com.losmessias.leherer.domain.AppUser;
import com.losmessias.leherer.dto.AppUserUpdateDto;
import com.losmessias.leherer.dto.ForgotPasswordDto;
import com.losmessias.leherer.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/app-user")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;

    @PatchMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody AppUserUpdateDto appUserUpdateDto) {
        if (id == null) {
            return ResponseEntity.badRequest().body("App User ID not registered");
        } else if (appUserService.getAppUserById(id) == null) {
            return ResponseEntity.badRequest().body("AppUser not found");
        }
        AppUser appUserSaved = appUserService.update(id, appUserUpdateDto);
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        return ResponseEntity.ok(converter.getObjectMapper().valueToTree(appUserSaved).toString());
    }

    @PostMapping(path = "/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody ForgotPasswordDto request) {
        String message = "{\"message\":" +
                "\"" + appUserService.changePassword(request.getEmail(), request.getPassword())
                + "\"}";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}
