package com.example.recyclingAppBackend.controller;

import com.example.recyclingAppBackend.dto.UpdateProgressRequest;
import com.example.recyclingAppBackend.model.UserProgress;
import com.example.recyclingAppBackend.service.UserProgressService;
import com.example.recyclingAppBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/progress")
public class UserProgressController {

    @Autowired
    private UserProgressService userProgressService;
    @Autowired
    private UserService userService;

    @PostMapping("/start/{storyId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<UserProgress> startStory(@PathVariable String storyId, Principal principal) {
        String userId = userService.getUserIdFromPrincipal(principal);
        return ResponseEntity.ok(userProgressService.startStory(userId, storyId));
    }

    @PostMapping("/update")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<UserProgress> updateProgress(@RequestBody UpdateProgressRequest request, Principal principal) {
        String userId = userService.getUserIdFromPrincipal(principal);
        return ResponseEntity.ok(userProgressService.updateProgress(userId, request));
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<UserProgress>> getMyProgress(Principal principal) {
        String userId = userService.getUserIdFromPrincipal(principal);
        return ResponseEntity.ok(userProgressService.getProgressForUser(userId));
    }
}