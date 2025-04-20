package com.example.recyclingAppBackend.controller;

import com.example.recyclingAppBackend.dto.CreateLearningPathRequest;
import com.example.recyclingAppBackend.model.LearningPath;
import com.example.recyclingAppBackend.service.LearningPathService;
import com.example.recyclingAppBackend.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/paths")
public class LearningPathController {
    @Autowired
    private LearningPathService pathService;

    @Autowired
    private ProfileService profileService;

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<LearningPath> create(@RequestBody CreateLearningPathRequest req,
                                               Principal principal) {
        return ResponseEntity.ok(pathService.createPath(req, principal.getName()));
    }

    @GetMapping
    public ResponseEntity<List<LearningPath>> list() {
        return ResponseEntity.ok(pathService.listPaths());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LearningPath> get(@PathVariable String id) {
        return ResponseEntity.ok(pathService.getPath(id));
    }

    @PostMapping("/{id}/view")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Void> view(@PathVariable String id, Principal principal) {
        profileService.updateHistory(principal.getName(), id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/complete")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Void> complete(@PathVariable String id, Principal principal) {
        pathService.completePath(principal.getName(), id);
        return ResponseEntity.ok().build();
    }
}
