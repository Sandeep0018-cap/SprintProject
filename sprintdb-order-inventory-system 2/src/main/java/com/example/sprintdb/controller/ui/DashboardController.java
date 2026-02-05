package com.example.sprintdb.controller.ui;

import com.example.sprintdb.service.CategoryService;
import com.example.sprintdb.service.PurchaseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class DashboardController {

    private final CategoryService categoryService;
    private final PurchaseService purchaseService;

    public DashboardController(CategoryService categoryService, PurchaseService purchaseService) {
        this.categoryService = categoryService;
        this.purchaseService = purchaseService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("categories", categoryService.findAll());

        Map<String, Long> counts = purchaseService.paymentModeCounts();
        model.addAttribute("onlineCount", counts.getOrDefault("ONLINE", 0L));
        model.addAttribute("codCount", counts.getOrDefault("COD", 0L));
        return "dashboard";
    }
}
