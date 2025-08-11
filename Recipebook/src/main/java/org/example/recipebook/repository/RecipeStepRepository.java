package org.example.recipebook.repository;

import org.example.recipebook.entity.RecipeStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecipeStepRepository extends JpaRepository<RecipeStep, Integer> {
    List<RecipeStep> findByRecipeRecipeIdOrderByStepNumber(Integer recipeId);
}
