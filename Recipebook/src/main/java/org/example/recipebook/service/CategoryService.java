package org.example.recipebook.service;

import org.example.recipebook.entity.Category;
import java.util.List;

public interface CategoryService {
    Category getCategoryById(Integer categoryId);
    List<Category> getAllCategories();
    List<Category> getCategoriesByType(Category.CategoryType type);
}
