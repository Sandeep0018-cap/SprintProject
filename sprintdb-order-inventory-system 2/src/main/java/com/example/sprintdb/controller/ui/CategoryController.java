package com.example.sprintdb.controller.ui;

import com.example.sprintdb.dto.ProductDto;
import com.example.sprintdb.service.CategoryService;
import com.example.sprintdb.service.ProductService;
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

    // Category list (for admin + staff)
    @GetMapping("/categories")
    public String categories(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "categories";
    }

    // DRILL DOWN: category -> brand -> products
    @GetMapping("/categories/{id}")
    public String categoryDetail(@PathVariable Long id, Model model) {
        List<ProductDto> products = productService.findByCategoryWithDerivedStats(id);
        Map<String, List<ProductDto>> byBrand = productService.groupByBrand(products);

        model.addAttribute("categoryId", id);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("productsByBrand", byBrand);
        return "category-detail";
    }
}
