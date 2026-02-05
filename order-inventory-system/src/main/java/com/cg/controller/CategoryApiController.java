package com.cg.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cg.dto.CategoryDto;
import com.cg.service.CategoryService;



@RestController
@RequestMapping("/api/categories")
public class CategoryApiController {
	    private final CategoryService categoryService;

	    public CategoryApiController(CategoryService categoryService) {
	        this.categoryService = categoryService;
	    }

	    @GetMapping
	    public ResponseEntity<List<CategoryDto>> all() {
	        return ResponseEntity.ok(categoryService.findAll());
	    }
	}


