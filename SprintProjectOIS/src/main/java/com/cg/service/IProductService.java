package com.cg.service;

import java.util.List;
import java.util.Map;
import com.cg.dto.ProductDto;
import com.cg.dto.ProductStockAlertDto;

public interface IProductService { // Contract for catalog management and inventory analytics

    ProductDto createOrUpdate(ProductDto dto, Long categoryId); // Orchestrates product persistence and category association

    void delete(Long id); // Handles record removal with dependency checks

    List<ProductDto> findByCategoryWithDerivedStats(Long categoryId); // Retrieves items with calculated fields like availability and sales volume

    Map<String, List<ProductDto>> groupByBrand(List<ProductDto> products); // Organizes product data into a manufacturer-based lookup map

    List<ProductStockAlertDto> lowStockSummary(int threshold); // Identifies items requiring replenishment based on a defined quantity limit

    ProductDto getByIdAsDto(Long id); // Fetches a single record mapped specifically for UI form population
}
