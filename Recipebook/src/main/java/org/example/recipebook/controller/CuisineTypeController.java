package org.example.recipebook.controller;

import org.example.recipebook.dto.ApiResponse;
import org.example.recipebook.entity.CuisineType;
import org.example.recipebook.repository.CuisineTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuisine-types")
public class CuisineTypeController {
    
    @Autowired
    private CuisineTypeRepository cuisineTypeRepository;
    
    @GetMapping("/{cuisineTypeId}")
    public ApiResponse<CuisineType> getCuisineTypeById(@PathVariable Integer cuisineTypeId) {
        try {
            CuisineType cuisineType = cuisineTypeRepository.findById(cuisineTypeId)
                    .orElseThrow(() -> new RuntimeException("烹饪类型不存在"));
            return ApiResponse.success(cuisineType);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
    
    @GetMapping
    public ApiResponse<List<CuisineType>> getAllCuisineTypes() {
        try {
            List<CuisineType> cuisineTypes = cuisineTypeRepository.findAll();
            return ApiResponse.success(cuisineTypes);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage());
        }
    }
}
