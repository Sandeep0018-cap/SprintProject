package com.example.sprintdb.repository;

import com.example.sprintdb.entity.PurchaseItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseItemRepository extends JpaRepository<PurchaseItem, Long> {
    List<PurchaseItem> findByProductIdIn(List<Long> productIds);
}
