package com.cg.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.entity.Vendor;

import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

    boolean existsByVendorIdAndProducts_ProductId(Long vendorId, Long productId);

    // ✅ Minimal helper: fetch the first vendor (used as fallback)
    Optional<Vendor> findFirstByOrderByVendorIdAsc();
    
    // ✅ Find vendor by name (case-insensitive)
    Optional<Vendor> findByNameIgnoreCase(String name);
}