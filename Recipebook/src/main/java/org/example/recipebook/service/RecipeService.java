package org.example.recipebook.service;

import org.example.recipebook.dto.RecipeCreateRequest;
import org.example.recipebook.dto.RecipeDTO;
import org.example.recipebook.dto.RecipeUpdateRequest;
import org.example.recipebook.entity.Recipe;
import java.util.List;

public interface RecipeService {
    RecipeDTO getRecipeById(Integer recipeId);
    List<RecipeDTO> getAllRecipes();
    List<RecipeDTO> getRecipesByUser(Integer userId);
    List<RecipeDTO> getRecipesByCategory(Integer categoryId);
    List<RecipeDTO> getRecipesByCuisineType(Integer cuisineTypeId);
    List<RecipeDTO> searchRecipes(String keyword);
    RecipeDTO createRecipe(RecipeCreateRequest recipeRequest);
    RecipeDTO updateRecipe(Integer recipeId, RecipeUpdateRequest recipeRequest);
    void deleteRecipe(Integer recipeId);
    RecipeDTO convertToDTO(Recipe recipe);
    List<RecipeDTO> getLatestRecipes(int limit);
}
