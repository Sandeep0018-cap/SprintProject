package com.cg.service.impl;
import java.util.List;
import java.util.Locale.Category;
import java.util.stream.Collectors;

import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cg.dto.CategoryDto;
import com.cg.repository.CategoryRepository;
import com.cg.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll().stream()
                .map(c -> new CategoryDto(c.getId(), c.getName()))
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
        return new CategoryDto(saved.getId(), saved.getName());
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + id));
        categoryRepository.delete(c);
    }
}
