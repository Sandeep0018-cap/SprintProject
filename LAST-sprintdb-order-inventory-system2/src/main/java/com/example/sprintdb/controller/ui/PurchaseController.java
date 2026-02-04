package com.example.sprintdb.controller.ui;

import com.example.sprintdb.dto.PurchaseCreateDto;
import com.example.sprintdb.entity.PaymentMode;
import com.example.sprintdb.repository.CustomerRepository;
import com.example.sprintdb.repository.ProductRepository;
import com.example.sprintdb.service.PurchaseService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/staff/purchases")
public class PurchaseController {

    private final PurchaseService purchaseService;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public PurchaseController(PurchaseService purchaseService,
                              CustomerRepository customerRepository,
                              ProductRepository productRepository) {
        this.purchaseService = purchaseService;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("dto", new PurchaseCreateDto());
        model.addAttribute("customers", customerRepository.findAll());
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("modes", PaymentMode.values());
        return "purchase-form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("dto") PurchaseCreateDto dto, Authentication auth) {
        purchaseService.createPurchase(dto, auth.getName());
        return "redirect:/dashboard";
    }
}
