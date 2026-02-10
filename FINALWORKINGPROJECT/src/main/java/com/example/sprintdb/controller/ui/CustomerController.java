package com.example.sprintdb.controller.ui;

import com.example.sprintdb.entity.Customer;
import com.example.sprintdb.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/list")
    public String customers(Model model) {
        model.addAttribute("rows", customerService.listCustomersWithLastPurchase());
        model.addAttribute("customer", new Customer());
        return "customers";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute Customer customer) {
        customerService.create(customer);
        return "redirect:/admin/customers";
    }
}
