package com.example.sprintdb.repository;

import com.example.sprintdb.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

    boolean existsByVendorIdAndProducts_ProductId(Long vendorId, Long productId);

    // ✅ Minimal helper: fetch the first vendor (used as fallback)
    Optional<Vendor> findFirstByOrderByVendorIdAsc();
}
