package com.example.sprintdb.service.impl;

import com.example.sprintdb.dto.ProductDto;
import com.example.sprintdb.dto.ProductStockAlertDto;
import com.example.sprintdb.entity.Category;
import com.example.sprintdb.entity.Product;
import com.example.sprintdb.entity.PurchaseItem;
import com.example.sprintdb.entity.Vendor;
import com.example.sprintdb.exception.ResourceNotFoundException;
import com.example.sprintdb.repository.CategoryRepository;
import com.example.sprintdb.repository.ProductRepository;
import com.example.sprintdb.repository.PurchaseItemRepository;
import com.example.sprintdb.repository.VendorRepository;
import com.example.sprintdb.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

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
    @Transactional
    public ProductDto createOrUpdate(ProductDto dto, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryId));

        Product p = (dto.getProductId() == null)
                ? new Product()
                : productRepository.findById(dto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + dto.getProductId()));

        // Map simple fields (defensive defaults)
        p.setSkuId(trim(dto.getSkuId()));
        p.setName(trim(dto.getName()));
        p.setBrand(trim(dto.getBrand()));
        p.setPrice(dto.getPrice() == null ? 0.0 : dto.getPrice());
        p.setStockQty(dto.getStockQty() == null ? 0 : dto.getStockQty());

        // Always set category
        p.setCategory(category);

        // ✅ Minimal vendor fallback: if UI didn’t send vendorId, pick the first vendor
        Long vendorId = dto.getVendorId();
        Vendor vendor;
        if (vendorId == null) {
            vendor = vendorRepository.findFirstByOrderByVendorIdAsc()
                    .orElseThrow(() -> new IllegalStateException(
                            "No vendors found. Add at least one vendor or provide vendorId in the form."));
        } else {
            vendor = vendorRepository.findById(vendorId)
                    .orElseThrow(() -> new ResourceNotFoundException("Vendor not found: " + vendorId));
        }
        p.setVendor(vendor); // <-- prevents vendor_id NULL

        Product saved = productRepository.save(p);

        // Return minimal DTO with derived basics
        ProductDto out = new ProductDto();
        out.setProductId(saved.getProductId());
        out.setSkuId(saved.getSkuId());
        out.setName(saved.getName());
        out.setBrand(saved.getBrand());
        out.setPrice(saved.getPrice());
        out.setStockQty(saved.getStockQty());

        // Derived
        int sold = getSoldQuantity(saved.getProductId());
        out.setSoldQty(sold);
        int available = Math.max(0, (saved.getStockQty() == null ? 0 : saved.getStockQty()) - sold);
        out.setAvailableQty(available);

        return out;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        productRepository.delete(p);
    }

    @Override
    public List<ProductDto> findByCategoryWithDerivedStats(Long categoryId) {
        List<Product> products = productRepository.findByCategoryCategoryIdOrderByBrandAscNameAsc(categoryId);
        if (products.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> productIds = products.stream().map(Product::getProductId).collect(Collectors.toList());
        List<PurchaseItem> items = purchaseItemRepository.findByProductProductIdIn(productIds);

        Map<Long, Integer> soldByProduct = items.stream()
                .collect(Collectors.groupingBy(pi -> pi.getProduct().getProductId(),
                        Collectors.summingInt(PurchaseItem::getQuantity)));

        Map<Long, Long> lastPurchaseIdByProduct = items.stream()
                .collect(Collectors.groupingBy(pi -> pi.getProduct().getProductId(),
                        Collectors.collectingAndThen(
                                Collectors.mapping(pi -> pi.getPurchase().getId(), Collectors.maxBy(Long::compareTo)),
                                opt -> opt.orElse(null)
                        )));

        return products.stream().map(p -> {
            ProductDto dto = new ProductDto();
            dto.setProductId(p.getProductId());
            dto.setSkuId(p.getSkuId());
            dto.setName(p.getName());
            dto.setBrand(p.getBrand());
            dto.setPrice(p.getPrice());
            dto.setStockQty(p.getStockQty());

            int sold = soldByProduct.getOrDefault(p.getProductId(), 0);
            dto.setSoldQty(sold);

            int available = Math.max(0, (p.getStockQty() == null ? 0 : p.getStockQty()) - sold);
            dto.setAvailableQty(available);

            dto.setLastPurchaseId(lastPurchaseIdByProduct.get(p.getProductId()));
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public Map<String, List<ProductDto>> groupByBrand(List<ProductDto> products) {
        return products.stream()
                .collect(Collectors.groupingBy(ProductDto::getBrand, LinkedHashMap::new, Collectors.toList()));
    }

    @Override
    public List<ProductStockAlertDto> lowStockSummary(int threshold) {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) return Collections.emptyList();

        List<Long> productIds = products.stream().map(Product::getProductId).collect(Collectors.toList());
        List<PurchaseItem> items = purchaseItemRepository.findByProductProductIdIn(productIds);

        Map<Long, Integer> soldByProduct = items.stream()
                .collect(Collectors.groupingBy(pi -> pi.getProduct().getProductId(),
                        Collectors.summingInt(PurchaseItem::getQuantity)));

        List<ProductStockAlertDto> alerts = products.stream()
                .map(p -> {
                    int stock = p.getStockQty() == null ? 0 : p.getStockQty();
                    int sold = soldByProduct.getOrDefault(p.getProductId(), 0);
                    int available = Math.max(0, stock - sold);

                    String severity;
                    if (available <= 4) severity = "RED";
                    else if (available <= 10) severity = "ORANGE";
                    else severity = "GREEN";

                    return new ProductStockAlertDto(p.getProductId(), p.getBrand(), p.getName(), available, severity);

                })
                .filter(dto -> dto.getAvailableQty() <= threshold)
                .sorted(Comparator
                        .comparing(ProductStockAlertDto::getSeverity, (a, b) -> {
                            List<String> order = Arrays.asList("RED", "ORANGE", "GREEN");
                            return Integer.compare(order.indexOf(a), order.indexOf(b));
                        })
                        .thenComparing(ProductStockAlertDto::getAvailableQty))
                .collect(Collectors.toList());

        return alerts;
    }

    /* -------------------- helpers -------------------- */

    private int getSoldQuantity(Long productId) {
        List<PurchaseItem> items = purchaseItemRepository.findByProductProductIdIn(Collections.singletonList(productId));
        return items.stream().mapToInt(PurchaseItem::getQuantity).sum();
    }

    private static String trim(String s) {
        return s == null ? null : s.trim();
    }
}