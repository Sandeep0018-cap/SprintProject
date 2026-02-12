package com.cg.controller;

import com.cg.dto.ProductDto;
import com.cg.service.CategoryService;
import com.cg.service.ProductService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class CategoryController {

    private final CategoryService categoryService;
    private final ProductService productService;

    public CategoryController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    // ============================
    // CATEGORY DETAIL PAGE
    // ============================
    @GetMapping("/categories/{id}")
    public String categoryDetail(@PathVariable Long id, Model model) {

        // Load products under category with computed stats
        List<ProductDto> products = productService.findByCategoryWithDerivedStats(id);

        // Group by brand
        Map<String, List<ProductDto>> byBrand = productService.groupByBrand(products);

        // Resolve category name
        String categoryName = categoryService.findAll().stream()
                .filter(c -> c.getCategoryId().equals(id))
                .map(c -> c.getName())
                .findFirst()
                .orElse("Category");

        model.addAttribute("categoryId", id);
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("productsByBrand", byBrand);

        return "category-detail";
    }

    // ============================
    // CATEGORY LISTING PAGE
    // ============================
    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "categories";
    }
}