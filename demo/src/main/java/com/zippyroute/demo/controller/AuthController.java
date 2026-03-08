package com.zippyroute.demo.controller;

import com.zippyroute.demo.dto.AuthResponse;
import com.zippyroute.demo.dto.LoginRequest;
import com.zippyroute.demo.dto.RegisterRequest;
import com.zippyroute.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        try {
            if (request == null || request.getEmail() == null || request.getPassword() == null) {
                AuthResponse response = new AuthResponse(null, null, null, null, "Missing required fields");
                return ResponseEntity.badRequest().body(response);
            }
            AuthResponse response = authService.register(request);
            if (response.getToken() != null) {
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            AuthResponse response = new AuthResponse(null, null, null, null, "Registration failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            if (request == null || request.getEmail() == null || request.getPassword() == null) {
                AuthResponse response = new AuthResponse(null, null, null, null, "Missing email or password");
                return ResponseEntity.badRequest().body(response);
            }
            AuthResponse response = authService.login(request);
            if (response.getToken() != null) {
                return ResponseEntity.ok(response);
            }
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            AuthResponse response = new AuthResponse(null, null, null, null, "Login failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }    }
}