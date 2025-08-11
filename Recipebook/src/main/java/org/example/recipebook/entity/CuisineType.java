package org.example.recipebook.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "cuisine_types")
@NoArgsConstructor
@AllArgsConstructor
public class CuisineType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cuisine_type_id")
    private Integer cuisineTypeId;
    
    @Column(name = "name")
    private String name;
}
