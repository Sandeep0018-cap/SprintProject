package com.example.sprintdb.repository;

import com.example.sprintdb.dto.CustomerRowDto;
import com.example.sprintdb.entity.Customer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Query("select new com.example.sprintdb.dto.CustomerRowDto(" +
            "c.id, " +
            "c.name, " +
            "c.phone, " +
            "c.city, " +
            "c.lastPurchasedProductName, " +
            "c.lastPurchasedBrand" +
            ") " +
            "from Customer c " +
            "order by c.id desc")
    List<CustomerRowDto> findCustomerRows();
}