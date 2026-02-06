// src/main/java/com/example/sprintdb/repository/OrderRepository.java
package com.example.sprintdb.repository;

import com.example.sprintdb.entity.Order;
import com.example.sprintdb.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatusOrderByIdDesc(OrderStatus status);
}
