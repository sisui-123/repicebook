package org.example.recipebook.service.impl;

import org.example.recipebook.dto.*;
import org.example.recipebook.entity.*;
import org.example.recipebook.repository.*;
import org.example.recipebook.service.RecipeService;
import org.example.recipebook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeServiceImpl implements RecipeService {

    @Autowired
    private RecipeRepository recipeRepository;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private CuisineTypeRepository cuisineTypeRepository;
    
    @Autowired
    private IngredientRepository ingredientRepository;
    
    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;
    
    @Autowired
    private RecipeStepRepository recipeStepRepository;    @Override
    public RecipeDTO getRecipeById(Integer recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("菜谱不存在"));
        
        return convertToDTO(recipe);
    }

    @Override
    public List<RecipeDTO> getAllRecipes() {
        return recipeRepository.findAll().stream()
                .map(this::convertToBasicDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeDTO> getRecipesByUser(Integer userId) {
        return recipeRepository.findByUserUserId(userId).stream()
                .map(this::convertToBasicDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeDTO> getRecipesByCategory(Integer categoryId) {
        return recipeRepository.findByCategoryId(categoryId).stream()
                .map(this::convertToBasicDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeDTO> getRecipesByCuisineType(Integer cuisineTypeId) {
        return recipeRepository.findByCuisineTypeId(cuisineTypeId).stream()
                .map(this::convertToBasicDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RecipeDTO> searchRecipes(String keyword) {
        return recipeRepository.findByTitleContaining(keyword).stream()
                .map(this::convertToBasicDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RecipeDTO createRecipe(RecipeCreateRequest recipeRequest) {
        User currentUser = userService.getCurrentUser();
        
        Recipe recipe = new Recipe();
        recipe.setUser(currentUser);
        recipe.setTitle(recipeRequest.getTitle());
        recipe.setDescription(recipeRequest.getDescription());
        recipe.setCategoryId(recipeRequest.getCategoryId());
        recipe.setCuisineTypeId(recipeRequest.getCuisineTypeId());
        recipe.setCookingTime(recipeRequest.getCookingTime());
        recipe.setDifficultyLevel(Recipe.DifficultyLevel.valueOf(recipeRequest.getDifficultyLevel()));
        recipe.setImageUrl(recipeRequest.getImageUrl());
        recipe.setCreatedAt(LocalDateTime.now());
        
        Recipe savedRecipe = recipeRepository.save(recipe);
        
        // 处理菜谱步骤
        List<RecipeStepCreateDTO> steps = recipeRequest.getSteps();
        if (steps != null) {
            for (RecipeStepCreateDTO stepDTO : steps) {
                RecipeStep step = new RecipeStep();
                step.setRecipe(savedRecipe);
                step.setStepNumber(stepDTO.getStepNumber());
                step.setDescription(stepDTO.getDescription());
                step.setImageUrl(stepDTO.getImageUrl());
                recipeStepRepository.save(step);
            }
        }
        
        // 处理食材
        List<RecipeIngredientCreateDTO> ingredients = recipeRequest.getIngredients();
        if (ingredients != null) {
            for (RecipeIngredientCreateDTO ingredientDTO : ingredients) {
                Integer ingredientId = ingredientDTO.getIngredientId();
                String quantity = ingredientDTO.getQuantity();
                
                Ingredient ingredient = ingredientRepository.findById(ingredientId)
                        .orElseThrow(() -> new RuntimeException("食材不存在"));
                
                RecipeIngredient recipeIngredient = new RecipeIngredient();
                RecipeIngredientId id = new RecipeIngredientId(savedRecipe.getRecipeId(), ingredient.getIngredientId());
                recipeIngredient.setId(id);
                recipeIngredient.setRecipe(savedRecipe);
                recipeIngredient.setIngredient(ingredient);
                recipeIngredient.setQuantity(quantity);
                
                recipeIngredientRepository.save(recipeIngredient);
            }
        }
        
        return getRecipeById(savedRecipe.getRecipeId());
    }

    @Override
    @Transactional
    public RecipeDTO updateRecipe(Integer recipeId, RecipeUpdateRequest recipeRequest) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("菜谱不存在"));
        
        User currentUser = userService.getCurrentUser();
        if (!recipe.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new RuntimeException("只能修改自己的菜谱");
        }
        
        recipe.setTitle(recipeRequest.getTitle());
        recipe.setDescription(recipeRequest.getDescription());
        recipe.setCategoryId(recipeRequest.getCategoryId());
        recipe.setCuisineTypeId(recipeRequest.getCuisineTypeId());
        recipe.setCookingTime(recipeRequest.getCookingTime());
        recipe.setDifficultyLevel(Recipe.DifficultyLevel.valueOf(recipeRequest.getDifficultyLevel()));
        
        String imageUrl = recipeRequest.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            recipe.setImageUrl(imageUrl);
        }
        
        Recipe updatedRecipe = recipeRepository.save(recipe);
        
        // 更新菜谱步骤（先删除旧的）
        List<RecipeStep> existingSteps = recipeStepRepository.findByRecipeRecipeIdOrderByStepNumber(recipeId);
        recipeStepRepository.deleteAll(existingSteps);
        
        // 添加新的步骤
        List<RecipeStepCreateDTO> steps = recipeRequest.getSteps();
        if (steps != null) {
            for (RecipeStepCreateDTO stepDTO : steps) {
                RecipeStep step = new RecipeStep();
                step.setRecipe(updatedRecipe);
                step.setStepNumber(stepDTO.getStepNumber());
                step.setDescription(stepDTO.getDescription());
                step.setImageUrl(stepDTO.getImageUrl());
                recipeStepRepository.save(step);
            }
        }
        
        // 更新食材
        List<RecipeIngredient> existingIngredients = recipeIngredientRepository.findByRecipeRecipeId(recipeId);
        recipeIngredientRepository.deleteAll(existingIngredients);
        
        // 添加新的食材
        List<RecipeIngredientCreateDTO> ingredients = recipeRequest.getIngredients();
        if (ingredients != null) {
            for (RecipeIngredientCreateDTO ingredientDTO : ingredients) {
                Integer ingredientId = ingredientDTO.getIngredientId();
                String quantity = ingredientDTO.getQuantity();
                
                Ingredient ingredient = ingredientRepository.findById(ingredientId)
                        .orElseThrow(() -> new RuntimeException("食材不存在"));
                
                RecipeIngredient recipeIngredient = new RecipeIngredient();
                RecipeIngredientId id = new RecipeIngredientId(updatedRecipe.getRecipeId(), ingredient.getIngredientId());
                recipeIngredient.setId(id);
                recipeIngredient.setRecipe(updatedRecipe);
                recipeIngredient.setIngredient(ingredient);
                recipeIngredient.setQuantity(quantity);
                
                recipeIngredientRepository.save(recipeIngredient);
            }
        }
        
        return getRecipeById(updatedRecipe.getRecipeId());
    }

    @Override
    @Transactional
    public void deleteRecipe(Integer recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("菜谱不存在"));
        
        User currentUser = userService.getCurrentUser();
        if (!recipe.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new RuntimeException("只能删除自己的菜谱");
        }
        
        // 删除相关的步骤和食材关联
        recipeStepRepository.deleteAll(recipeStepRepository.findByRecipeRecipeIdOrderByStepNumber(recipeId));
        recipeIngredientRepository.deleteAll(recipeIngredientRepository.findByRecipeRecipeId(recipeId));
        
        // 删除菜谱
        recipeRepository.delete(recipe);
    }    @Override
    public RecipeDTO convertToDTO(Recipe recipe) {
        if (recipe == null) {
            return null;
        }
        
        RecipeDTO dto = new RecipeDTO();
        dto.setRecipeId(recipe.getRecipeId());
        dto.setUserId(recipe.getUser().getUserId());
        dto.setUsername(recipe.getUser().getUsername());
        dto.setTitle(recipe.getTitle());
        dto.setDescription(recipe.getDescription());
        dto.setCategoryId(recipe.getCategoryId());
        
        // 获取分类名称
        if (recipe.getCategoryId() != null) {
            categoryRepository.findById(recipe.getCategoryId()).ifPresent(category -> 
                dto.setCategoryName(category.getName()));
        }
        
        dto.setCuisineTypeId(recipe.getCuisineTypeId());
        
        // 获取烹饪类型名称
        if (recipe.getCuisineTypeId() != null) {
            cuisineTypeRepository.findById(recipe.getCuisineTypeId()).ifPresent(cuisineType -> 
                dto.setCuisineTypeName(cuisineType.getName()));
        }
        
        dto.setCookingTime(recipe.getCookingTime());
        dto.setDifficultyLevel(recipe.getDifficultyLevel());
        dto.setImageUrl(recipe.getImageUrl());
        dto.setCreatedAt(recipe.getCreatedAt());

        // 获取步骤
        List<RecipeStepDTO> stepDTOs = recipeStepRepository.findByRecipeRecipeIdOrderByStepNumber(recipe.getRecipeId())
                .stream()
                .map(step -> new RecipeStepDTO(
                        step.getStepId(),
                        step.getStepNumber(),
                        step.getDescription(),
                        step.getImageUrl()))
                .collect(Collectors.toList());
        dto.setSteps(stepDTOs);
        
        // 获取食材
        List<RecipeIngredientDTO> ingredientDTOs = recipeIngredientRepository.findByRecipeRecipeId(recipe.getRecipeId())
                .stream()
                .map(ri -> new RecipeIngredientDTO(
                        ri.getIngredient().getIngredientId(),
                        ri.getIngredient().getName(),
                        ri.getQuantity()))
                .collect(Collectors.toList());
        dto.setIngredients(ingredientDTOs);
        
        return dto;
    }
    
    /**
     * 转换为不包含步骤信息的基础DTO
     */
    public RecipeDTO convertToBasicDTO(Recipe recipe) {
        if (recipe == null) {
            return null;
        }
        
        RecipeDTO dto = new RecipeDTO();
        dto.setRecipeId(recipe.getRecipeId());
        dto.setUserId(recipe.getUser().getUserId());
        dto.setUsername(recipe.getUser().getUsername());
        dto.setTitle(recipe.getTitle());
        dto.setDescription(recipe.getDescription());
        dto.setCategoryId(recipe.getCategoryId());
        
        // 获取分类名称
        if (recipe.getCategoryId() != null) {
            categoryRepository.findById(recipe.getCategoryId()).ifPresent(category -> 
                dto.setCategoryName(category.getName()));
        }
        
        dto.setCuisineTypeId(recipe.getCuisineTypeId());
        
        // 获取烹饪类型名称
        if (recipe.getCuisineTypeId() != null) {
            cuisineTypeRepository.findById(recipe.getCuisineTypeId()).ifPresent(cuisineType -> 
                dto.setCuisineTypeName(cuisineType.getName()));
        }
        
        dto.setCookingTime(recipe.getCookingTime());
        dto.setDifficultyLevel(recipe.getDifficultyLevel());
        dto.setImageUrl(recipe.getImageUrl());
        dto.setCreatedAt(recipe.getCreatedAt());
        
        // 获取食材
        List<RecipeIngredientDTO> ingredientDTOs = recipeIngredientRepository.findByRecipeRecipeId(recipe.getRecipeId())
                .stream()
                .map(ri -> new RecipeIngredientDTO(
                        ri.getIngredient().getIngredientId(),
                        ri.getIngredient().getName(),
                        ri.getQuantity()))
                .collect(Collectors.toList());
        dto.setIngredients(ingredientDTOs);
        
        // 不包含步骤信息
        dto.setSteps(new ArrayList<>());
        
        return dto;
    }@Override
    public List<RecipeDTO> getLatestRecipes(int limit) {
        return recipeRepository.findLatestRecipes().stream()
                .limit(limit)
                .map(this::convertToBasicDTO)
                .collect(Collectors.toList());
    }
}
