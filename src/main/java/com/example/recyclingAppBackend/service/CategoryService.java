package com.example.recyclingAppBackend.service;

import com.example.recyclingAppBackend.dto.CreateCategoryRequest;
import com.example.recyclingAppBackend.model.Category;
import com.example.recyclingAppBackend.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public Category createCategory(CreateCategoryRequest request) {
        Category category = new Category();
        category.setTitle(request.title());
        category.setDescription(request.description());
        return categoryRepository.save(category);
    }

    public List<Category> listCategories() {
        return categoryRepository.findAll();
    }

    public Category updateCategory(String id, CreateCategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found: " + id));
        category.setTitle(request.title());
        category.setDescription(request.description());
        return categoryRepository.save(category);
    }

    public void deleteCategory(String id) {
        categoryRepository.deleteById(id);
    }
}
