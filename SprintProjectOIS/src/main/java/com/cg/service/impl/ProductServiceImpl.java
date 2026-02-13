package com.cg.service.impl;

import com.cg.dto.ProductDto;
import com.cg.dto.ProductStockAlertDto;
import com.cg.entity.*;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repository.*;
import com.cg.service.IProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements IProductService {
    
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final VendorRepository vendorRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              PurchaseItemRepository purchaseItemRepository,
                              VendorRepository vendorRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.purchaseItemRepository = purchaseItemRepository;
        this.vendorRepository = vendorRepository;
    }

    @Override
    @Transactional // Orchestrates the full save/update cycle including automated vendor assignment
    public ProductDto createOrUpdate(ProductDto dto, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryId));

        Product p = (dto.getProductId() == null) ? new Product() 
                : productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + dto.getProductId()));

        p.setSkuId(dto.getSkuId());
        p.setName(dto.getName());
        p.setBrand(dto.getBrand());
        p.setPrice(dto.getPrice() == null ? 0.0 : dto.getPrice());
        p.setStockQty(dto.getStockQty() == null ? 0 : dto.getStockQty());
        p.setCategory(category);

        // Fallback logic: Assigns the first available vendor if no specific ID is provided
        Vendor vendor = (dto.getVendorId() == null) 
                ? vendorRepository.findFirstByOrderByVendorIdAsc().orElseThrow(() -> new IllegalStateException("No vendors available"))
                : vendorRepository.findById(dto.getVendorId()).orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        p.setVendor(vendor);

        Product saved = productRepository.save(p);
        return mapToDtoWithStats(saved); // Internal helper to attach calculated inventory metrics
    }

    @Override
    @Transactional(readOnly = true) // Optimized for read-only access during form pre-population
    public ProductDto getByIdAsDto(Long id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        return mapToDtoWithStats(p);
    }

    @Override
    @Transactional // Ensures cascading effects are handled during record removal
    public void delete(Long id) {
        if (!productRepository.existsById(id)) throw new ResourceNotFoundException("Product not found");
        productRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true) // Bulk calculation of sales and availability for the category view
    public List<ProductDto> findByCategoryWithDerivedStats(Long categoryId) {
        List<Product> products = productRepository.findByCategoryCategoryIdOrderByBrandAscNameAsc(categoryId);
        if (products.isEmpty()) return Collections.emptyList();

        List<Long> ids = products.stream().map(Product::getProductId).collect(Collectors.toList());
        List<PurchaseItem> items = purchaseItemRepository.findByProductProductIdIn(ids);

        // Maps calculated sales volumes and latest purchase references to product IDs
        Map<Long, Integer> soldMap = items.stream().collect(Collectors.groupingBy(pi -> pi.getProduct().getProductId(), Collectors.summingInt(PurchaseItem::getQuantity)));
        Map<Long, Long> lastPurchaseMap = items.stream().collect(Collectors.toMap(pi -> pi.getProduct().getProductId(), pi -> pi.getPurchase().getId(), (a, b) -> Math.max(a, b)));

        return products.stream().map(p -> {
            ProductDto d = mapToDtoWithStats(p, soldMap.getOrDefault(p.getProductId(), 0));
            d.setLastPurchaseId(lastPurchaseMap.get(p.getProductId()));
            return d;
        }).collect(Collectors.toList());
    }

    @Override
    public Map<String, List<ProductDto>> groupByBrand(List<ProductDto> products) {
        // Groups the list into a LinkedHashMap to preserve the brand's alphabetical sort order
        return products.stream().collect(Collectors.groupingBy(ProductDto::getBrand, LinkedHashMap::new, Collectors.toList()));
    }

    @Override
    @Transactional(readOnly = true) // Evaluates health of total inventory against defined thresholds
    public List<ProductStockAlertDto> lowStockSummary(int threshold) {
        return productRepository.findAll().stream().map(p -> {
            int available = p.getStockQty() - getSoldQuantity(p.getProductId());
            String severity = (available <= 4) ? "RED" : (available <= 10) ? "ORANGE" : "GREEN";
            return new ProductStockAlertDto(p.getProductId(), p.getBrand(), p.getName(), available, severity);
        }).collect(Collectors.toList());
    }

    private ProductDto mapToDtoWithStats(Product p) { // Overloaded helper for single-item mapping
        return mapToDtoWithStats(p, getSoldQuantity(p.getProductId()));
    }

    private ProductDto mapToDtoWithStats(Product p, int sold) { // Standardizes DTO creation with inventory math
        ProductDto d = new ProductDto();
        d.setProductId(p.getProductId());
        d.setSkuId(p.getSkuId());
        d.setName(p.getName());
        d.setBrand(p.getBrand());
        d.setPrice(p.getPrice());
        d.setStockQty(p.getStockQty());
        d.setVendorId(p.getVendor().getVendorId());
        d.setCategoryId(p.getCategory().getCategoryId());
        d.setSoldQty(sold);
        d.setAvailableQty(Math.max(0, p.getStockQty() - sold));
        return d;
    }

    private int getSoldQuantity(Long id) { // Private utility to isolate sales volume calculation
        return purchaseItemRepository.findByProductProductIdIn(Collections.singletonList(id))
                .stream().mapToInt(PurchaseItem::getQuantity).sum();
    }
}
