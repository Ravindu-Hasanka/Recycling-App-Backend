package com.example.recyclingAppBackend.service;

import com.example.recyclingAppBackend.dto.CreateWasteItemRequest;
import com.example.recyclingAppBackend.exception.ResourceNotFoundException;
import com.example.recyclingAppBackend.model.WasteItem;
import com.example.recyclingAppBackend.repository.WasteItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WasteItemService {

    @Autowired
    private WasteItemRepository wasteItemRepository;

    public WasteItem createItem(CreateWasteItemRequest request) {
        WasteItem newItem = new WasteItem(
                request.getName(),
                request.getDescription(),
                request.getCategoryId()
        );
        return wasteItemRepository.save(newItem);
    }

    public List<WasteItem> getAllItems() {
        return wasteItemRepository.findAll();
    }

    public WasteItem getItemById(String id) {
        return wasteItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("WasteItem not found with id: " + id));
    }

    public WasteItem updateItem(String id, CreateWasteItemRequest request) {
        WasteItem existingItem = getItemById(id);

        existingItem.setName(request.getName());
        existingItem.setDescription(request.getDescription());
        existingItem.setCategoryId(request.getCategoryId());

        return wasteItemRepository.save(existingItem);
    }

    public void deleteItem(String id) {
        if (!wasteItemRepository.existsById(id)) {
            throw new ResourceNotFoundException("WasteItem not found with id: " + id);
        }
        wasteItemRepository.deleteById(id);
    }
}