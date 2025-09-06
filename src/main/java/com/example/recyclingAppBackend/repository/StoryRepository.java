package com.example.recyclingAppBackend.repository;

import com.example.recyclingAppBackend.model.Story;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoryRepository extends MongoRepository<Story, String> {
    Optional<Story> findByTitle(String title);
}
