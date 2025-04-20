package com.example.recyclingAppBackend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Document(collection = "profiles")
public class StudentProfile {
    @Id private String userId;
    private List<String> viewHistory;
    private List<String> completedPaths;
    private List<String> ongoingPaths;
    private List<String> likedPaths;
    private int totalPoints;
}