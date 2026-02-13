package com.cg.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cg.entity.Order;
import com.cg.enums.OrderStatus;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // Retrieves orders filtered by status, sorted by the most recent ID first
    List<Order> findByStatusOrderByOrderIdDesc(OrderStatus status); 

}
