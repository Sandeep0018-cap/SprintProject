package com.cg.service;

import java.util.List;
import java.util.Map;
import com.cg.dto.ProductDto;
import com.cg.dto.ProductStockAlertDto;

public interface ProductService {
    ProductDto createOrUpdate(ProductDto dto, Long categoryId);
    void delete(Long id);
    List<ProductDto> findByCategoryWithDerivedStats(Long categoryId);
    Map<String, List<ProductDto>> groupByBrand(List<ProductDto> products);
    List<ProductStockAlertDto> lowStockSummary(int threshold);

    // NEW: used by the edit screen to pre-fill the form
    ProductDto getByIdAsDto(Long id);
}