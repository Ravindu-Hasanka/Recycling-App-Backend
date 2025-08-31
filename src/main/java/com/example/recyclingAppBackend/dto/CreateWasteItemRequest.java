package com.example.recyclingAppBackend.dto;

import lombok.Data;

@Data
public class CreateWasteItemRequest {
    private String name;
    private String description;
    private String categoryId;
}