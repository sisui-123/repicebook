package org.example.recipebook.controller;

import org.example.recipebook.dto.ApiResponse;
import org.example.recipebook.entity.Category;
import org.example.recipebook.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {    @Autowired
    private CategoryService categoryService;
    
    @GetMapping("/{categoryId}")
    public ApiResponse<Category> getCategoryById(@PathVariable Integer categoryId) {
        try {
            Category category = categoryService.getCategoryById(categoryId);
            return ApiResponse.success(category);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping
    public ApiResponse<List<Category>> getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            return ApiResponse.success(categories);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/type")
    public ApiResponse<List<Category>> getCategoriesByType(@RequestParam Category.CategoryType type) {
        try {
            List<Category> categories = categoryService.getCategoriesByType(type);
            return ApiResponse.success(categories);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
