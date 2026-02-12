package com.cg.controller;

import com.cg.dto.PurchaseCreateDto;
import com.cg.enums.PaymentMode;
import com.cg.repository.CustomerRepository;
import com.cg.repository.ProductRepository;
import com.cg.service.PurchaseService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
        if (!model.containsAttribute("dto")) {
            model.addAttribute("dto", new PurchaseCreateDto());
        }
        model.addAttribute("customers", customerRepository.findAll());
        model.addAttribute("products", productRepository.findAll());
        model.addAttribute("modes", PaymentMode.values());
        return "purchase-form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("dto") PurchaseCreateDto dto,
                         BindingResult binding,
                         Authentication auth,
                         Model model,
                         RedirectAttributes ra) {

        if (dto.getPaymentMode() == PaymentMode.NETBANKING &&
            (dto.getTransactionId() == null || dto.getTransactionId().isBlank())) {
            binding.rejectValue("transactionId", "NotBlank",
                    "Transaction ID is required for ONLINE payments");
        }

        if (binding.hasErrors()) {
            model.addAttribute("customers", customerRepository.findAll());
            model.addAttribute("products", productRepository.findAll());
            model.addAttribute("modes", PaymentMode.values());
            return "purchase-form";
        }

        final String username = (auth != null ? auth.getName() : "staff");
        purchaseService.createPurchase(dto, username);

        ra.addFlashAttribute("success", "Sale recorded successfully");
        return "redirect:/dashboard";
    }
}