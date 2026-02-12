package com.cg.service.impl;

import com.cg.dto.CustomerRowDto;
import com.cg.entity.Customer;
import com.cg.repository.CustomerRepository;
import com.cg.repository.PurchaseRepository;
import com.cg.repository.PurchaseItemRepository;
import com.cg.service.CustomerService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final PurchaseRepository purchaseRepository;
    private final PurchaseItemRepository purchaseItemRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               PurchaseRepository purchaseRepository,
                               PurchaseItemRepository purchaseItemRepository) {
        this.customerRepository = customerRepository;
        this.purchaseRepository = purchaseRepository;
        this.purchaseItemRepository = purchaseItemRepository;
    }

    @Override
    @Transactional
    public Customer create(Customer c) {
        return customerRepository.save(c);
    }

    @Override
    public List<CustomerRowDto> listCustomersWithLastPurchase() {
        // 🚀 FIX: return the repository results directly
        return customerRepository.listCustomersWithLastPurchase();
    }
}