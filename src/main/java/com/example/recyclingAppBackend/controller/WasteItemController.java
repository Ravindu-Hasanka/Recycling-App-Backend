package com.example.recyclingAppBackend.controller;

import com.example.recyclingAppBackend.dto.CreateWasteItemRequest;
import com.example.recyclingAppBackend.model.WasteItem;
import com.example.recyclingAppBackend.service.WasteItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class WasteItemController {

    @Autowired
    private WasteItemService wasteItemService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WasteItem> create(@RequestBody CreateWasteItemRequest request) {
        return ResponseEntity.ok(wasteItemService.createItem(request));
    }

    @GetMapping
    public ResponseEntity<List<WasteItem>> getAllItems() {
        return ResponseEntity.ok(wasteItemService.getAllItems());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WasteItem> getItemById(@PathVariable String id) {
        return ResponseEntity.ok(wasteItemService.getItemById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WasteItem> update(@PathVariable String id, @RequestBody CreateWasteItemRequest request) {
        return ResponseEntity.ok(wasteItemService.updateItem(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        wasteItemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }
}