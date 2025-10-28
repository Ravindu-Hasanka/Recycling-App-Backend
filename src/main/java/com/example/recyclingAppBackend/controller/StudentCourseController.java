package com.example.recyclingAppBackend.controller;

import com.example.recyclingAppBackend.dto.LevelInfoResponse;
import com.example.recyclingAppBackend.dto.FullLevelStatusResponse;
import com.example.recyclingAppBackend.dto.UpdateDayScoreRequest;
import com.example.recyclingAppBackend.dto.UpdateDayStatusRequest;
import com.example.recyclingAppBackend.service.ProfileService;
import com.example.recyclingAppBackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/student-course")
public class StudentCourseController {

    private final ProfileService profileService;

    @Autowired
    private UserService userService;

    public StudentCourseController(ProfileService profileService) {
        this.profileService = profileService;
    }

    /**
     * PUT /api/student-course/day/{day}/score
     * Body: { "score": 72 }
     */
    @PutMapping("/day/{day}/score")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Void> updateDayScore(@PathVariable int day,
                                               @Valid @RequestBody UpdateDayScoreRequest body,
                                               Principal principal) {

//        String userId = principal.getName();
        String userId = userService.getUserIdFromPrincipal(principal);
        profileService.updateDayScore(userId, day, body.getScore());
        return ResponseEntity.ok().build();
    }

    /**
     * PUT /api/student-course/day/{day}/status
     * Body: { "status": "COMPLETED" }
     */
    @PutMapping("/day/{day}/status")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Void> updateDayStatus(@PathVariable int day,
                                                @Valid @RequestBody UpdateDayStatusRequest body,
                                                Principal principal) {

//        String userId = principal.getName();
        String userId = userService.getUserIdFromPrincipal(principal);
        profileService.updateDayStatus(userId, day, body.getStatus());
        return ResponseEntity.ok().build();
    }

    /**
     * GET /api/student-course/level
     * Returns overall difficulty (based on age + day1 score),
     * plus age & ageGroup.
     */
    @GetMapping("/level")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<LevelInfoResponse> getLevelInfo(Principal principal) {

//        String userId = principal.getName();
        String userId = userService.getUserIdFromPrincipal(principal);
        LevelInfoResponse resp = profileService.getLevelInfo(userId);
        return ResponseEntity.ok(resp);
    }

    /**
     * GET /api/student-course/level/full
     *
     * Returns:
     * - overall difficulty
     * - age, ageGroup
     * - for each day (1,2,3): score, status, difficulty for that specific day
     *
     * Example response:
     * {
     *   "overallLevelText": "MEDIUM",
     *   "overallLevelCode": 2,
     *   "age": 8,
     *   "ageGroup": 3,
     *   "day1Score": 60,
     *   "day1Status": "COMPLETED",
     *   "day1DifficultyText": "MEDIUM",
     *   "day1DifficultyCode": 2,
     *   "day2Score": 20,
     *   "day2Status": "IN_PROGRESS",
     *   "day2DifficultyText": "EASY",
     *   "day2DifficultyCode": 1,
     *   "day3Score": 0,
     *   "day3Status": "NOT_START",
     *   "day3DifficultyText": "EASY",
     *   "day3DifficultyCode": 1
     * }
     */
    @GetMapping("/level/full")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<FullLevelStatusResponse> getFullLevelStatus(Principal principal) {

//        String userId = principal.getName();
        String userId = userService.getUserIdFromPrincipal(principal);
        FullLevelStatusResponse resp = profileService.getFullLevelStatus(userId);
        return ResponseEntity.ok(resp);
    }
}
