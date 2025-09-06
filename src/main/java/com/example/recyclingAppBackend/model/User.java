package com.example.recyclingAppBackend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    private String password;
    private String name;

    @Indexed(unique = true)
    private String email;

    private String role;

    private String parentId;
    private boolean isActive;
    private AccessibilityPrefs accessibilityPrefs;
    private List<String> childrenIds = new ArrayList<>();

    @Data
    public static class AccessibilityPrefs {
        private boolean audioInstructions = false;
        private boolean motionReduction = false;
        private String colorBlindMode = "none";
    }
}