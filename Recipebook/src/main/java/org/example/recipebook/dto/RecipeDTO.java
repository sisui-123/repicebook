package org.example.recipebook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.recipebook.entity.Recipe;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDTO {
    private Integer recipeId;
    private Integer userId;
    private String username;
    private String title;
    private String description;
    private Integer categoryId;
    private String categoryName;
    private Integer cuisineTypeId;
    private String cuisineTypeName;
    private Integer cookingTime;
    private Recipe.DifficultyLevel difficultyLevel;
    private String imageUrl;
    private LocalDateTime createdAt;
    private List<RecipeStepDTO> steps;
    private List<RecipeIngredientDTO> ingredients;
}
