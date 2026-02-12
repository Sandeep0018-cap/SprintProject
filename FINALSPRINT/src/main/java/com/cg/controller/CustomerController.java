package com.cg.controller;

import com.cg.entity.Customer;
import com.cg.service.CustomerService;

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
    public String list(Model model) {
        var rows = customerService.listCustomersWithLastPurchase();
        // PRINT EXACT ROWS THAT GO TO THE UI
        rows.stream().limit(10).forEach(r ->
            System.out.println(
                r.getCustomer_id() + " | " +
                r.getLastPurchasedProductName() + " | " +
                (r.getPaymentMode() != null ? r.getPaymentMode().name() : "null")
            )
        );
        model.addAttribute("rows", rows);
        return "customers";
    }


    @PostMapping
    public String create(@Valid @ModelAttribute Customer customer) {
        customerService.create(customer);
        // Redirect to the list view to see the new entry
        return "redirect:/customers/list"; 
    }

}
