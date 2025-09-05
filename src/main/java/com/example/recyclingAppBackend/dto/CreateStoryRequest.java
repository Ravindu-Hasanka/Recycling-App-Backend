package com.example.recyclingAppBackend.dto;

import lombok.Data;

@Data
public class CreateStoryRequest {
    private String title;
    private String description;
    private int numberOfStages;
    private String link;
    private String image;

    private int plastic;
    private int glass;
    private int organic;
    private int metal;
}