package com.cg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cg.entity.PurchaseItem;
import java.util.List;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {

    // Aggregates sales records for multiple specific products to calculate dashboard statistics
    List<PurchaseItem> findByProductProductIdIn(List<Long> productIds);

    // Determines if a product is linked to existing transactions to prevent orphaned records during deletion
    boolean existsByProductProductId(Long productId); 
}
