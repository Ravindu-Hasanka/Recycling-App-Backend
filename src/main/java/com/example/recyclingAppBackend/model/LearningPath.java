package com.example.recyclingAppBackend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Document(collection = "learningPaths")
public class LearningPath {
    @Id private String id;
    private String title;
    private String description;
    private String notes;
    private String uploaderId;
    private List<String> labels;
    private List<String> ratings;
    private List<Section> sections;
    private String coverImageUrl;
    private int points;
}
