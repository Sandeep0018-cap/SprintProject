package com.example.sprintdb.controller.ui;

import com.example.sprintdb.dto.ProductDto;
import com.example.sprintdb.repository.VendorRepository;
import com.example.sprintdb.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/products/ui")
public class AdminProductController {

    private final CategoryService categoryService;
    private final VendorRepository vendorRepository;

    public AdminProductController(CategoryService categoryService,
                                  VendorRepository vendorRepository) {
        this.categoryService = categoryService;
        this.vendorRepository = vendorRepository;
    }

    // GET /admin/products/ui/new?categoryId=...
    @GetMapping("/new")
    public String form(@RequestParam Long categoryId, Model model) {
        if (!model.containsAttribute("product")) {
            model.addAttribute("product", new ProductDto());
        }
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categories", categoryService.findAll());   // optional (if you later show category select)
        model.addAttribute("vendors", vendorRepository.findAll());     // ✅ provide vendors
        model.addAttribute("postAction", "/admin/products");
        return "product-form";
    }
}