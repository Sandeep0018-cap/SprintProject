package com.cg.service.impl;

import com.cg.dto.PurchaseCreateDto;
import com.cg.entity.*;
import com.cg.enums.PaymentMode;
import com.cg.exception.BadRequestException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.repository.*;
import com.cg.service.IPurchaseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PurchaseServiceImpl implements IPurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final PurchaseItemRepository purchaseItemRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public PurchaseServiceImpl(PurchaseRepository purchaseRepository,
                               PurchaseItemRepository purchaseItemRepository,
                               CustomerRepository customerRepository,
                               ProductRepository productRepository) {
        this.purchaseRepository = purchaseRepository;
        this.purchaseItemRepository = purchaseItemRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Purchase createPurchase(PurchaseCreateDto dto, String createdByUsername) {
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found: " + dto.getCustomerId()));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + dto.getProductId()));

        // Unified rule: Txn ID required for CARD and NETBANKING
        if ((dto.getPaymentMode() == PaymentMode.CARD || dto.getPaymentMode() == PaymentMode.NETBANKING)
                && (dto.getTransactionId() == null || dto.getTransactionId().isBlank())) {
            throw new BadRequestException("Transaction ID is required for CARD/NETBANKING payment");
        }

        if (dto.getPaymentMode() == PaymentMode.COD) {
            dto.setTransactionId(null);
        }

        Purchase purchase = new Purchase();
        purchase.setCustomer(customer);
        purchase.setPaymentMode(dto.getPaymentMode());
        purchase.setTransactionId(dto.getTransactionId());
        purchase.setCreatedByUsername(createdByUsername);
        Purchase saved = purchaseRepository.save(purchase);

        PurchaseItem item = new PurchaseItem();
        item.setPurchase(saved);
        item.setProduct(product);
        item.setQuantity(dto.getQuantity());
        item.setUnitPrice(product.getPrice());
        purchaseItemRepository.save(item);

        customer.setLastPurchasedProductName(product.getName());
        customer.setLastPurchasedBrand(product.getBrand());
        customerRepository.save(customer);

        return saved;
    }

    @Override
    public Map<String, Long> paymentModeCounts() {
        return purchaseRepository.findAll().stream()
                .collect(Collectors.groupingBy(p -> p.getPaymentMode().name(), Collectors.counting()));
    }
}