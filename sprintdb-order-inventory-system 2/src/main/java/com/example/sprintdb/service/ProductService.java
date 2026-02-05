package com.example.sprintdb.service;

import com.example.sprintdb.dto.ProductDto;

import java.util.List;
import java.util.Map;

public interface ProductService {
    ProductDto createOrUpdate(ProductDto dto, Long categoryId);
    void delete(Long id);

    List<ProductDto> findByCategoryWithDerivedStats(Long categoryId);

    // brand -> list of product dto
    Map<String, List<ProductDto>> groupByBrand(List<ProductDto> products);
}
