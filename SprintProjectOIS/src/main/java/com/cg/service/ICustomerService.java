package com.cg.service;

import com.cg.dto.CustomerRowDto;
import com.cg.entity.Customer;
import java.util.List;

public interface ICustomerService { // Defines business operations for client relationship management

    Customer create(Customer c); // Handles the validation and persistence of new customer profiles
    
    List<CustomerRowDto> listCustomersWithLastPurchase(); // Generates an analytical list of clients paired with their latest order details
}
