package com.cg.service;

import com.cg.dto.PurchaseCreateDto;
import com.cg.entity.Purchase;

import java.util.Map;

public interface PurchaseService {
    Purchase createPurchase(PurchaseCreateDto dto, String createdByUsername);

    // payment mode counts for dashboard
    Map<String, Long> paymentModeCounts();
}
