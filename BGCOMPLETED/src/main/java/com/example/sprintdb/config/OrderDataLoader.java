package com.example.sprintdb.config;

import com.example.sprintdb.entity.Order;
import com.example.sprintdb.entity.OrderStatus;
import com.example.sprintdb.repository.OrderRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class OrderDataLoader {

    @Bean
    CommandLineRunner seedOrders(OrderRepository repo) {
        return args -> {
            if (repo.count() == 0) {
                Order o1 = new Order("ORD-2026-0001", OrderStatus.PENDING);
                o1.setUpdatedAt(LocalDateTime.now());

                Order o2 = new Order("ORD-2026-0002", OrderStatus.INTRANSIT);
                o2.setUpdatedAt(LocalDateTime.now());

                Order o3 = new Order("ORD-2026-0003", OrderStatus.DELIVERED);
                o3.setUpdatedAt(LocalDateTime.now());

                Order o4 = new Order("ORD-2026-0004", OrderStatus.CANCELLED);
                o4.setUpdatedAt(LocalDateTime.now());

                repo.save(o1);
                repo.save(o2);
                repo.save(o3);
                repo.save(o4);
            }
        };
    }
}