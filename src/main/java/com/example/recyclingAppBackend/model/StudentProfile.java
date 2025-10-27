package com.example.recyclingAppBackend.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "profiles")
public class StudentProfile {

    @Id
    private String id; // MongoDB document id

    // links to User.id
    private String userId;

    // NEW FIELDS
    private Integer age;        // student age (e.g. 5, 8, etc.)
    private String course;      // course assigned to the student

    // quiz progress for 3 days (each day has score + status)
    @Builder.Default
    private QuizProgress quizProgress = new QuizProgress();

    // badges earned, optional
    @Builder.Default
    private Set<String> badgeIds = new HashSet<>();

    // gamification points
    @Builder.Default
    private int points = 0;

    // optional auditing timestamps
    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    private List<String> viewHistory;
    private List<String> completedPaths;
    private List<String> ongoingPaths;
    private List<String> likedPaths;
    private int totalPoints;
}
