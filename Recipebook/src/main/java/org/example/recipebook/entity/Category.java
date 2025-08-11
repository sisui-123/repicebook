package org.example.recipebook.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "categories")
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Integer categoryId;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private CategoryType type;
    
    public enum CategoryType {
        食材分类, 口味, 烹饪方式
    }
}
