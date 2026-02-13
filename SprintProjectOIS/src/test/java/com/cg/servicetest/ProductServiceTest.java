package com.cg.servicetest;

import com.cg.dto.ProductDto;
import com.cg.entity.Category;
import com.cg.entity.Product;
import com.cg.entity.Vendor;
import com.cg.repository.*;
import com.cg.service.impl.ProductServiceImpl;
import com.cg.exception.ResourceNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private PurchaseItemRepository purchaseItemRepository;
    private VendorRepository vendorRepository;

    private ProductServiceImpl productService;

    private Category category;
    private Vendor vendor;
    private Product product;
    private ProductDto dto;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        categoryRepository = mock(CategoryRepository.class);
        purchaseItemRepository = mock(PurchaseItemRepository.class);
        vendorRepository = mock(VendorRepository.class);

        // ✅ constructor now matches exactly
        productService = new ProductServiceImpl(
                productRepository,
                categoryRepository,
                purchaseItemRepository,
                vendorRepository
        );

        category = new Category();
        category.setCategoryId(1L);

        vendor = new Vendor();
        vendor.setVendorId(10L);

        product = new Product();
        product.setProductId(5L);

        dto = new ProductDto();
        dto.setProductId(5L);
        dto.setName("Laptop");
        dto.setBrand("Dell");
        dto.setVendorId(10L);
        dto.setPrice(50000.0);
        dto.setStockQty(5);
    }

    // =========================
    // ✅ POSITIVE TEST
    // =========================
    @Test
    void createOrUpdate_shouldReturnDto_whenValid() {

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.findById(5L)).thenReturn(Optional.of(product));
        when(vendorRepository.findById(10L)).thenReturn(Optional.of(vendor));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDto result = productService.createOrUpdate(dto, 1L);

        assertNotNull(result);
        verify(productRepository).save(any(Product.class));
    }

    // =========================
    // ❌ NEGATIVE TESTS
    // =========================
    @Test
    void createOrUpdate_shouldThrow_whenCategoryNotFound() {

        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.createOrUpdate(dto, 1L));
    }

    @Test
    void createOrUpdate_shouldThrow_whenProductNotFound() {

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.createOrUpdate(dto, 1L));
    }

    @Test
    void createOrUpdate_shouldThrow_whenVendorNotFound() {

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.findById(5L)).thenReturn(Optional.of(product));
        when(vendorRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> productService.createOrUpdate(dto, 1L));
    }
}