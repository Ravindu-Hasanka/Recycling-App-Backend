package com.example.recyclingAppBackend.repository;

import com.example.recyclingAppBackend.model.WasteItem;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WasteItemRepository extends MongoRepository<WasteItem, String> {
}