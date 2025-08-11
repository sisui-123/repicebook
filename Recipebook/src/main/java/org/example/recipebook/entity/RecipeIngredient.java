package org.example.recipebook.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "recipe_ingredients")
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredient {
    
    @EmbeddedId
    private RecipeIngredientId id;
    
    @ManyToOne
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
    
    @ManyToOne
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;
    
    @Column(name = "quantity")
    private String quantity;
}
