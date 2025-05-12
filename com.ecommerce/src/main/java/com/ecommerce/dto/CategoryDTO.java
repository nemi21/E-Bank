package com.ecommerce.dto;

import jakarta.validation.constraints.NotBlank;

// DTO for creating a new category
public class CategoryDTO {

    @NotBlank(message = "Category name is required")
    private String name;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

