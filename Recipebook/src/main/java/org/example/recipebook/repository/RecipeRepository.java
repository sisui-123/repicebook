package org.example.recipebook.repository;

import org.example.recipebook.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Integer> {
    List<Recipe> findByUserUserId(Integer userId);
    List<Recipe> findByCategoryId(Integer categoryId);
    List<Recipe> findByCuisineTypeId(Integer cuisineTypeId);
    
    @Query("SELECT r FROM Recipe r ORDER BY r.createdAt DESC")
    List<Recipe> findLatestRecipes();
    
    List<Recipe> findByTitleContaining(String keyword);
}
