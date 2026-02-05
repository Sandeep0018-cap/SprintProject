package com.cg.service;

import java.util.List;

import com.cg.dto.CategoryDto;

public interface CategoryService {
	    public List<CategoryDto> findAll();
	    CategoryDto create(String name);
	    void delete(Long id);
	}
