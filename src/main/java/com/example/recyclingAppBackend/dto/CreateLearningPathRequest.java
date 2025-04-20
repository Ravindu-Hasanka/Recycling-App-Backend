package com.example.recyclingAppBackend.dto;

import com.example.recyclingAppBackend.model.Section;

import java.util.List;

public record CreateLearningPathRequest(
        String title, String description, String uploaderId,
        List<String> labels, int points,
        List<Section> sections
) {}