package com.example.sprintdb.service.impl;

import com.example.sprintdb.dto.ProductDto;
import com.example.sprintdb.entity.Category;
import com.example.sprintdb.entity.Product;
import com.example.sprintdb.entity.PurchaseItem;
import com.example.sprintdb.exception.ResourceNotFoundException;
import com.example.sprintdb.repository.CategoryRepository;
import com.example.sprintdb.repository.ProductRepository;
import com.example.sprintdb.repository.PurchaseItemRepository;
import com.example.sprintdb.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final PurchaseItemRepository purchaseItemRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryRepository categoryRepository,
                              PurchaseItemRepository purchaseItemRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.purchaseItemRepository = purchaseItemRepository;
    }

    @Override
    @Transactional
    public ProductDto createOrUpdate(ProductDto dto, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + categoryId));

        Product p;
        if (dto.getId() == null) {
            p = new Product();
        } else {
            p = productRepository.findById(dto.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + dto.getId()));
        }

        p.setSkuId(dto.getSkuId());
        p.setName(dto.getName());
        p.setBrand(dto.getBrand());
        p.setPrice(dto.getPrice());
        p.setStockQty(dto.getStockQty());
        p.setCategory(category);

        Product saved = productRepository.save(p);

        ProductDto out = new ProductDto();
        out.setId(saved.getId());
        out.setSkuId(saved.getSkuId());
        out.setName(saved.getName());
        out.setBrand(saved.getBrand());
        out.setPrice(saved.getPrice());
        out.setStockQty(saved.getStockQty());
        out.setSoldQty(0);
        out.setAvailableQty(saved.getStockQty());
        return out;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Product p = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + id));
        productRepository.delete(p);
    }

    /**
     * SOLD QTY is derived from PurchaseItems using Java 8 Streams.
     * For each product: soldQty = sum(quantity for productId)
     * lastPurchaseId = max(purchase.id for that product)
     */
    @Override
    public List<ProductDto> findByCategoryWithDerivedStats(Long categoryId) {
        List<Product> products = productRepository.findByCategoryIdOrderByBrandAscNameAsc(categoryId);
        if (products.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> productIds = products.stream().map(Product::getId).collect(Collectors.toList());

        List<PurchaseItem> items = purchaseItemRepository.findByProductIdIn(productIds);

        Map<Long, Integer> soldByProduct = items.stream()
                .collect(Collectors.groupingBy(pi -> pi.getProduct().getId(),
                        Collectors.summingInt(PurchaseItem::getQuantity)));

        Map<Long, Long> lastPurchaseIdByProduct = items.stream()
                .collect(Collectors.groupingBy(pi -> pi.getProduct().getId(),
                        Collectors.collectingAndThen(
                                Collectors.mapping(pi -> pi.getPurchase().getId(), Collectors.maxBy(Long::compareTo)),
                                opt -> opt.orElse(null)
                        )));

        return products.stream().map(p -> {
            ProductDto dto = new ProductDto();
            dto.setId(p.getId());
            dto.setSkuId(p.getSkuId());
            dto.setName(p.getName());
            dto.setBrand(p.getBrand());
            dto.setPrice(p.getPrice());
            dto.setStockQty(p.getStockQty());

            int sold = soldByProduct.getOrDefault(p.getId(), 0);
            dto.setSoldQty(sold);

            int available = Math.max(0, p.getStockQty() - sold);
            dto.setAvailableQty(available);

            dto.setLastPurchaseId(lastPurchaseIdByProduct.get(p.getId()));
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public Map<String, List<ProductDto>> groupByBrand(List<ProductDto> products) {
        // Java 8 Streams: brand -> products
        return products.stream()
                .collect(Collectors.groupingBy(ProductDto::getBrand, LinkedHashMap::new, Collectors.toList()));
    }
}
