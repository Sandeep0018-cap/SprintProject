package com.example.sprintdb.controller.api;

import com.example.sprintdb.dto.CategoryDto;
import com.example.sprintdb.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
