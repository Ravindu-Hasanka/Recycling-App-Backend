package com.example.recyclingAppBackend.dto;

import java.util.List;

public record UpdateProfileRequest(
        String userId,
        List<String> completedPaths,
        List<String> ongoingPaths
){}