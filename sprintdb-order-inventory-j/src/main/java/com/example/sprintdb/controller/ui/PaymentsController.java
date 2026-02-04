package com.example.sprintdb.controller.ui;

import com.example.sprintdb.entity.PaymentMode;
import com.example.sprintdb.service.PurchaseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class PaymentsController {

    private final PurchaseService purchaseService;
    public PaymentsController(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @GetMapping("/payments")
    public String payments(Model model) {
        Map<String, Long> raw = purchaseService.paymentModeCounts();
        Map<String, Long> modeCounts = new LinkedHashMap<>();
        for (PaymentMode m : PaymentMode.values()) {
            modeCounts.put(m.name(), raw.getOrDefault(m.name(), 0L));
        }
        model.addAttribute("modeCounts", modeCounts);
        return "payments";
    }
}