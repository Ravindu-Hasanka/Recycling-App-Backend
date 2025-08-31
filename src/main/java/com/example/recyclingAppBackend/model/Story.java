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
    private int numberOfStages;
    private boolean isActive;
}