package com.cg.controller;

import com.cg.enums.PaymentMode;
import com.cg.service.IPurchaseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class PaymentsController {

    private final IPurchaseService purchaseService;

    public PaymentsController(IPurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping("/payments") // Entry point for the financial transaction overview
    public String payments(Model model) {
        // Retrieves raw transaction counts categorized by their payment identifiers
        Map<String, Long> raw = purchaseService.paymentModeCounts();
        Map<String, Long> modeCounts = new LinkedHashMap<>();

        // Synchronizes every defined payment enum with the results to ensure zero-count visibility
        Arrays.stream(PaymentMode.values()).forEach(m -> 
            modeCounts.put(m.name(), raw.getOrDefault(m.name(), 0L))
        );

        model.addAttribute("modeCounts", modeCounts); // Binds the synchronized map to the UI layer
        return "payments";
    }
}
