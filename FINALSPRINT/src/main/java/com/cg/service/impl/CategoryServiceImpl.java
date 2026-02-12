package com.cg.service.impl;

import com.cg.dto.CategoryDto;
import com.cg.entity.Category;
import com.cg.exception.BadRequestException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repository.CategoryRepository;
import com.cg.service.CategoryService;

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
    @Override
    public CategoryDto findById(Long id) {
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return new CategoryDto(c.getCategoryId(), c.getName());
    }

    @Override
    @Transactional
    public CategoryDto update(Long id, String name) {
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        c.setName(name);
        categoryRepository.save(c);

        return new CategoryDto(c.getCategoryId(), c.getName());
    }
}
