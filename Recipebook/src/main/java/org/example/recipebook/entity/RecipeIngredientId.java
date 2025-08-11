package org.example.recipebook.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredientId implements Serializable {
    
    @Column(name = "recipe_id")
    private Integer recipeId;
    
    @Column(name = "ingredient_id")
    private Integer ingredientId;
}
