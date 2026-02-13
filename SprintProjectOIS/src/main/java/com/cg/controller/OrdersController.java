package com.cg.controller;

import com.cg.entity.Order;
import com.cg.enums.OrderStatus;
import com.cg.repository.OrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.Arrays;
import java.util.List;

@Controller
public class OrdersController {

    private final OrderRepository orderRepository;

    public OrdersController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/orders") // Entry point for viewing and filtering transaction history
    public String orders(@RequestParam(name = "status", required = false) String status, Model model) {
        List<Order> orders;

        // Determines whether to fetch the full dataset or a subset filtered by state
        if (status == null || status.equalsIgnoreCase("ALL")) {
            orders = orderRepository.findAll();
        } else {
            // Converts the URL string parameter into a typed Enum for safe database querying
            OrderStatus st = OrderStatus.valueOf(status.toUpperCase());
            orders = orderRepository.findByStatusOrderByOrderIdDesc(st);
        }

        model.addAttribute("selectedStatus", status == null ? "ALL" : status.toUpperCase()); // Tracks current filter for UI active states
        model.addAttribute("statuses", Arrays.asList("ALL", "PENDING", "INTRANSIT", "DELIVERED", "CANCELLED")); // Provides the filter toggle options
        model.addAttribute("orders", orders); // Passes the resulting list to the view template
        
        return "orders";
    }
}
