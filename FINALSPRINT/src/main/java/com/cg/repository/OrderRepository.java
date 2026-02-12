// src/main/java/com/example/sprintdb/repository/OrderRepository.java
package com.cg.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.entity.Order;
import com.cg.enums.OrderStatus;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {


    List<Order> findByStatusOrderByOrderIdDesc(OrderStatus status);
}
