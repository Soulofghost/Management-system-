package com.library.controller;

import com.library.dto.LoginRequest;
import com.library.dto.LoginResponse;
import com.library.dto.UserDTO;
import com.library.service.AuthService;
import com.library.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse loginResponse = authService.login(loginRequest);
            if (loginResponse.getToken() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse);
            }
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse(null, null, null, null, "Login failed: " + e.getMessage()));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserDTO userDTO, @RequestParam String password) {
        try {
            UserDTO registeredUser = authService.registerUser(userDTO, password);
            return ResponseEntity.ok(new LoginResponse(null, registeredUser.getUsername(), registeredUser.getEmail(), registeredUser.getRole(), "User registered successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new LoginResponse(null, null, null, null, "Registration failed: " + e.getMessage()));
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try {
            String jwt = token.replace("Bearer ", "");
            if (jwtTokenProvider.validateToken(jwt)) {
                return ResponseEntity.ok(new LoginResponse(jwt, jwtTokenProvider.getUsernameFromToken(jwt), null, jwtTokenProvider.getRoleFromToken(jwt), "Token is valid"));
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(null, null, null, null, "Invalid token"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(null, null, null, null, "Token validation failed"));
        }
    }
}
