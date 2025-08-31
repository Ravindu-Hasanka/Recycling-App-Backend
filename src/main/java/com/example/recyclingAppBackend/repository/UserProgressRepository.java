package com.example.recyclingAppBackend.repository;

import com.example.recyclingAppBackend.model.UserProgress;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProgressRepository extends MongoRepository<UserProgress, String> {
    List<UserProgress> findByUserId(String userId);

    Optional<UserProgress> findByUserIdAndStoryId(String userId, String storyId);
}