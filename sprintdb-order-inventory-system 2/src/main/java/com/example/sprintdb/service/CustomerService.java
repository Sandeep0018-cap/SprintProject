package com.example.sprintdb.service;

import com.example.sprintdb.dto.CustomerRowDto;
import com.example.sprintdb.entity.Customer;

import java.util.List;

public interface CustomerService {
    Customer create(Customer c);
    List<CustomerRowDto> listCustomersWithLastPurchase();
}
