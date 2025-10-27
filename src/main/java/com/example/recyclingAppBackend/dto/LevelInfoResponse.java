package com.example.recyclingAppBackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LevelInfoResponse {

    // "EASY", "MEDIUM", "HARD"
    private String levelText;

    // EASY=1, MEDIUM=2, HARD=3
    private int levelCode;

    // actual age
    private Integer age;

    // age group bucket:
    // 1-3  => 1
    // 3-7  => 2
    // 7-10 => 3
    // otherwise => 0
    private int ageGroup;
}
