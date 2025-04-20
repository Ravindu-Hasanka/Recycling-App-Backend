package com.example.recyclingAppBackend.controller;

import com.example.recyclingAppBackend.dto.CreateCategoryRequest;
import com.example.recyclingAppBackend.model.Category;
import com.example.recyclingAppBackend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Category> create(@RequestBody CreateCategoryRequest req) {
        return ResponseEntity.ok(categoryService.createCategory(req));
    }

    @GetMapping("/view")
    public ResponseEntity<List<Category>> list() {
        return ResponseEntity.ok(categoryService.listCategories());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Category> update(@PathVariable String id, @RequestBody CreateCategoryRequest req) {
        return ResponseEntity.ok(categoryService.updateCategory(id, req));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}