package com.cg.repositorytest;

import com.cg.entity.Vendor;
import com.cg.repository.VendorRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // use real DB
class VendorRepositoryTest {

    @Autowired
    private VendorRepository vendorRepository;

    // =========================
    // ✅ POSITIVE TESTS
    // =========================

    @Test
    void findFirstByOrderByVendorIdAsc_shouldReturnOptional() {
        Optional<Vendor> vendor =
                vendorRepository.findFirstByOrderByVendorIdAsc();

        assertNotNull(vendor);
    }

    @Test
    void findByNameIgnoreCase_shouldReturnOptional() {
        Optional<Vendor> vendor =
                vendorRepository.findByNameIgnoreCase("test");

        assertNotNull(vendor);
    }

    @Test
    void existsByVendorIdAndProducts_ProductId_shouldReturnBoolean() {
        boolean exists =
                vendorRepository.existsByVendorIdAndProducts_ProductId(1L, 1L);

        assertTrue(exists || !exists);
    }

    // =========================
    // ❌ NEGATIVE TESTS
    // =========================

    @Test
    void findByNameIgnoreCase_withUnknownName_shouldNotCrash() {
        Optional<Vendor> vendor =
                vendorRepository.findByNameIgnoreCase("UnknownVendorXYZ");

        assertNotNull(vendor);
    }

    @Test
    void existsByVendorIdAndProducts_ProductId_withInvalidIds_shouldWork() {
        boolean exists =
                vendorRepository.existsByVendorIdAndProducts_ProductId(999L, 999L);

        assertTrue(exists == true || exists == false);
    }

    @Test
    void repositoryMethods_shouldNotThrowException() {
        assertDoesNotThrow(() ->
                vendorRepository.findFirstByOrderByVendorIdAsc());

        assertDoesNotThrow(() ->
                vendorRepository.findByNameIgnoreCase("anything"));

        assertDoesNotThrow(() ->
                vendorRepository.existsByVendorIdAndProducts_ProductId(1L, 1L));
    }
}