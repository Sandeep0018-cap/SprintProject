package com.cg.controller;

import com.cg.dto.ProductDto;
import com.cg.service.ICategoryService;
import com.cg.service.IProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Controller
public class CategoryController {

    private final ICategoryService categoryService;
    private final IProductService productService;

    public CategoryController(ICategoryService categoryService, IProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    // SHOW ALL CATEGORIES (ADMIN + STAFF + USER)
    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "category/categories";   // <--- FIXED
    }

    // SHOW ALL PRODUCTS INSIDE SELECTED CATEGORY
    @GetMapping("/categories/{id}")
    public String categoryDetail(@PathVariable Long id, Model model) {

        List<ProductDto> products =
                productService.findByCategoryWithDerivedStats(id);

        Map<String, List<ProductDto>> byBrand =
                productService.groupByBrand(products);

        String categoryName = categoryService.findAll().stream()
                .filter(c -> c.getCategoryId().equals(id))
                .map(c -> c.getName())
                .findFirst()
                .orElse("Category");

        model.addAttribute("categoryId", id);
        model.addAttribute("categoryName", categoryName);
        model.addAttribute("productsByBrand", byBrand);

        return "category/category-detail";  // <--- FIXED
    }
}