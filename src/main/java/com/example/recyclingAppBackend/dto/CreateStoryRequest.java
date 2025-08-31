package com.example.recyclingAppBackend.dto;

import lombok.Data;

@Data
public class CreateStoryRequest {
    private String title;
    private String description;
    private int numberOfStages;
}