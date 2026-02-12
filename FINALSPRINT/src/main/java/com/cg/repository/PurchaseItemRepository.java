package com.cg.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.entity.PurchaseItem;

import java.util.List;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {

    // Stats: list items for a set of products
    List<PurchaseItem> findByProductProductIdIn(List<Long> productIds);

    // Integrity check before deleting a product
    boolean existsByProductProductId(Long productId);
}
