package com.example.sprintdb.service;

import com.example.sprintdb.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> findAll();
    CategoryDto create(String name);
    void delete(Long id);
}
