// RegisterRequest.java
package com.example.recyclingAppBackend.dto;

import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotBlank @Size(min = 3, max = 20) String username,
        @NotBlank @Size(min = 6, max = 40) String password,
        @NotBlank String name,
        @NotBlank @Email String email
) {}