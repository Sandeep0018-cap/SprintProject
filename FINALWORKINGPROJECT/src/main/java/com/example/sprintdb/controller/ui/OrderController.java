//package com.example.sprintdb.controller.ui;
//
//import com.example.sprintdb.entity.Order;
//import com.example.sprintdb.repository.OrderRepository;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.util.List;
//
//@Controller
//@RequestMapping("/orders")
//public class OrderController {
//
//    private final OrderRepository orderRepository;
//
//    public OrderController(OrderRepository orderRepository) {
//        this.orderRepository = orderRepository;
//    }
//
//    @GetMapping
//    public String listOrders(Model model) {
//        List<Order> orders = orderRepository.findAll();
//        model.addAttribute("orders", orders);
//        return "orders-list";
//    }
//
//    @GetMapping("/{id}")
//    public String orderDetail(@PathVariable Long id, Model model) {
//        Order order = orderRepository.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
//        model.addAttribute("order", order);
//        return "order-detail";
//    }
//}










package com.example.sprintdb.controller.ui;

import com.example.sprintdb.entity.Order;
import com.example.sprintdb.entity.OrderStatus;
import com.example.sprintdb.repository.OrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
public class OrderController {

    private final OrderRepository orderRepository;
    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/orders")
    public String orders(@RequestParam(name="status", required=false) String status, Model model) {
        List<Order> orders;
        if (status == null || status.equalsIgnoreCase("ALL")) {
            orders = orderRepository.findAll();
        } else {
            OrderStatus st = OrderStatus.valueOf(status.toUpperCase());
            orders = orderRepository.findByStatusOrderByOrderIdDesc(st);
        }
        model.addAttribute("selectedStatus", status == null ? "ALL" : status.toUpperCase());
        model.addAttribute("statuses", Arrays.asList("ALL","PENDING","INTRANSIT","DELIVERED","CANCELLED"));
        model.addAttribute("orders", orders);
        return "orders";
    }
}