package com.cg.service.impl;

import com.cg.dto.CategoryDto;
import com.cg.entity.Category;
import com.cg.exception.BadRequestException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repository.CategoryRepository;
import com.cg.service.ICategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service // Marks this as the business logic implementation for category operations
public class CategoryServiceImpl implements ICategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDto> findAll() {
        // Transforms all persisted entities into data transfer objects for the UI
        return categoryRepository.findAll().stream()
                .map(c -> new CategoryDto(c.getCategoryId(), c.getName()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional // Ensures atomicity for the creation process
    public CategoryDto create(String name) {
        // Enforces uniqueness by checking for existing names regardless of case
        if (categoryRepository.findByNameIgnoreCase(name).isPresent()) {
            throw new BadRequestException("Category already exists: " + name);
        }
        Category c = new Category();
        c.setName(name);
        Category saved = categoryRepository.save(c);
        return new CategoryDto(saved.getCategoryId(), saved.getName());
    }

    @Override
    @Transactional // Guarantees the delete operation is finalized in the database
    public void delete(Long id) {
        // Validates existence before attempting removal to prevent silent failures
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
        categoryRepository.delete(c);
    }

    @Override
    public CategoryDto findById(Long id) {
        // Retrieves a specific classification and converts it to a DTO for viewing
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return new CategoryDto(c.getCategoryId(), c.getName());
    }

    @Override
    @Transactional // Synchronizes state changes to the database within a transaction
    public CategoryDto update(Long id, String name) {
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        c.setName(name); // Updates the entity state in the persistence context
        categoryRepository.save(c);

        return new CategoryDto(c.getCategoryId(), c.getName());
    }
}
