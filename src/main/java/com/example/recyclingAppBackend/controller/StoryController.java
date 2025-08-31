package com.example.recyclingAppBackend.controller;

import com.example.recyclingAppBackend.dto.CreateStoryRequest;
import com.example.recyclingAppBackend.model.Story;
import com.example.recyclingAppBackend.service.StoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stories")
public class StoryController {

    @Autowired
    private StoryService storyService;

    @GetMapping
    public ResponseEntity<List<Story>> listStories() {
        return ResponseEntity.ok(storyService.getAllStories());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Story> create(@RequestBody CreateStoryRequest request) {
        return ResponseEntity.ok(storyService.createStory(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Story> update(@PathVariable String id, @RequestBody CreateStoryRequest request) {
        return ResponseEntity.ok(storyService.updateStory(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        storyService.deleteStory(id);
        return ResponseEntity.noContent().build();
    }
}