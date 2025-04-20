package com.example.recyclingAppBackend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class SubSection {
    @Id
    private String id;
    private String title;
    private String description;
    private String resourceType; // audio, video, image, document
    private String resourceUrl;
    private String notes;
    private List<String> documents;
    private Integer durationSeconds;
}