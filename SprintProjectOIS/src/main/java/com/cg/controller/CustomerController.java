package com.cg.controller;

import com.cg.entity.Customer;
import com.cg.service.ICustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    private final ICustomerService customerService;

    public CustomerController(ICustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/list") // Handles navigation to the customer directory view
    public String list(Model model) {
        // Aggregates client data along with their most recent transaction date/info
        var rows = customerService.listCustomersWithLastPurchase();
        model.addAttribute("rows", rows);
        return "customers";
    }

    @PostMapping // Handles the submission of the new customer registration form
    public String create(@Valid @ModelAttribute Customer customer) {
        // Triggers validation and persistence of the customer entity
        customerService.create(customer);
        return "redirect:/customers/list"; 
    }
}
