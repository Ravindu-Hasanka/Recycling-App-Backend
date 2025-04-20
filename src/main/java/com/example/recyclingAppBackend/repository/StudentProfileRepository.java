package com.example.recyclingAppBackend.repository;

import com.example.recyclingAppBackend.model.StudentProfile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StudentProfileRepository extends MongoRepository<StudentProfile, String> {}
