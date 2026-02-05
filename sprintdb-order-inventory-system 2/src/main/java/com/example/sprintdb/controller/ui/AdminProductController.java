package com.example.sprintdb.controller.ui;

import com.example.sprintdb.dto.ProductDto;
import com.example.sprintdb.service.CategoryService;
import com.example.sprintdb.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    public AdminProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    // Admin will use categories drill-down; this page is quick add/edit.
    @GetMapping("/new")
    public String form(@RequestParam Long categoryId, Model model) {
        model.addAttribute("product", new ProductDto());
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categories", categoryService.findAll());
        return "product-form";
    }

    @PostMapping
    public String save(@ModelAttribute ProductDto product, @RequestParam Long categoryId) {
        productService.createOrUpdate(product, categoryId);
        return "redirect:/categories/" + categoryId;
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, @RequestParam Long categoryId) {
        productService.delete(id);
        return "redirect:/categories/" + categoryId;
    }
}
