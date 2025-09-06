package com.example.recyclingAppBackend.controller;

import com.example.recyclingAppBackend.dto.LoginRequest;
import com.example.recyclingAppBackend.dto.RegisterRequest;
import com.example.recyclingAppBackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        return userService.registerUser(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        return userService.authenticateUser(request);
    }

    @PostMapping("/parent/add-child")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<?> addChild(@Valid @RequestBody RegisterRequest request, Principal principal) {
        String parentId = userService.getUserIdFromPrincipal(principal);
        return userService.addChild(request, parentId);
    }

    @GetMapping("/parent/children")
    @PreAuthorize("hasRole('PARENT')")
    public ResponseEntity<?> getChildren(@RequestParam String parentId) {
        return userService.getChildren(parentId);
    }
}