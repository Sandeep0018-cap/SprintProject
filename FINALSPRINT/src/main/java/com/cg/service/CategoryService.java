package com.cg.service;

import java.util.List;

import com.cg.dto.CategoryDto;

public interface CategoryService {
    List<CategoryDto> findAll();
    CategoryDto create(String name);
    void delete(Long id);
    CategoryDto findById(Long id);
    CategoryDto update(Long id, String name);
}
