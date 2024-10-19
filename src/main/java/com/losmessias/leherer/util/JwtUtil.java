package com.losmessias.leherer.util;

import com.losmessias.leherer.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class JwtUtil {

    // Utility method to extract user ID from the request
    public static ResponseEntity<Long> extractUserIdFromRequest(HttpServletRequest request, JwtService jwtService) {
        String authHeader = request.getHeader("Authorization");

        // Check if the authorization header is valid
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // Return 401 Unauthorized if invalid
        }

        // Extract the token from the header
        String token = authHeader.substring(7); // "Bearer " has 7 characters, so we skip those

        // Extract user ID from the token using JwtService
        Long userId = jwtService.extractUserId(token);

        // If user ID is not found in the token, return 400 Bad Request
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        // Return userId wrapped in a ResponseEntity
        return ResponseEntity.ok(userId);
    }
}