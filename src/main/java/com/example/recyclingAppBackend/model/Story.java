package com.example.recyclingAppBackend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "stories")
public class Story {
    @Id
    private String id;
    private String title;
    private String description;
    private String link;
    private String image;
    private int numberOfStages;
    private boolean isActive;

    private int plastic = 0;
    private int glass = 0;
    private int organic = 0;
    private int metal = 0;
}