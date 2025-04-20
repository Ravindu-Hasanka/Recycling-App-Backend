package com.example.recyclingAppBackend.dto;

import java.util.List;

public record CreateMaterialRequest(
        String title, String description, String notes,
        String uploaderId, List<String> labels,
        String coverImageUrl, int points
) {}
