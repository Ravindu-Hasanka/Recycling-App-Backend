package com.example.recyclingAppBackend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "waste_items")
@Data
public class WasteItem {
    @Id
    private String id;
    private String name;
    private String description;
    private String categoryId;

    public WasteItem() {
    }

    public WasteItem(String name, String description, String categoryId) {
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
    }
}