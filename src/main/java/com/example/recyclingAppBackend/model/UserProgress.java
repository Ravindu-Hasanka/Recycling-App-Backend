package com.example.recyclingAppBackend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Data
@Document(collection = "user_progress")
public class UserProgress {
    @Id
    private String id;

    @Indexed
    private String userId;

    @Indexed
    private String storyId;

    private int currentStage;
    private List<Integer> marks = new ArrayList<>();
    private ProgressStatus status;
    private LocalDateTime lastPlayed;

    public enum ProgressStatus {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED
    }
}