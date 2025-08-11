package org.example.recipebook.controller;

import org.example.recipebook.dto.ApiResponse;
import org.example.recipebook.entity.Ingredient;
import org.example.recipebook.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {

    @Autowired
    private IngredientRepository ingredientRepository;    @GetMapping("/{ingredientId}")
    public ApiResponse<Ingredient> getIngredientById(@PathVariable Integer ingredientId) {
        try {
            Ingredient ingredient = ingredientRepository.findById(ingredientId)
                    .orElseThrow(() -> new RuntimeException("食材不存在"));
            return ApiResponse.success(ingredient);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping
    public ApiResponse<List<Ingredient>> getAllIngredients() {
        try {
            List<Ingredient> ingredients = ingredientRepository.findAll();
            return ApiResponse.success(ingredients);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping("/search")
    public ApiResponse<List<Ingredient>> searchIngredients(@RequestParam String keyword) {
        try {
            List<Ingredient> ingredients = ingredientRepository.findByNameContaining(keyword);
            return ApiResponse.success(ingredients);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
