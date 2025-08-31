package com.example.recyclingAppBackend.dto;

import lombok.Data;

@Data
public class CreateBadgeRequest {
    private String name;
    private String description;
    private String iconUrl;
}