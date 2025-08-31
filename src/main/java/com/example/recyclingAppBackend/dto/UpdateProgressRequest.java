package com.example.recyclingAppBackend.dto;

import lombok.Data;

@Data
public class UpdateProgressRequest {
    private String storyId;
    private int score;
}