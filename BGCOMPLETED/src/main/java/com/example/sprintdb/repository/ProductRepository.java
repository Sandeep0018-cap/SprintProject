package com.example.sprintdb.repository;

import com.example.sprintdb.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Used by the service to list products by category for the page
    List<Product> findByCategoryIdOrderByBrandAscNameAsc(Long categoryId);
}