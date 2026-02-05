package com.example.sprintdb.controller.api;

import com.example.sprintdb.dto.ProductDto;
import com.example.sprintdb.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

    private final ProductService productService;

    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<List<ProductDto>> byCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.findByCategoryWithDerivedStats(categoryId));
    }
}
