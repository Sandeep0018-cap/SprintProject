package com.cg.repositorytest;

import com.cg.entity.Product;
import com.cg.repository.ProductRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // use real DB
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    // =========================
    // ✅ POSITIVE TESTS
    // =========================

    @Test
    void findByCategoryCategoryIdOrderByBrandAscNameAsc_shouldReturnList() {
        List<Product> products =
                productRepository.findByCategoryCategoryIdOrderByBrandAscNameAsc(1L);

        assertNotNull(products);
    }

    @Test
    void findAllProductsByVendorId_shouldReturnList() {
        List<Product> products =
                productRepository.findAllProductsByVendorId(1L);

        assertNotNull(products);
    }

    @Test
    void findLowStockProductsByVendorId_shouldReturnList() {
        List<Product> products =
                productRepository.findLowStockProductsByVendorId(1L, 10);

        assertNotNull(products);
    }

    // =========================
    // ❌ NEGATIVE TESTS
    // =========================

    @Test
    void findByCategoryCategoryId_shouldNotThrowException() {
        assertDoesNotThrow(() ->
                productRepository.findByCategoryCategoryIdOrderByBrandAscNameAsc(999L));
    }

    @Test
    void findAllProductsByVendorId_shouldHandleInvalidVendor() {
        List<Product> products =
                productRepository.findAllProductsByVendorId(999L);

        assertNotNull(products);
    }

    @Test
    void findLowStockProductsByVendorId_shouldHandleInvalidVendor() {
        List<Product> products =
                productRepository.findLowStockProductsByVendorId(999L, 5);

        assertNotNull(products);
    }
}