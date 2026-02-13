package com.cg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cg.entity.Vendor;
import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

    // Validates the relationship between a supplier and a specific product in the join table
    boolean existsByVendorIdAndProducts_ProductId(Long vendorId, Long productId);

    // Retrieves the primary vendor record based on the lowest numerical ID for fallback logic
    Optional<Vendor> findFirstByOrderByVendorIdAsc();
    
    // Locates a specific supplier record matching the name while ignoring character casing
    Optional<Vendor> findByNameIgnoreCase(String name);
}
