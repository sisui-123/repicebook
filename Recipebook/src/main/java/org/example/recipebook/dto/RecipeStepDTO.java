package org.example.recipebook.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeStepDTO {
    private Integer stepId;
    private Integer stepNumber;
    private String description;
    private String imageUrl;
}
