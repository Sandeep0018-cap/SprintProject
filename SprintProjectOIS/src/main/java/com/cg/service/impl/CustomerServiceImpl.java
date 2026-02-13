package com.cg.service.impl;

import com.cg.dto.CustomerRowDto;
import com.cg.entity.Customer;
import com.cg.repository.CustomerRepository;
import com.cg.repository.PurchaseRepository;
import com.cg.repository.PurchaseItemRepository;
import com.cg.service.ICustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service // Implementation of customer-related business logic and reporting
public class CustomerServiceImpl implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final PurchaseRepository purchaseRepository; // Reference for potential purchase-related validations
    private final PurchaseItemRepository purchaseItemRepository; // Reference for potential item-level analytics

    public CustomerServiceImpl(CustomerRepository customerRepository,
                               PurchaseRepository purchaseRepository,
                               PurchaseItemRepository purchaseItemRepository) {
        this.customerRepository = customerRepository;
        this.purchaseRepository = purchaseRepository;
        this.purchaseItemRepository = purchaseItemRepository;
    }

    @Override
    @Transactional // Ensures the customer record is fully persisted within a transaction boundary
    public Customer create(Customer c) {
        return customerRepository.save(c); // Saves the validated customer entity to the database
    }

    @Override
    public List<CustomerRowDto> listCustomersWithLastPurchase() {
        // Delegates to the repository for a high-performance JPQL projection of purchase history
        return customerRepository.listCustomersWithLastPurchase();
    }
}
