package org.example.recipebook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.recipebook.entity.Recipe;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeCreateDTO {
    private String title;
    private String description;
    private Integer categoryId;
    private Integer cuisineTypeId;
    private Integer cookingTime;
    private Recipe.DifficultyLevel difficultyLevel;
    private String imageUrl;
    private List<RecipeStepCreateDTO> steps;
    private List<RecipeIngredientCreateDTO> ingredients;
}
