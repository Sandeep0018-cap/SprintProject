package com.example.sprintdb.service.impl;

import com.example.sprintdb.dto.CustomerRowDto;
import com.example.sprintdb.entity.Customer;
import com.example.sprintdb.entity.Purchase;
import com.example.sprintdb.entity.PurchaseItem;
import com.example.sprintdb.repository.CustomerRepository;
import com.example.sprintdb.repository.PurchaseItemRepository;
import com.example.sprintdb.repository.PurchaseRepository;
import com.example.sprintdb.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
        List<Customer> customers = customerRepository.findAll();
        if (customers.isEmpty()) return Collections.emptyList();

        List<Purchase> purchases = purchaseRepository.findAll();

        // customerId -> last purchase (max id)
        Map<Long, Purchase> lastPurchaseByCustomer = purchases.stream()
                .collect(Collectors.toMap(p -> p.getCustomer().getId(),
                        p -> p,
                        (a, b) -> a.getId() > b.getId() ? a : b));

        // Build rows
        return customers.stream().map(c -> {
            CustomerRowDto dto = new CustomerRowDto();
            dto.setId(c.getId());
            dto.setName(c.getName());
            dto.setPhone(c.getPhone());
            dto.setCity(c.getCity());

            Purchase last = lastPurchaseByCustomer.get(c.getId());
            if (last != null) {
                dto.setPaymentMode(last.getPaymentMode());
                dto.setTransactionId(last.getTransactionId());
                dto.setLastPurchaseId(last.getId());
                String itemText = (c.getLastPurchasedBrand() == null ? "" : c.getLastPurchasedBrand() + " ")
                        + (c.getLastPurchasedProductName() == null ? "" : c.getLastPurchasedProductName());
                dto.setPurchasedItem(itemText.trim());
            } else {
                dto.setPurchasedItem("-");
            }
            return dto;
        }).collect(Collectors.toList());
    }
}
