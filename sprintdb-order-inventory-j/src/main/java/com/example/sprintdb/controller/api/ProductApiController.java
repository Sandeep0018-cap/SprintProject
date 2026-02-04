package com.example.sprintdb.controller.api;

import com.example.sprintdb.dto.ProductDto;
import com.example.sprintdb.service.ProductService;
import com.example.sprintdb.repository.CategoryRepository;
import com.example.sprintdb.repository.ProductRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/products")
public class ProductApiController {

    private final ProductService productService;
    private final CategoryRepository categoryRepository;

    public ProductApiController(ProductService productService,
                                CategoryRepository spc) {
        this.productService = productService;
        this.categoryRepository = spc;
    }

    // Renders 'product-form.html'
    // Open as: /admin/products/new?categoryId=123
    @GetMapping("/new")
    public String newProduct(@RequestParam("categoryId") Long categoryId, Model model) {
        model.addAttribute("product", new ProductDto()); // <-- changed to DTO
        model.addAttribute("categoryId", categoryId);
        return "product-form";
    }

    // Handles th:action="@{/admin/products}"
    @PostMapping
    public String create(@Valid @ModelAttribute("product") ProductDto product, // <-- DTO
                         BindingResult binding,
                         @RequestParam("categoryId") Long categoryId,
                         RedirectAttributes ra,
                         Model model) {

        // Keep categoryId in model for re-render if errors
        model.addAttribute("categoryId", categoryId);

        if (binding.hasErrors()) {
            return "product-form";
        }

        try {
            // Optional: product.setCategoryId(categoryId); // only if your service uses it
            productService.createOrUpdate(product, categoryId);
        } catch (DataIntegrityViolationException ex) {
            binding.rejectValue("skuId", "sku.duplicate", "SKU already exists");
            return "product-form";
        }

        ra.addFlashAttribute("msg", "Product added successfully");
        return "redirect:/categories/" + categoryId;
    }
}