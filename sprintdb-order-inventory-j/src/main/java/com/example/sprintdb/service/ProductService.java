package com.example.sprintdb.service;

import com.example.sprintdb.dto.ProductDto;
import com.example.sprintdb.dto.ProductStockAlertDto;

import java.util.List;
import java.util.Map;

public interface ProductService {
    ProductDto createOrUpdate(ProductDto dto, Long categoryId);
    void delete(Long id);
    List<ProductDto> findByCategoryWithDerivedStats(Long categoryId);
    Map<String, List<ProductDto>> groupByBrand(List<ProductDto> products);

    // NEW: low stock summary (threshold inclusive)
    List<ProductStockAlertDto> lowStockSummary(int threshold);
}