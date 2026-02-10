package com.example.sprintdb.service.impl;

import com.example.sprintdb.dto.CategoryDto;
import com.example.sprintdb.entity.Category;
import com.example.sprintdb.exception.BadRequestException;
import com.example.sprintdb.exception.ResourceNotFoundException;
import com.example.sprintdb.repository.CategoryRepository;
import com.example.sprintdb.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(c -> new CategoryDto(c.getCategoryId(), c.getName()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryDto create(String name) {
        if (categoryRepository.findByNameIgnoreCase(name).isPresent()) {
            throw new BadRequestException("Category already exists: " + name);
        }
        Category c = new Category();
        c.setName(name);
        Category saved = categoryRepository.save(c);
        return new CategoryDto(saved.getCategoryId(), saved.getName());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
        categoryRepository.delete(c);
    }
}
