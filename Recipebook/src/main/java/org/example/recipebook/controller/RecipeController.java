package org.example.recipebook.controller;

import org.example.recipebook.dto.ApiResponse;
import org.example.recipebook.dto.RecipeCreateRequest;
import org.example.recipebook.dto.RecipeDTO;
import org.example.recipebook.dto.RecipeUpdateRequest;
import org.example.recipebook.service.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @GetMapping("/{recipeId}")
    public ApiResponse<RecipeDTO> getRecipeById(@PathVariable Integer recipeId) {
        try {
            RecipeDTO recipeDTO = recipeService.getRecipeById(recipeId);
            return ApiResponse.success(recipeDTO);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping
    public ApiResponse<List<RecipeDTO>> getAllRecipes() {
        try {
            List<RecipeDTO> recipes = recipeService.getAllRecipes();
            return ApiResponse.success(recipes);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public ApiResponse<List<RecipeDTO>> getRecipesByUser(@PathVariable Integer userId) {
        try {
            List<RecipeDTO> recipes = recipeService.getRecipesByUser(userId);
            return ApiResponse.success(recipes);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/category/{categoryId}")
    public ApiResponse<List<RecipeDTO>> getRecipesByCategory(@PathVariable Integer categoryId) {
        try {
            List<RecipeDTO> recipes = recipeService.getRecipesByCategory(categoryId);
            return ApiResponse.success(recipes);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/cuisine-type/{cuisineTypeId}")
    public ApiResponse<List<RecipeDTO>> getRecipesByCuisineType(@PathVariable Integer cuisineTypeId) {
        try {
            List<RecipeDTO> recipes = recipeService.getRecipesByCuisineType(cuisineTypeId);
            return ApiResponse.success(recipes);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/search")
    public ApiResponse<List<RecipeDTO>> searchRecipes(@RequestParam String keyword) {
        try {
            List<RecipeDTO> recipes = recipeService.searchRecipes(keyword);
            return ApiResponse.success(recipes);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @GetMapping("/latest")
    public ApiResponse<List<RecipeDTO>> getLatestRecipes(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<RecipeDTO> recipes = recipeService.getLatestRecipes(limit);
            return ApiResponse.success(recipes);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }    @PostMapping
    public ApiResponse<RecipeDTO> createRecipe(@RequestBody RecipeCreateRequest recipeRequest) {
        try {
            RecipeDTO recipeDTO = recipeService.createRecipe(recipeRequest);
            return ApiResponse.success("创建菜谱成功", recipeDTO);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }    @PutMapping("/{recipeId}")
    public ApiResponse<RecipeDTO> updateRecipe(
            @PathVariable Integer recipeId,
            @RequestBody RecipeUpdateRequest recipeRequest) {
        try {
            RecipeDTO recipeDTO = recipeService.updateRecipe(recipeId, recipeRequest);
            return ApiResponse.success("更新菜谱成功", recipeDTO);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }

    @DeleteMapping("/{recipeId}")
    public ApiResponse<Void> deleteRecipe(@PathVariable Integer recipeId) {
        try {
            recipeService.deleteRecipe(recipeId);
            return ApiResponse.success("删除菜谱成功", null);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
