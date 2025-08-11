package org.example.recipebook.dto;

import lombok.Data;
import java.util.List;

@Data
public class RecipeUpdateRequest {
    private String title;
    private String description;
    private String imageUrl;
    private Integer preparationTime;
    private Integer cookingTime;
    private String difficultyLevel;
    private Integer servings;
    private Integer categoryId;
    private Integer cuisineTypeId;
    private List<RecipeIngredientCreateDTO> ingredients;
    private List<RecipeStepCreateDTO> steps;
}
