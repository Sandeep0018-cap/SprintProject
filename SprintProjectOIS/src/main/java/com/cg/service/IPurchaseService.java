package com.cg.service;

import com.cg.dto.PurchaseCreateDto;
import com.cg.entity.Purchase;
import java.util.Map;

public interface IPurchaseService { // Interface for managing sales transactions and financial analytics

    // Executes the business workflow to validate, record, and audit a new sale
    Purchase createPurchase(PurchaseCreateDto dto, String createdByUsername);

    // Aggregates transaction volumes across all supported payment methods for reporting
    Map<String, Long> paymentModeCounts(); 
}
