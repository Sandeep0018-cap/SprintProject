package com.cg.dto; // Defines the package for Data Transfer Objects

public class CategoryDto { // Lightweight object for moving category data between layers

    private Long categoryId; // Unique identifier for the category
    private String name; // Human-readable name of the category

    public CategoryDto() {} // Default constructor required for JSON/Form binding

    public CategoryDto(Long id, String name) { // Overloaded constructor for quick instantiation
        this.categoryId = id; 
        this.name = name; 
    }

    public Long getCategoryId() { // Returns the primary key of the category
        return categoryId;
    }

    public void setCategoryId(Long categoryId) { // Assigns the primary key
        this.categoryId = categoryId;
    }

    public String getName() { // Returns the category display name
        return name; 
    }

    public void setName(String name) { // Assigns the category display name
        this.name = name; 
    }
}
