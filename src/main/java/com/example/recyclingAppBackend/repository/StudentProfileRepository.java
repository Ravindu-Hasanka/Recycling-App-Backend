package com.example.recyclingAppBackend.repository;

import com.example.recyclingAppBackend.model.StudentProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StudentProfileRepository extends MongoRepository<StudentProfile, String> {

    Optional<StudentProfile> findByUserId(String userId);

    boolean existsByUserId(String userId);
}
