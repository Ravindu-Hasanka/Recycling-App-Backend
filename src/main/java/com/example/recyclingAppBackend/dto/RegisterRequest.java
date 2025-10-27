package com.example.recyclingAppBackend.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotBlank @Size(min = 3, max = 20) String username,
        @NotBlank @Size(min = 6, max = 40) String password,
        @NotBlank String name,
        @NotBlank @Email String email,
        @NotBlank String role,
        @NotNull(message = "Age is required")
        @Min(value = 1, message = "Age must be at least 1")
        @Max(value = 120, message = "Age cannot exceed 120")
        Integer age,

        String parentId
) {}
