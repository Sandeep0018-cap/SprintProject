package com.cg.service;

import java.util.List;
import com.cg.dto.CategoryDto;

public interface ICategoryService { // Defines the business contract for managing product classifications

    List<CategoryDto> findAll(); // Retrieves all category data mapped to transfer objects

    CategoryDto create(String name); // Persists a new category entry based on the provided name

    void delete(Long id); // Removes a specific category record from the system by its identifier

    CategoryDto findById(Long id); // Locates a single category and returns its data wrapper

    CategoryDto update(Long id, String name); // Modifies the attributes of an existing category record
}
