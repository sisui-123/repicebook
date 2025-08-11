package org.example.recipebook.service.impl;

import org.example.recipebook.entity.Category;
import org.example.recipebook.repository.CategoryRepository;
import org.example.recipebook.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category getCategoryById(Integer categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("分类不存在"));
    }    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
    
    @Override
    public List<Category> getCategoriesByType(Category.CategoryType type) {
        return categoryRepository.findByType(type);
    }
}
