package com.example.sprintdb.service;

import com.example.sprintdb.dto.PurchaseCreateDto;
import com.example.sprintdb.entity.Purchase;

import java.util.Map;

public interface PurchaseService {
    Purchase createPurchase(PurchaseCreateDto dto, String createdByUsername);

    // payment mode counts for dashboard
    Map<String, Long> paymentModeCounts();
}
