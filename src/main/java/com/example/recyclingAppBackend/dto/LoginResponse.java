package com.example.recyclingAppBackend.dto;

public record LoginResponse(String token, String userId, String role) {}