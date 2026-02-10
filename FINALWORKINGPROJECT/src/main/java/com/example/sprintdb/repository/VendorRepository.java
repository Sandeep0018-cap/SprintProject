package com.example.sprintdb.repository;

import com.example.sprintdb.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VendorRepository extends JpaRepository<Vendor, Long> {

    boolean existsByVendorIdAndProducts_ProductId(Long vendorId, Long productId);

    // ✅ Minimal helper: fetch the first vendor (used as fallback)
    Optional<Vendor> findFirstByOrderByVendorIdAsc();
<<<<<<< HEAD
    
    // ✅ Find vendor by name (case-insensitive)
    Optional<Vendor> findByNameIgnoreCase(String name);
}
=======
}
>>>>>>> 4dae07659c187a62e65e7087a60de0d7f1af2310
