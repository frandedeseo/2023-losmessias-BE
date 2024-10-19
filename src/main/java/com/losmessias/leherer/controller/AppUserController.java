package com.losmessias.leherer.controller;

import com.losmessias.leherer.domain.AppUser;
import com.losmessias.leherer.dto.AppUserUpdateDto;
import com.losmessias.leherer.dto.ForgotPasswordDto;
import com.losmessias.leherer.service.AppUserService;
import com.losmessias.leherer.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.losmessias.leherer.util.JwtUtil;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/app-user")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;
    private final JwtService jwtService;

    @PatchMapping("/update")
    public ResponseEntity<String> update(HttpServletRequest request, @RequestBody AppUserUpdateDto appUserUpdateDto) {
        ResponseEntity<Long> userIdResponse = JwtUtil.extractUserIdFromRequest(request, jwtService);
        if (!userIdResponse.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.status(userIdResponse.getStatusCode()).body("Invalid token or user ID not found");
        }
        Long userId = userIdResponse.getBody();

        if (userId == null) {
            return ResponseEntity.badRequest().body("App User ID not found in token");
        } else if (appUserService.getAppUserById(userId) == null) {
            return ResponseEntity.badRequest().body("AppUser not found");
        }

        AppUser appUserSaved = appUserService.update(userId, appUserUpdateDto);
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
