package com.cg.service;

import com.cg.dto.CustomerRowDto;
import com.cg.entity.Customer;

import java.util.List;

public interface CustomerService {
    Customer create(Customer c);
    
    List<CustomerRowDto> listCustomersWithLastPurchase();
}
