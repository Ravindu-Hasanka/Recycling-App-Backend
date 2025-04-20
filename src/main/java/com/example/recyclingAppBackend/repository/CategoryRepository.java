package com.example.recyclingAppBackend.repository;

import com.example.recyclingAppBackend.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {}
