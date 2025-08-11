package org.example.recipebook.repository;

import org.example.recipebook.entity.RecipeIngredient;
import org.example.recipebook.entity.RecipeIngredientId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, RecipeIngredientId> {
    List<RecipeIngredient> findByRecipeRecipeId(Integer recipeId);
}
