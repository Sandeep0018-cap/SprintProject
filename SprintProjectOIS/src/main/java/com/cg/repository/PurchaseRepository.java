package com.cg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cg.entity.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> { 
    // Manages persistence logic and transactional storage for high-level sale records
}
