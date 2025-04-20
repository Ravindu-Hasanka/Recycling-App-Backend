package com.example.recyclingAppBackend.repository;

import com.example.recyclingAppBackend.model.LearningPath;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LearningPathRepository extends MongoRepository<LearningPath, String> {}
