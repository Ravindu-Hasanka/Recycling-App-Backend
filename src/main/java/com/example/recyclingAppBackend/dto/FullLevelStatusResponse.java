package com.example.recyclingAppBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class FullLevelStatusResponse {

    // Overall difficulty determined using age + day1 score
    private String overallLevelText; // "EASY", "MEDIUM", "HARD"
    private int overallLevelCode;    // 1, 2, 3

    // Age info
    private Integer age;
    private int ageGroup;            // 1, 2, 3, or 0

    // Day 1 details
    private Integer day1Score;
    private String day1Status;
    private String day1DifficultyText;
    private int day1DifficultyCode;

    // Day 2 details
    private Integer day2Score;
    private String day2Status;
    private String day2DifficultyText;
    private int day2DifficultyCode;

    // Day 3 details
    private Integer day3Score;
    private String day3Status;
    private String day3DifficultyText;
    private int day3DifficultyCode;
}
