package com.example.sprintdb.repository;

import com.example.sprintdb.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryIdOrderByBrandAscNameAsc(Long categoryId);
}